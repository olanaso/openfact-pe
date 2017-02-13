package org.openfact.pe.services.scheduled;

import org.jboss.logging.Logger;
import org.openfact.models.*;
import org.openfact.models.enums.DocumentType;
import org.openfact.models.enums.RequiredAction;
import org.openfact.models.enums.SendEventStatus;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SendToThridPartySunatScheduleTaskProvider implements OrganizationScheduleTaskProvider {

    public static final Logger log = Logger.getLogger(SendToThridPartySunatScheduleTaskProvider.class);

    public static final String JOB_NAME = "SENT_TO_THRID_PARTY_SUNAT";

    private int maxRetries = 20;

    protected boolean isActive;

    public SendToThridPartySunatScheduleTaskProvider(boolean isActive, int retries) {
        this.isActive = isActive;
        this.maxRetries = retries;
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

        readCount += sendInvoicesFactura(session, session.organizations().getOrganization(organization.getId()));

        readCount += sendCreditNotes(session, session.organizations().getOrganization(organization.getId()));
        readCount += sendDebitNotes(session, session.organizations().getOrganization(organization.getId()));

        readCount += sendPerceptions(session, session.organizations().getOrganization(organization.getId()));
        readCount += sendRetentions(session, session.organizations().getOrganization(organization.getId()));

        readCount += sendSummaryDocument(session, session.organizations().getOrganization(organization.getId()));

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
                .documentType(DocumentType.INVOICE)
                .enabled(true)
                .requiredAction(RequiredAction.SEND_TO_TRIRD_PARTY)
                .entityQuery()
                .orderByAsc(DocumentModel.DOCUMENT_ID)
                .resultList()
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

    private long sendInvoicesFactura(OpenfactSession session, OrganizationModel organization) {
        List<DocumentModel> documents = session.documents().createQuery(organization)
                .filterTextReplaceAsterisk("F*", DocumentModel.DOCUMENT_ID)
                .documentType(DocumentType.INVOICE).requiredAction(RequiredAction.SEND_TO_TRIRD_PARTY)
                .thirdPartySendEventFailures(maxRetries, false).enabled(true)
                .entityQuery().resultList().getResultList();

        documents.stream().forEach(document -> sendToThirdParty(session, organization, document));
        return documents.size();
    }

    private long sendCreditNotes(OpenfactSession session, OrganizationModel organization) {
        long readCount = 0;
        readCount += processDocument(session, organization, DocumentType.CREDIT_NOTE.toString());
        return readCount;
    }

    private long sendDebitNotes(OpenfactSession session, OrganizationModel organization) {
        long readCount = 0;
        readCount += processDocument(session, organization, DocumentType.DEBIT_NOTE.toString());
        return readCount;
    }

    private long sendPerceptions(OpenfactSession session, OrganizationModel organization) {
        long readCount = 0;
        readCount += processDocument(session, organization, SunatDocumentType.PERCEPTION.toString());
        return readCount;
    }

    private long sendRetentions(OpenfactSession session, OrganizationModel organization) {
        long readCount = 0;
        readCount += processDocument(session, organization, SunatDocumentType.RETENTION.toString());
        return readCount;
    }

    private long processDocument(OpenfactSession session, OrganizationModel organization, String documentType) {
        List<DocumentModel> documents = session.documents().createQuery(organization)
                .documentType(documentType).requiredAction(RequiredAction.SEND_TO_TRIRD_PARTY)
                .thirdPartySendEventFailures(maxRetries, false).enabled(true)
                .entityQuery().resultList().getResultList();

        documents.stream().forEach(document -> sendToThirdParty(session, organization, document));
        return documents.size();
    }

    private void sendToThirdParty(OpenfactSession session, OrganizationModel organization, DocumentModel document) {
        SunatDocumentManager sunatManager = new SunatDocumentManager(session);
        SendEventModel sendEvent = null;
        try {
            sendEvent = sunatManager.sendToThirdParty(organization, document);
            document.incrementThirdPartySendEventFailures();
            document.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
        } catch (ModelInsuficientData e) {
            sendEvent.setResult(SendEventStatus.ERROR);
            throw new JobException("Insufient data to send", e);
        } catch (SendException e) {
            sendEvent.setResult(SendEventStatus.ERROR);
            throw new JobException("error on execute job", e);
        }
    }

}
