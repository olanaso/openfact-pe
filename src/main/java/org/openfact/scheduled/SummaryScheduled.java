package org.openfact.scheduled;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import org.openfact.models.*;
import org.openfact.models.types.DocumentRequiredAction;
import org.openfact.models.types.DocumentType;
import org.openfact.models.types.SendEventStatus;
import org.openfact.models.utils.TypeToModel;
import org.openfact.pe.models.utils.SunatRepresentationToType;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.representations.idm.BillingPaymentRepresentation;
import org.openfact.pe.representations.idm.SummaryLineRepresentation;
import org.openfact.pe.representations.idm.SummaryRepresentation;
import org.openfact.pe.representations.idm.TaxTotalRepresentation;
import org.openfact.pe.ubl.types.*;
import org.openfact.pe.ubl.ubl21.summary.SummaryDocumentsType;
import org.openfact.services.managers.DocumentManager;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.utils.UBLUtil;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class SummaryScheduled implements OrganizationScheduledTask {

    public static final String JOB_NAME = "SENT_TO_THRID_PARTY_SUNAT";

    private int maxRetries = 20;

    @Inject
    private DocumentProvider documentProvider;

    @Inject
    private SunatRepresentationToType sunatRepresentationToType;

    @Inject
    private DocumentManager documentManager;

    @Inject
    private UBLUtil ublUtil;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "Sunat Scheduled tasks";
    }

    @Override
    public String getDescription() {
        return "Use to send summary documents";
    }

    @Override
    public void executeTask(OrganizationModel organizationModel) {
        // Start Job
        long startTime = System.currentTimeMillis();
        long readCount = 0;

        readCount += sendSummaryDocument(organizationModel);

        readCount += sendInvoicesFactura(organizationModel);

        readCount += sendCreditNotes(organizationModel);
        readCount += sendDebitNotes(organizationModel);

        readCount += sendPerceptions(organizationModel);
        readCount += sendRetentions(organizationModel);

        readCount += sendVoidedDocuments(organizationModel);

        long endTime = System.currentTimeMillis();

//        // Save Report
//        JobReportModel jobReportModel = session.jobReports().createJobReport(organization, JOB_NAME);
//
//        // Metrics
//        jobReportModel.setStartTime(startTime);
//        jobReportModel.setEndTime(endTime);
//        jobReportModel.setDuration(endTime - startTime);
//        jobReportModel.setReadCount(readCount);
    }

    private long sendSummaryDocument(OrganizationModel organization) {
        List<DocumentModel> boletas = documentProvider.createQuery(organization)
                .filterTextReplaceAsterisk("B*", DocumentModel.DOCUMENT_ID)
                .documentType(DocumentType.INVOICE).requiredAction(DocumentRequiredAction.SEND_TO_THIRD_PARTY)
                .thirdPartySendEventFailures(maxRetries, false).enabled(true)
                .entityQuery().orderByAsc(DocumentModel.DOCUMENT_ID).resultList().getResultList();

        List<DocumentModel> notasDeCredito = documentProvider.createQuery(organization)
                .filterTextReplaceAsterisk("B*", DocumentModel.DOCUMENT_ID)
                .documentType(DocumentType.CREDIT_NOTE).requiredAction(DocumentRequiredAction.SEND_TO_THIRD_PARTY)
                .thirdPartySendEventFailures(maxRetries, false).enabled(true)
                .entityQuery().orderByAsc(DocumentModel.DOCUMENT_ID).resultList().getResultList();

        List<DocumentModel> notasDeDebito = documentProvider.createQuery(organization)
                .filterTextReplaceAsterisk("B*", DocumentModel.DOCUMENT_ID)
                .documentType(DocumentType.DEBIT_NOTE).requiredAction(DocumentRequiredAction.SEND_TO_THIRD_PARTY)
                .thirdPartySendEventFailures(maxRetries, false).enabled(true)
                .entityQuery().orderByAsc(DocumentModel.DOCUMENT_ID).resultList().getResultList();

        Stream<DocumentModel> documents = Stream.of(boletas, notasDeCredito, notasDeDebito).flatMap(Collection::stream);

        buildAndSendSummary(organization, documents);
        return boletas.size() + notasDeCredito.size() + notasDeDebito.size();
    }

    private void buildAndSendSummary(OrganizationModel organization, Stream<DocumentModel> documents) {
        Map<LocalDate, List<DocumentModel>> documentsGroupedByDates = documents.collect(Collectors.groupingBy(f -> f.getCreatedTimestamp().toLocalDate()));
        for (Map.Entry<LocalDate, List<DocumentModel>> entrySet : documentsGroupedByDates.entrySet()) {
            Map<String, List<DocumentModel>> documentsGroupedBySerie = entrySet.getValue().stream().collect(Collectors.groupingBy(f -> f.getDocumentId().split("-")[0]));

            SummaryRepresentation summaryRep = new SummaryRepresentation();
            summaryRep.setFechaDeEmision(LocalDateTime.now());
            summaryRep.setFechaDeReferencia(entrySet.getKey().atStartOfDay());

            List<SummaryLineRepresentation> lines = new ArrayList<>();

            for (Map.Entry<String, List<DocumentModel>> entrySetBySerie : documentsGroupedBySerie.entrySet()) {
                String serie = entrySetBySerie.getKey();
                List<DocumentModel> allDocuments = entrySetBySerie.getValue();
                Map<DocumentType, List<DocumentModel>> documentsGroupedByType = allDocuments.stream()
                        .sorted(Comparator.comparing(DocumentModel::getDocumentType))
                        .collect(Collectors.groupingBy(f -> DocumentType.valueOf(f.getDocumentType())));


                for (Map.Entry<DocumentType, List<DocumentModel>> entry : documentsGroupedByType.entrySet()) {
                    SummaryLineRepresentation line = buildSummaryDocumentLine(serie, entry.getValue(), entry.getKey());
                    lines.add(line);
                }
                summaryRep.setLine(lines);
            }


            // Create document
            DocumentModel document;
            try {
                SummaryDocumentsType summaryType = sunatRepresentationToType.toSummaryDocumentType(organization, summaryRep);
                UBLIDGenerator<SummaryDocumentsType> ublIDGenerator = ublUtil.getIDGenerator(SunatDocumentType.SUMMARY_DOCUMENTS.toString());
                String newDocumentId = ublIDGenerator.generateID(organization, summaryType);
                summaryType.setId(new IDType(newDocumentId));

                document = documentManager.addDocument(organization, summaryType.getId().getValue(), SunatDocumentType.SUMMARY_DOCUMENTS.toString(), summaryType);
            } catch (ModelException e) {
                e.printStackTrace();
                throw new EJBException("Error creating summary documents");
            }


            // Send document
            SendEventModel sendEvent = null;
            try {
                sendEvent = documentManager.sendToThirdParty(organization, document);
                document.removeRequiredAction(DocumentRequiredAction.SEND_TO_THIRD_PARTY);
                entrySet.getValue().stream().forEach(c -> c.removeRequiredAction(DocumentRequiredAction.SEND_TO_THIRD_PARTY));
            } catch (ModelInsuficientData e) {
                if (sendEvent != null) {
                    sendEvent.setResult(SendEventStatus.ERROR);
                }
                throw new JobException("Insufient data to send", e);
            } catch (SendEventException e) {
                if (sendEvent != null) {
                    sendEvent.setResult(SendEventStatus.ERROR);
                }
                throw new JobException("error on execute job", e);
            }

            entrySet.getValue().forEach(c -> document.addAttachedDocument(c));
        }
    }

    private SummaryLineRepresentation buildSummaryDocumentLine(String serie, List<DocumentModel> allDocuments, DocumentType documentType) {
        String codigoDocumento = null;
        switch (documentType) {
            case INVOICE:
                codigoDocumento = TipoInvoice.BOLETA.getCodigo();
                break;
            case CREDIT_NOTE:
                codigoDocumento = TipoComprobante.NOTA_CREDITO.getCodigo();
                break;
            case DEBIT_NOTE:
                codigoDocumento = TipoComprobante.NOTA_DEBITO.getCodigo();
                break;
            default:
                throw new IllegalStateException("Invalid document to summary");
        }

        SummaryLineRepresentation summaryLineRepresentation = new SummaryLineRepresentation();

        // Boletas
        summaryLineRepresentation.setCodigoDocumento(codigoDocumento);
        summaryLineRepresentation.setSerieDocumento(serie);
        summaryLineRepresentation.setNumeroInicioDocumento(Integer.valueOf(allDocuments.get(0).getDocumentId().split("-")[1]).toString());
        summaryLineRepresentation.setNumeroFinDocumento(Integer.valueOf(allDocuments.get(allDocuments.size() - 1).getDocumentId().split("-")[1]).toString());
        summaryLineRepresentation.setMoneda(TipoMoneda.PEN.getCodigo());

        summaryLineRepresentation.setTotalAmount(allDocuments.stream()
                .map(f -> f.getFirstAttribute(TypeToModel.LEGAL_MONETARY_TOTAL_PAYABLE_AMOUNT))
                .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryLineRepresentation.setChargeAmount(allDocuments.stream()
                .map(f -> f.getFirstAttribute(TypeToModel.LEGAL_MONETARY_TOTAL_CHARGE_TOTAL_AMOUNT))
                .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryLineRepresentation.setChargeIndicator(true);

        // Totales
        BillingPaymentRepresentation totalOperacionesGravadas = new BillingPaymentRepresentation();
        totalOperacionesGravadas.setInstructionID(TipoValorVenta.GRAVADO.getCodigo());
        totalOperacionesGravadas.setPaidAmount(allDocuments.stream()
                .map(f -> f.getFirstAttribute(SunatTypeToModel.TOTAL_OPERACIONES_GRAVADAS))
                .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        BillingPaymentRepresentation totalOperacionesExoneradas = new BillingPaymentRepresentation();
        totalOperacionesExoneradas.setInstructionID(TipoValorVenta.EXONERADO.getCodigo());
        totalOperacionesExoneradas.setPaidAmount(allDocuments.stream()
                .map(f -> f.getFirstAttribute(SunatTypeToModel.TOTAL_OPERACIONES_EXONERADAS))
                .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        BillingPaymentRepresentation totalOperacionesInafectas = new BillingPaymentRepresentation();
        totalOperacionesInafectas.setInstructionID(TipoValorVenta.INAFECTO.getCodigo());
        totalOperacionesInafectas.setPaidAmount(allDocuments.stream()
                .map(f -> f.getFirstAttribute(SunatTypeToModel.TOTAL_OPERACIONES_INAFECTAS))
                .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        summaryLineRepresentation.setPayment(Arrays.asList(totalOperacionesGravadas, totalOperacionesExoneradas, totalOperacionesInafectas));

        //
        TaxTotalRepresentation igvTaxTotalRepresentation = new TaxTotalRepresentation();
        igvTaxTotalRepresentation.setTaxSchemeID(TipoTributo.IGV.getId());
        igvTaxTotalRepresentation.setTaxSchemeName(TipoTributo.IGV.toString());
        igvTaxTotalRepresentation.setTaxTypeCode(TipoTributo.IGV.getCodigo());
        igvTaxTotalRepresentation.setTaxAmount(allDocuments.stream()
                .map(f -> f.getFirstAttribute(TypeToModel.TAX_TOTAL_AMOUNT))
                .map(f -> f != null ? new BigDecimal(f) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        TaxTotalRepresentation iscTaxTotalRepresentation = new TaxTotalRepresentation();
        iscTaxTotalRepresentation.setTaxSchemeID(TipoTributo.ISC.getId());
        iscTaxTotalRepresentation.setTaxSchemeName(TipoTributo.ISC.toString());
        iscTaxTotalRepresentation.setTaxTypeCode(TipoTributo.ISC.getCodigo());
        iscTaxTotalRepresentation.setTaxAmount(BigDecimal.ZERO);

        summaryLineRepresentation.setTaxTotal(Arrays.asList(igvTaxTotalRepresentation, iscTaxTotalRepresentation));

        return summaryLineRepresentation;
    }

    private long sendInvoicesFactura(OrganizationModel organization) {
        List<DocumentModel> documents = documentProvider.createQuery(organization)
                .filterTextReplaceAsterisk("F*", DocumentModel.DOCUMENT_ID)
                .documentType(DocumentType.INVOICE).requiredAction(DocumentRequiredAction.SEND_TO_THIRD_PARTY)
                .thirdPartySendEventFailures(maxRetries, false).enabled(true)
                .entityQuery().resultList().getResultList();

        documents.stream().forEach(document -> sendToThirdParty(organization, document));
        return documents.size();
    }

    private long sendCreditNotes(OrganizationModel organization) {
        long readCount = 0;
        readCount += processDocument(organization, DocumentType.CREDIT_NOTE.toString());
        return readCount;
    }

    private long sendDebitNotes(OrganizationModel organization) {
        long readCount = 0;
        readCount += processDocument(organization, DocumentType.DEBIT_NOTE.toString());
        return readCount;
    }

    private long sendPerceptions(OrganizationModel organization) {
        long readCount = 0;
        readCount += processDocument(organization, SunatDocumentType.PERCEPTION.toString());
        return readCount;
    }

    private long sendRetentions(OrganizationModel organization) {
        long readCount = 0;
        readCount += processDocument(organization, SunatDocumentType.RETENTION.toString());
        return readCount;
    }

    private long sendVoidedDocuments(OrganizationModel organization) {
        long readCount = 0;
        readCount += processDocument(organization, SunatDocumentType.VOIDED_DOCUMENTS.toString());
        return readCount;
    }

    private long processDocument(OrganizationModel organization, String documentType) {
        List<DocumentModel> documents = documentProvider.createQuery(organization)
                .documentType(documentType).requiredAction(DocumentRequiredAction.SEND_TO_THIRD_PARTY)
                .thirdPartySendEventFailures(maxRetries, false).enabled(true)
                .entityQuery().resultList().getResultList();

        documents.stream().forEach(document -> sendToThirdParty(organization, document));
        return documents.size();
    }

    private void sendToThirdParty(OrganizationModel organization, DocumentModel document) {
        SendEventModel sendEvent = null;
        try {
            sendEvent = documentManager.sendToThirdParty(organization, document);
            document.incrementThirdPartySendEventFailures();
            document.removeRequiredAction(DocumentRequiredAction.SEND_TO_THIRD_PARTY);
        } catch (ModelInsuficientData e) {
            if (sendEvent != null) {
                sendEvent.setResult(SendEventStatus.ERROR);
            }
            throw new JobException("Insufient data to send", e);
        } catch (SendEventException e) {
            if (sendEvent != null) {
                sendEvent.setResult(SendEventStatus.ERROR);
            }
            throw new JobException("error on execute job", e);
        }
    }


}
