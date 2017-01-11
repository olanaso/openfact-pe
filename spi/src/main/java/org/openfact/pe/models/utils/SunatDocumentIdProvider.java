package org.openfact.pe.models.utils;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.CreditNoteModel;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.InvoiceModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.pe.constants.TipoComprobante;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionProvider;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentProvider;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.VoidedDocumentProvider;

public class SunatDocumentIdProvider {

	public static String generateInvoiceDocumentId(OpenfactSession session, OrganizationModel organization, String invoiceType) {
		TipoComprobante invoiceCode;
		if (TipoComprobante.FACTURA.getCodigo().equals(invoiceType)) {
			invoiceCode = TipoComprobante.FACTURA;
		} else if (TipoComprobante.BOLETA.getCodigo().equals(invoiceType)) {
			invoiceCode = TipoComprobante.BOLETA;
		} else {
			throw new ModelException("Invalid invoiceTypeCode");
		}

		InvoiceModel lastInvoice = null;
		ScrollModel<InvoiceModel> invoices = session.invoices().getInvoicesScroll(organization, false, 10);
		Iterator<InvoiceModel> iterator = invoices.iterator();

		Pattern pattern = Pattern.compile(invoiceCode.getMask());
		while (iterator.hasNext()) {
			InvoiceModel invoice = iterator.next();
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
		documentId.append(invoiceCode.getMask().substring(2, 3));
		documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
		documentId.append("-");
		documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

		return documentId.toString();
	}

	public static String generateCreditNoteDocumentId(OpenfactSession session, OrganizationModel organization, String codeType) {
		TipoComprobante creditNoteCode = TipoComprobante.NOTA_CREDITO;

		CreditNoteModel lastCreditNote = null;
		ScrollModel<CreditNoteModel> creditNotes = session.creditNotes().getCreditNotesScroll(organization, false, 10);
		Iterator<CreditNoteModel> iterator = creditNotes.iterator();

		Pattern pattern = Pattern.compile(creditNoteCode.getMask());
		while (iterator.hasNext()) {
			CreditNoteModel creditNote = iterator.next();
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
		documentId.append(codeType);
		documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
		documentId.append("-");
		documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

		return documentId.toString();
	}

	public static String generateDebitNoteDocumentId(OpenfactSession session, OrganizationModel organization, String codeType) {
		TipoComprobante debitNoteCode = TipoComprobante.NOTA_DEBITO;

		DebitNoteModel lastDebitNote = null;
		ScrollModel<DebitNoteModel> debitNotes = session.debitNotes().getDebitNotesScroll(organization, false, 4);
		Iterator<DebitNoteModel> iterator = debitNotes.iterator();

		Pattern pattern = Pattern.compile(debitNoteCode.getMask());
		while (iterator.hasNext()) {
			DebitNoteModel debitNote = iterator.next();
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
		documentId.append(codeType);
		documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
		documentId.append("-");
		documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

		return documentId.toString();
	}

	public static String generatePerceptionDocumentId(OpenfactSession session, OrganizationModel organization) {
		TipoComprobante perceptionCode = TipoComprobante.PERCEPCION;
		PerceptionModel lastPerception = null;
		ScrollModel<PerceptionModel> perceptions = session.getProvider(PerceptionProvider.class)
				.getPerceptionsScroll(organization, false, 4);
		Iterator<PerceptionModel> iterator = perceptions.iterator();

		Pattern pattern = Pattern.compile(perceptionCode.getMask());
		while (iterator.hasNext()) {
			PerceptionModel perception = iterator.next();
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
		RetentionModel lastRetention = null;
		ScrollModel<RetentionModel> retentions = session.getProvider(RetentionProvider.class)
				.getRetentionsScroll(organization, false, 4);
		Iterator<RetentionModel> iterator = retentions.iterator();

		Pattern pattern = Pattern.compile(retentionCode.getMask());
		while (iterator.hasNext()) {
			RetentionModel retention = iterator.next();
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
		SummaryDocumentModel lastSummaryDocument = null;
		ScrollModel<SummaryDocumentModel> summaryDocuments = session.getProvider(SummaryDocumentProvider.class)
				.getSummaryDocumentsScroll(organization, false, 4);
		Iterator<SummaryDocumentModel> iterator = summaryDocuments.iterator();

		Pattern pattern = Pattern.compile(summaryDocumentCode.getMask());
		while (iterator.hasNext()) {
			SummaryDocumentModel summaryDocument = iterator.next();
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
		VoidedDocumentModel lastVoidedDocument = null;
		ScrollModel<VoidedDocumentModel> voidedDocuments = session.getProvider(VoidedDocumentProvider.class)
				.getVoidedDocumentsScroll(organization, false, 4);
		Iterator<VoidedDocumentModel> iterator = voidedDocuments.iterator();

		Pattern pattern = Pattern.compile(voidedDocumentCode.getMask());
		while (iterator.hasNext()) {
			VoidedDocumentModel voidedDocument = iterator.next();
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