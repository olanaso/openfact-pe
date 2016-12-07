package org.openfact.pe.models.utils;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionProvider;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentProvider;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.VoidedDocumentProvider;

public class SunatDocumentIdProvider {

	public static String generatePerceptionDocumentId(OpenfactSession session, OrganizationModel organization) {
		CodigoTipoDocumento perceptionCode = CodigoTipoDocumento.PERCEPCION;
		PerceptionModel lastPerception = null;
		ScrollModel<PerceptionModel> perceptions = session.getProvider(PerceptionProvider.class)
				.getPerceptionsScroll(organization, false, 4, 2);
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

		int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
		int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
		StringBuilder documentId = new StringBuilder();
		documentId.append(perceptionCode.getMask().substring(0, 1));
		documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
		documentId.append("-");
		documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

		return documentId.toString();
	}

	public static String generateRetentionDocumentId(OpenfactSession session, OrganizationModel organization) {
		CodigoTipoDocumento retentionCode = CodigoTipoDocumento.RETENCION;
		RetentionModel lastRetention = null;
		ScrollModel<RetentionModel> retentions = session.getProvider(RetentionProvider.class)
				.getRetentionsScroll(organization, false, 4, 2);
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

		int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
		int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
		StringBuilder documentId = new StringBuilder();
		documentId.append(retentionCode.getMask().substring(0, 1));
		documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
		documentId.append("-");
		documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

		return documentId.toString();
	}

	public static String generateSummaryDocumentDocumentId(OpenfactSession session, OrganizationModel organization) {
		CodigoTipoDocumento summaryDocumentCode = CodigoTipoDocumento.RESUMEN_DIARIO;
		SummaryDocumentModel lastSummaryDocument = null;
		ScrollModel<SummaryDocumentModel> summaryDocuments = session.getProvider(SummaryDocumentProvider.class)
				.getSummaryDocumentsScroll(organization, false, 4, 2);
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

		int number = 0;
		if (lastSummaryDocument != null) {
			String[] splits = lastSummaryDocument.getDocumentId().split("-");
			number = Integer.parseInt(splits[2]);
		}

		int nextNumber = SunatUtils.getNextNumber(number, 99999);
		int nextSeries = SunatUtils.getDateToNumber();
		StringBuilder documentId = new StringBuilder();
		documentId.append(summaryDocumentCode.getMask().substring(0, 2));
		documentId.append("-");
		documentId.append(nextSeries);
		documentId.append("-");
		documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 3, "0"));

		return documentId.toString();
	}

	public static String generateVoidedDocumentId(OpenfactSession session, OrganizationModel organization) {
		CodigoTipoDocumento voidedDocumentCode = CodigoTipoDocumento.BAJA;
		VoidedDocumentModel lastVoidedDocument = null;
		ScrollModel<VoidedDocumentModel> voidedDocuments = session.getProvider(VoidedDocumentProvider.class)
				.getVoidedDocumentsScroll(organization, false, 4, 2);
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

		int number = 0;
		if (lastVoidedDocument != null) {
			String[] splits = lastVoidedDocument.getDocumentId().split("-");
			number = Integer.parseInt(splits[2]);
		}

		int nextNumber = SunatUtils.getNextNumber(number, 99999);
		int nextSeries = SunatUtils.getDateToNumber();
		StringBuilder documentId = new StringBuilder();
		documentId.append(voidedDocumentCode.getMask().substring(0, 2));
		documentId.append("-");
		documentId.append(nextSeries);
		documentId.append("-");
		documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 3, "0"));

		return documentId.toString();
	}
}