package org.openfact.pe.services.util;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.openfact.common.util.ObjectUtil;
import org.openfact.models.CreditNoteModel;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.FileModel;
import org.openfact.models.InvoiceModel;
import org.openfact.models.OrganizationModel;
import org.openfact.models.SimpleFileModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.services.constants.SunatEventType;
import org.openfact.ubl.SendException;

import jodd.io.ZipBuilder;

public class SunatTemplateUtils {

	public static byte[] generateZip(byte[] document, String fileName) throws TransformerException, IOException {
		return ZipBuilder.createZipInMemory()/* .addFolder("dummy/") */.add(document).path(fileName + ".xml").save()
				.toBytes();
	}

	public static String generateXmlFileName(OrganizationModel organization, InvoiceModel invoice)
			throws SendException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new SendException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo;
		if (invoice.getInvoiceTypeCode().equals(CodigoTipoDocumento.FACTURA.getCodigo())) {
			codigo = CodigoTipoDocumento.FACTURA.getCodigo();
		} else if (invoice.getInvoiceTypeCode().equals(CodigoTipoDocumento.BOLETA.getCodigo())) {
			codigo = CodigoTipoDocumento.BOLETA.getCodigo();
		} else {
			throw new SendException("Invalid invoice code", new Throwable());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(invoice.getDocumentId());
		return sb.toString();
	}

	public static String generateXmlFileName(OrganizationModel organization, CreditNoteModel creditNote)
			throws SendException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new SendException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo = CodigoTipoDocumento.NOTA_CREDITO.getCodigo();
		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(creditNote.getDocumentId());
		return sb.toString();
	}

	public static String generateXmlFileName(OrganizationModel organization, DebitNoteModel debitNote)
			throws SendException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new SendException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo = CodigoTipoDocumento.NOTA_DEBITO.getCodigo();
		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(debitNote.getDocumentId());
		return sb.toString();
	}

	public static String generateXmlFileName(OrganizationModel organization, VoidedDocumentModel voidedDocument)
			throws SendException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new SendException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(voidedDocument.getDocumentId());
		return sb.toString();
	}

	public static String generateXmlFileName(OrganizationModel organization, SummaryDocumentModel summaryDocument)
			throws SendException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new SendException("Organization doesn't have assignedIdentificationId", new Throwable());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(summaryDocument.getDocumentId());
		return sb.toString();
	}

	public static String generateXmlFileName(OrganizationModel organization, RetentionModel retention)
			throws SendException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new SendException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo = CodigoTipoDocumento.RETENCION.getCodigo();
		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(retention.getDocumentId());
		return sb.toString();
	}

	public static String generateXmlFileName(OrganizationModel organization, PerceptionModel perception)
			throws SendException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new SendException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo = CodigoTipoDocumento.PERCEPCION.getCodigo();
		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(perception.getDocumentId());
		return sb.toString();
	}

	public static FileModel toFileModel(InternetMediaType mediaType, String fileName, byte[] file) {
		FileModel model = new SimpleFileModel();
		model.setFile(file);
		model.setFileName(fileName + mediaType.getExtension());
		model.setMimeType(mediaType.getMimeType());
		return model;
	}

	public static String getOrganizationName(OrganizationModel organization) {
		if (organization.getDisplayName() != null) {
			return organization.getDisplayName();
		} else {
			return ObjectUtil.capitalize(organization.getName());
		}
	}

	public static String toCamelCase(SunatEventType event) {
		StringBuilder sb = new StringBuilder("event");
		for (String s : event.name().toString().toLowerCase().split("_")) {
			sb.append(ObjectUtil.capitalize(s));
		}
		return sb.toString();
	}
}
