package org.openfact.pe.services.ubl;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.*;
import org.openfact.models.enums.DocumentType;
import org.openfact.pe.models.enums.TipoComprobante;
import org.openfact.pe.models.enums.*;
import org.openfact.pe.models.utils.SunatMarshallerUtils;

public class SunatUBLIDGenerator {

    public static String generateInvoiceDocumentId(OpenfactSession session, OrganizationModel organization, InvoiceType invoiceType) {
        String invoiceTypeCode = invoiceType.getInvoiceTypeCodeValue();

        TipoInvoice tipoInvoice;
        if (TipoInvoice.FACTURA.getCodigo().equals(invoiceTypeCode)) {
            tipoInvoice = TipoInvoice.FACTURA;
        } else if (TipoInvoice.BOLETA.getCodigo().equals(invoiceTypeCode)) {
            tipoInvoice = TipoInvoice.BOLETA;
        } else {
            throw new ModelException("Invoice Type Code Invalido");
        }

        DocumentModel lastInvoice = null;
        ScrollModel<DocumentModel> invoices = session.documents()
                .createQuery(organization)
                .documentType(DocumentType.INVOICE.toString())
                .orderByDesc(DocumentModel.DOCUMENT_ID)
                .getScrollResult(10);

        Iterator<DocumentModel> iterator = invoices.iterator();

        Pattern pattern = Pattern.compile(tipoInvoice.getDocumentIdPattern());
        while (iterator.hasNext()) {
            DocumentModel invoice = iterator.next();
            String documentId = invoice.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastInvoice = invoice;
                break;
            }
        }

        int series = 0;
        int number = 0;
        if (lastInvoice != null) {
            String[] splits = lastInvoice.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = SunatMarshallerUtils.getNextNumber(number, 99_999_999);
        int nextSeries = SunatMarshallerUtils.getNextSerie(series, number, 999, 99_999_999);

        StringBuilder documentId = new StringBuilder();
        documentId.append(tipoInvoice.getDocumentIdPattern().substring(2, 3));
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }

    public static String generateCreditNoteDocumentId(OpenfactSession session, OrganizationModel organization, CreditNoteType creditNoteType) {
        String invoiceDocumentReferenceID = null;
        if (creditNoteType.getDiscrepancyResponseCount() > 0) {
            invoiceDocumentReferenceID = creditNoteType.getDiscrepancyResponse().get(0).getReferenceIDValue();
        } else if (creditNoteType.getBillingReferenceCount() > 0) {
            invoiceDocumentReferenceID = creditNoteType.getBillingReference().get(0).getInvoiceDocumentReference().getIDValue();
        }

        TipoComprobante tipoComprobante = TipoComprobante.NOTA_CREDITO;

        DocumentModel lastCreditNote = null;
        ScrollModel<DocumentModel> creditNotes = session.documents().getDocumentScroll(organization, DocumentType.CREDIT_NOTE.toString(), 10, false);
        Iterator<DocumentModel> iterator = creditNotes.iterator();

        Pattern pattern = Pattern.compile(tipoComprobante.getMask());
        while (iterator.hasNext()) {
            DocumentModel creditNote = iterator.next();
            String documentId = creditNote.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastCreditNote = creditNote;
                break;
            }
        }

        int series = 0;
        int number = 0;
        if (lastCreditNote != null) {
            String[] splits = lastCreditNote.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = SunatMarshallerUtils.getNextNumber(number, 99_999_999);
        int nextSeries = SunatMarshallerUtils.getNextSerie(series, number, 999, 99_999_999);

        StringBuilder documentId = new StringBuilder();
        documentId.append(invoiceDocumentReferenceID.substring(0, 1));
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }

    public static String generateDebitNoteDocumentId(OpenfactSession session, OrganizationModel organization, DebitNoteType debitNoteType) {
        String invoiceDocumentReferenceID = null;
        if (debitNoteType.getDiscrepancyResponseCount() > 0) {
            invoiceDocumentReferenceID = debitNoteType.getDiscrepancyResponse().get(0).getReferenceIDValue();
        } else if (debitNoteType.getBillingReferenceCount() > 0) {
            invoiceDocumentReferenceID = debitNoteType.getBillingReference().get(0).getInvoiceDocumentReference().getIDValue();
        }

        TipoComprobante tipoComprobante = TipoComprobante.NOTA_DEBITO;

        DocumentModel lastDebitNote = null;
        ScrollModel<DocumentModel> debitNotes = session.documents().getDocumentScroll(organization, DocumentType.DEBIT_NOTE.toString(), 10, false);
        Iterator<DocumentModel> iterator = debitNotes.iterator();

        Pattern pattern = Pattern.compile(tipoComprobante.getMask());
        while (iterator.hasNext()) {
            DocumentModel debitNote = iterator.next();
            String documentId = debitNote.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastDebitNote = debitNote;
                break;
            }
        }

        int series = 0;
        int number = 0;
        if (lastDebitNote != null) {
            String[] splits = lastDebitNote.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = SunatMarshallerUtils.getNextNumber(number, 99_999_999);
        int nextSeries = SunatMarshallerUtils.getNextSerie(series, number, 999, 99_999_999);

        StringBuilder documentId = new StringBuilder();
        documentId.append(invoiceDocumentReferenceID.substring(0, 1));
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }

