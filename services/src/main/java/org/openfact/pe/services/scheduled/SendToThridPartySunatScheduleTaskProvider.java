package org.openfact.pe.services.scheduled;

import org.jboss.logging.Logger;
import org.openfact.models.*;
import org.openfact.models.enums.DocumentType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.utils.TypeToModel;
import org.openfact.pe.models.enums.*;
import org.openfact.pe.models.types.summary.SummaryDocumentsType;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.representations.idm.BillingPaymentRepresentation;
import org.openfact.pe.representations.idm.SummaryLineRepresentation;
import org.openfact.pe.representations.idm.SummaryRepresentation;
import org.openfact.pe.representations.idm.TaxTotalRepresentation;
import org.openfact.pe.services.managers.SunatDocumentManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SendToThridPartySunatScheduleTaskProvider implements OrganizationScheduleTaskProvider {

    public static final Logger log = Logger.getLogger(SendToThridPartySunatScheduleTaskProvider.class);

    public static final String JOB_NAME = "SENT_TO_THRID_PARTY_SUNAT";

    private int RETRY = 20;

    protected boolean isActive;

    public SendToThridPartySunatScheduleTaskProvider(boolean isActive, int retries) {
        this.isActive = isActive;
        this.RETRY = retries;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void run(OpenfactSession session, OrganizationModel organization) throws JobException {
        // Start Job
        long startTime = System.currentTimeMillis();
        long readCount = 0;

        readCount += sendSummaryDocument(session, session.organizations().getOrganization(organization.getId()));
        readCount += sendFacturas(session, session.organizations().getOrganization(organization.getId()));

        long endTime = System.currentTimeMillis();

        // Save Report
        JobReportModel jobReportModel = session.jobReports().createJobReport(organization, JOB_NAME);

        // Metrics
        jobReportModel.setStartTime(startTime);
        jobReportModel.setEndTime(endTime);
        jobReportModel.setDuration(endTime - startTime);
        jobReportModel.setReadCount(readCount);
    }

    private long sendSummaryDocument(OpenfactSession session, OrganizationModel organization) {
        List<DocumentModel> document = session.documents().createQuery(organization)
                .filterText("B", DocumentModel.DOCUMENT_ID)
                .documentType(DocumentType.INVOICE.toString())
                .requiredAction(RequiredAction.SEND_TO_TRIRD_PARTY)
                .orderByAsc(DocumentModel.DOCUMENT_ID)
                .getResultList();

        Map<LocalDate, List<DocumentModel>> groups = document.stream().collect(Collectors.groupingBy(f -> f.getCreatedTimestamp().toLocalDate()));

        for (Map.Entry<LocalDate, List<DocumentModel>> entrySet : groups.entrySet()) {
            Map<String, List<DocumentModel>> groupsBySerie = entrySet.getValue().stream().collect(Collectors.groupingBy(f -> f.getDocumentId().split("-")[0]));

            for (Map.Entry<String, List<DocumentModel>> entrySetBySerie : groupsBySerie.entrySet()) {
                String serie = entrySetBySerie.getKey();
                List<DocumentModel> documents = entrySetBySerie.getValue();

                SummaryLineRepresentation lineBoletasRep = new SummaryLineRepresentation();
                SummaryLineRepresentation lineNotaCreditoRep = new SummaryLineRepresentation();
                SummaryLineRepresentation lineNotaDebitoRep = new SummaryLineRepresentation();

                // Boletas
                lineBoletasRep.setCodigoDocumento(TipoInvoice.BOLETA.getCodigo());
                lineBoletasRep.setSerieDocumento(serie);
                lineBoletasRep.setNumeroInicioDocumento(Integer.valueOf(documents.get(0).getDocumentId().split("-")[1]).toString());
                lineBoletasRep.setNumeroFinDocumento(Integer.valueOf(documents.get(document.size() - 1).getDocumentId().split("-")[1]).toString());
                lineBoletasRep.setMoneda(TipoMoneda.PEN.getCodigo());
                lineBoletasRep.setTotalAmount(documents.stream()
                        .map(f -> f.getFirstAttribute(TypeToModel.LEGAL_MONETARY_TOTAL_PAYABLE_AMOUNT))
                        .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                lineBoletasRep.setChargeAmount(documents.stream()
                        .map(f -> f.getFirstAttribute(TypeToModel.LEGAL_MONETARY_TOTAL_CHARGE_TOTAL_AMOUNT))
                        .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

                //
                BillingPaymentRepresentation totalOperacionesGravadas = new BillingPaymentRepresentation();
                totalOperacionesGravadas.setInstructionID(TipoValorVenta.GRAVADO.getCodigo());
                totalOperacionesGravadas.setPaidAmount(documents.stream()
                        .map(f -> f.getFirstAttribute(SunatTypeToModel.TOTAL_OPERACIONES_GRAVADAS))
                        .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

                BillingPaymentRepresentation totalOperacionesExoneradas = new BillingPaymentRepresentation();
                totalOperacionesExoneradas.setInstructionID(TipoValorVenta.EXONERADO.getCodigo());
                totalOperacionesExoneradas.setPaidAmount(documents.stream()
                        .map(f -> f.getFirstAttribute(SunatTypeToModel.TOTAL_OPERACIONES_EXONERADAS))
                        .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

                BillingPaymentRepresentation totalOperacionesInafectas = new BillingPaymentRepresentation();
                totalOperacionesInafectas.setInstructionID(TipoValorVenta.INAFECTO.getCodigo());
                totalOperacionesInafectas.setPaidAmount(documents.stream()
                        .map(f -> f.getFirstAttribute(SunatTypeToModel.TOTAL_OPERACIONES_INAFECTAS))
                        .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

                lineBoletasRep.setPayment(Arrays.asList(totalOperacionesGravadas, totalOperacionesExoneradas, totalOperacionesInafectas));

                //
                TaxTotalRepresentation taxTotalRepresentation = new TaxTotalRepresentation();
                taxTotalRepresentation.setTaxSchemeID(TipoTributo.IGV.getId());
                taxTotalRepresentation.setTaxSchemeName(TipoTributo.IGV.toString());
                taxTotalRepresentation.setTaxTypeCode(TipoTributo.IGV.getCodigo());
                taxTotalRepresentation.setTaxAmount(documents.stream()
                        .map(f -> f.getFirstAttribute(SunatTypeToModel.TAX_TOTAL_AMOUNT))
                        .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

                lineBoletasRep.setTaxTotal(Arrays.asList(taxTotalRepresentation));

                SummaryRepresentation summaryRep = new SummaryRepresentation();
                summaryRep.setFechaDeEmision(LocalDateTime.now());
                summaryRep.setFechaDeReferencia(entrySet.getKey().atStartOfDay());
                summaryRep.setLine(Arrays.asList(lineBoletasRep/*, lineNotaCreditoRep, lineNotaDebitoRep*/));

                SunatDocumentManager sunatDocumentManager = new SunatDocumentManager(session);
                SummaryDocumentsType summaryType = SunatRepresentationToType.toSummaryDocumentType(session, organization, summaryRep);
                DocumentModel summaryModel = sunatDocumentManager.addSummaryDocument(organization, summaryType);
                try {
                    sunatDocumentManager.sendToThirdParty(organization, summaryModel);
                    summaryModel.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
                    entrySet.getValue().stream().forEach(c -> c.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY));
                } catch (ModelInsuficientData e) {
                    log.error(e.getMessage());
                } catch (SendException e) {
                    log.error(e.getMessage());
                }
            }
        }

        return document.size();
    }

    private long sendFacturas(OpenfactSession session, OrganizationModel organization) {
        long readCount = 0;

        ScrollModel<List<DocumentModel>> scroll = session.documents()
                .createQuery(organization)
                .filterText("F", DocumentModel.DOCUMENT_ID)
                .documentType(DocumentType.INVOICE.toString())
                .requiredAction(RequiredAction.SEND_TO_TRIRD_PARTY)
                .getScrollResultList(1000);

        Iterator<List<DocumentModel>> iterator = scroll.iterator();

        while (iterator.hasNext()) {
            List<DocumentModel> documents = iterator.next();

            readCount += documents.size();

            documents.stream()
                    .filter(p -> p.getRequiredActions().contains(RequiredAction.SEND_TO_TRIRD_PARTY.toString()))
                    .filter(p -> p.getSendEvents().size() < RETRY).forEach(c -> {
                SunatDocumentManager manager = new SunatDocumentManager(session);
                try {
                    manager.sendToThirdParty(organization, c);
                    c.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
                } catch (ModelInsuficientData e) {
                    throw new JobException("Insufient data to send", e);
                } catch (SendException e) {
                    throw new JobException("error on execute job", e);
                }
            });
        }

        return readCount;
    }

}