    public static String generatePerceptionDocumentId(OpenfactSession session, OrganizationModel organization) {
        TipoComprobante perceptionCode = TipoComprobante.PERCEPCION;
        DocumentModel lastPerception = null;
        ScrollModel<DocumentModel> perceptions = session.documents().getDocumentScroll(organization, SunatDocumentType.PERCEPTION.toString(), 10, false);
        Iterator<DocumentModel> iterator = perceptions.iterator();

        Pattern pattern = Pattern.compile(perceptionCode.getMask());
        while (iterator.hasNext()) {
            DocumentModel perception = iterator.next();
            String documentId = perception.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastPerception = perception;
                break;
            }
        }

        int series = 0;
        int number = 0;
        if (lastPerception != null) {
            String[] splits = lastPerception.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = SunatMarshallerUtils.getNextNumber(number, 99_999_999);
        int nextSeries = SunatMarshallerUtils.getNextSerie(series, number, 999, 99_999_999);
        StringBuilder documentId = new StringBuilder();
        documentId.append(perceptionCode.getMask().substring(2, 3));
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }

    public static String generateRetentionDocumentId(OpenfactSession session, OrganizationModel organization) {
        TipoComprobante retentionCode = TipoComprobante.RETENCION;
        DocumentModel lastRetention = null;
        ScrollModel<DocumentModel> retentions = session.documents().getDocumentScroll(organization, SunatDocumentType.RETENTION.toString(), 10, false);
        Iterator<DocumentModel> iterator = retentions.iterator();

        Pattern pattern = Pattern.compile(retentionCode.getMask());
        while (iterator.hasNext()) {
            DocumentModel retention = iterator.next();
            String documentId = retention.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastRetention = retention;
                break;
            }
        }

        int series = 0;
        int number = 0;
        if (lastRetention != null) {
            String[] splits = lastRetention.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = SunatMarshallerUtils.getNextNumber(number, 99_999_999);
        int nextSeries = SunatMarshallerUtils.getNextSerie(series, number, 999, 99_999_999);
        StringBuilder documentId = new StringBuilder();
        documentId.append(retentionCode.getMask().substring(2, 3));
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }

    public static String generateSummaryDocumentDocumentId(OpenfactSession session, OrganizationModel organization) {
        TipoComprobante summaryDocumentCode = TipoComprobante.RESUMEN_DIARIO;
        DocumentModel lastSummaryDocument = null;
        ScrollModel<DocumentModel> summaryDocuments = session.documents().getDocumentScroll(organization, SunatDocumentType.SUMMARY.toString(), 10, false);
        Iterator<DocumentModel> iterator = summaryDocuments.iterator();

        Pattern pattern = Pattern.compile(summaryDocumentCode.getMask());
        while (iterator.hasNext()) {
            DocumentModel summaryDocument = iterator.next();
            String documentId = summaryDocument.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastSummaryDocument = summaryDocument;
                break;
            }
        }

        int number = 0, secuence = 0;
        if (lastSummaryDocument != null) {
            String[] splits = lastSummaryDocument.getDocumentId().split("-");
            secuence = Integer.parseInt(splits[1]);
            number = Integer.parseInt(splits[2]);
        }
        int nextSeries = SunatMarshallerUtils.getDateToNumber();
        int nextNumber = 0;
        if (secuence == nextSeries) {
            nextNumber = SunatMarshallerUtils.getNextNumber(number, 99999);
        } else {
            nextNumber = SunatMarshallerUtils.getNextNumber(0, 99999);
        }
        StringBuilder documentId = new StringBuilder();
        documentId.append(summaryDocumentCode.getMask().substring(2, 4));
        documentId.append("-");
        documentId.append(nextSeries);
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 5, "0"));

        return documentId.toString();
    }

    public static String generateVoidedDocumentId(OpenfactSession session, OrganizationModel organization) {
        TipoComprobante voidedDocumentCode = TipoComprobante.BAJA;
        DocumentModel lastVoidedDocument = null;
        ScrollModel<DocumentModel> voidedDocuments = session.documents().getDocumentScroll(organization, SunatDocumentType.VOIDED_DOCUMENTS.toString(), 10, false);
        Iterator<DocumentModel> iterator = voidedDocuments.iterator();

        Pattern pattern = Pattern.compile(voidedDocumentCode.getMask());
        while (iterator.hasNext()) {
            DocumentModel voidedDocument = iterator.next();
            String documentId = voidedDocument.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastVoidedDocument = voidedDocument;
                break;
            }
        }

        int number = 0, secuence = 0;
        if (lastVoidedDocument != null) {
            String[] splits = lastVoidedDocument.getDocumentId().split("-");
            secuence = Integer.parseInt(splits[1]);
            number = Integer.parseInt(splits[2]);
        }
        int nextSeries = SunatMarshallerUtils.getDateToNumber();
        int nextNumber = 0;
        if (secuence == nextSeries) {
            nextNumber = SunatMarshallerUtils.getNextNumber(number, 99999);
        } else {
            nextNumber = SunatMarshallerUtils.getNextNumber(0, 99999);
        }
        StringBuilder documentId = new StringBuilder();
        documentId.append(voidedDocumentCode.getMask().substring(2, 4));
        documentId.append("-");
        documentId.append(nextSeries);
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 5, "0"));

        return documentId.toString();
    }

}