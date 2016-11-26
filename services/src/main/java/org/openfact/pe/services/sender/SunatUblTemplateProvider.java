package org.openfact.pe.services.sender;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPFault;
import javax.xml.transform.TransformerException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang.ArrayUtils;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.models.ubl.CreditNoteModel;
import org.openfact.models.ubl.DebitNoteModel;
import org.openfact.models.ubl.InvoiceModel;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.provider.UblSunatTemplateProvider;
import org.openfact.ubl.UblSenderException;

import jodd.io.ZipBuilder;

public class SunatUblTemplateProvider implements UblSunatTemplateProvider {

	private OpenfactSession session;

	public SunatUblTemplateProvider(OpenfactSession session) {
		this.session = session;
	}

	private SunatUblSenderProvider getUblSenderProvider(OrganizationModel organization) {
		return session.getProvider(SunatUblSenderProvider.class, organization.getDefaultLocale());
	}

	private SunatUblSenderResponseProvider getUblSenderResponseProvider(OrganizationModel organization) {
		return session.getProvider(SunatUblSenderResponseProvider.class, organization.getDefaultLocale());
	}

	@Override
	public SendEventModel sendInvoice(OrganizationModel organization, InvoiceModel invoice) throws UblSenderException {
		SendEventModel model = null;
		byte[] zip = null;
		try {
			String fileName = generateXmlFileName(organization, invoice);
			zip = generateZip(ArrayUtils.toPrimitive(invoice.getXmlDocument()), fileName);
			// sender
			Map<String, Object> response = getUblSenderProvider(organization).sendDocument(organization, zip, fileName,
					InternetMediaType.ZIP);
			// byte[] response = (byte[]) result.get("success");
			// sender response
			model = getUblSenderResponseProvider(organization).invoiceSenderResponse(organization, invoice, zip,
					response);
		} catch (TransformerException e) {
			throw new UblSenderException(e);
		} catch (IOException e) {
			throw new UblSenderException(e);
		} catch (SOAPFaultException e) {
			SOAPFault soapFault = e.getFault();
			model = getUblSenderResponseProvider(organization).invoiceSenderResponse(organization, invoice, zip, null,
					new String[] { soapFault.getFaultCode(), soapFault.getFaultString() });
		}
		return model;
	}

	@Override
	public SendEventModel sendInvoices(OrganizationModel organization, List<InvoiceModel> invoices)
			throws UblSenderException {
		throw new UblSenderException("method not implemented", new Throwable());
	}

	@Override
	public SendEventModel sendCreditNote(OrganizationModel organization, CreditNoteModel creditNote)
			throws UblSenderException {
		SendEventModel model = null;
		byte[] zip = null;
		try {
			String fileName = generateXmlFileName(organization, creditNote);
			zip = generateZip(ArrayUtils.toPrimitive(creditNote.getXmlDocument()), fileName);
			// sender
			Map<String, Object> response = getUblSenderProvider(organization).sendDocument(organization, zip, fileName,
					InternetMediaType.ZIP);
			// sender response
			model = getUblSenderResponseProvider(organization).creditNoteSenderResponse(organization, creditNote, zip,
					response);
		} catch (TransformerException e) {
			throw new UblSenderException(e);
		} catch (IOException e) {
			throw new UblSenderException(e);
		} catch (SOAPFaultException e) {
			SOAPFault soapFault = e.getFault();
			model = getUblSenderResponseProvider(organization).creditNoteSenderResponse(organization, creditNote, zip,
					null, new String[] { soapFault.getFaultCode(), soapFault.getFaultString() });
		}
		return model;
	}

	@Override
	public SendEventModel sendDebitNote(OrganizationModel organization, DebitNoteModel debitNote)
			throws UblSenderException {
		SendEventModel model = null;
		byte[] zip = null;
		try {
			String fileName = generateXmlFileName(organization, debitNote);
			zip = generateZip(ArrayUtils.toPrimitive(debitNote.getXmlDocument()), fileName);
			// sender
			Map<String, Object> response = getUblSenderProvider(organization).sendDocument(organization, zip, fileName,
					InternetMediaType.ZIP);
			// sender response
			model = getUblSenderResponseProvider(organization).debitNoteSenderResponse(organization, debitNote, zip,
					response);
		} catch (TransformerException e) {
			throw new UblSenderException(e);
		} catch (IOException e) {
			throw new UblSenderException(e);
		} catch (SOAPFaultException e) {
			SOAPFault soapFault = e.getFault();
			model = getUblSenderResponseProvider(organization).debitNoteSenderResponse(organization, debitNote, zip,
					null, new String[] { soapFault.getFaultCode(), soapFault.getFaultString() });
		}
		return model;
	}

	@Override
	public SendEventModel sendPerception(OrganizationModel organization, PerceptionModel perception)
			throws UblSenderException {
		SendEventModel model = null;
		byte[] zip = null;
		try {
			String fileName = generateXmlFileName(organization, perception);
			zip = generateZip(perception.getXmlDocument(), fileName);
			// sender
			Map<String, Object> result = getUblSenderProvider(organization).sendDocument(organization, zip, fileName,
					InternetMediaType.ZIP);
			byte[] response = null;
			if (result.containsKey("success")) {
				response = (byte[]) result.get("success");
			}
			// sender response
			model = getUblSenderResponseProvider(organization).perceptionSenderResponse(organization, perception, zip,
					response);
		} catch (TransformerException e) {
			throw new UblSenderException(e);
		} catch (IOException e) {
			throw new UblSenderException(e);
		} catch (SOAPFaultException e) {
			SOAPFault soapFault = e.getFault();
			model = getUblSenderResponseProvider(organization).perceptionSenderResponse(organization, perception, zip,
					null, new String[] { soapFault.getFaultCode(), soapFault.getFaultString() });
		}
		return model;
	}

	@Override
	public SendEventModel sendRetention(OrganizationModel organization, RetentionModel retention)
			throws UblSenderException {
		SendEventModel model = null;
		byte[] zip = null;
		try {
			String fileName = generateXmlFileName(organization, retention);
			zip = generateZip(retention.getXmlDocument(), fileName);
			// sender
			Map<String, Object> result = getUblSenderProvider(organization).sendDocument(organization, zip, fileName,
					InternetMediaType.ZIP);
			byte[] response = null;
			if (result.containsKey("success")) {
				response = (byte[]) result.get("success");
			}
			// sender response
			model = getUblSenderResponseProvider(organization).retentionSenderResponse(organization, retention, zip,
					response);
		} catch (TransformerException e) {
			throw new UblSenderException(e);
		} catch (IOException e) {
			throw new UblSenderException(e);
		} catch (SOAPFaultException e) {
			SOAPFault soapFault = e.getFault();
			model = getUblSenderResponseProvider(organization).retentionSenderResponse(organization, retention, zip,
					null, new String[] { soapFault.getFaultCode(), soapFault.getFaultString() });
		}
		return model;
	}

	@Override
	public SendEventModel sendSummaryDocument(OrganizationModel organization, SummaryDocumentModel summaryDocument)
			throws UblSenderException {
		SendEventModel model = null;
		byte[] zip = null;
		try {
			String fileName = generateXmlFileName(organization, summaryDocument);
			zip = generateZip(summaryDocument.getXmlDocument(), fileName);
			// sender
			String response = getUblSenderProvider(organization).sendSummary(organization, zip, fileName,
					InternetMediaType.ZIP);
			// sender response
			model = getUblSenderResponseProvider(organization).summaryDocumentSenderResponse(organization,
					summaryDocument, zip, response);
		} catch (TransformerException e) {
			throw new UblSenderException(e);
		} catch (IOException e) {
			throw new UblSenderException(e);
		} catch (SOAPFaultException e) {
			SOAPFault soapFault = e.getFault();
			model = getUblSenderResponseProvider(organization).summaryDocumentSenderResponse(organization,
					summaryDocument, zip, null, new String[] { soapFault.getFaultCode(), soapFault.getFaultString() });
		}
		return model;
	}

	@Override
	public SendEventModel sendVoidedDocument(OrganizationModel organization, VoidedDocumentModel voidedDocument)
			throws UblSenderException {
		SendEventModel model = null;
		byte[] zip = null;
		try {
			String fileName = generateXmlFileName(organization, voidedDocument);
			zip = generateZip(voidedDocument.getXmlDocument(), fileName);
			// sender
			String response = getUblSenderProvider(organization).sendSummary(organization, zip, fileName,
					InternetMediaType.ZIP);
			// sender response
			model = getUblSenderResponseProvider(organization).voidedDocumentSenderResponse(organization,
					voidedDocument, zip, response);
		} catch (TransformerException e) {
			throw new UblSenderException(e);
		} catch (IOException e) {
			throw new UblSenderException(e);
		} catch (SOAPFaultException e) {
			SOAPFault soapFault = e.getFault();
			model = getUblSenderResponseProvider(organization).voidedDocumentSenderResponse(organization,
					voidedDocument, zip, null, new String[] { soapFault.getFaultCode(), soapFault.getFaultString() });
		}
		return model;
	}

	@Override
	public void close() {
	}

	private byte[] generateZip(byte[] document, String fileName) throws TransformerException, IOException {
		return ZipBuilder.createZipInMemory()/* .addFolder("dummy/") */.add(document).path(fileName + ".xml").save()
				.toBytes();
	}

	private String generateXmlFileName(OrganizationModel organization, InvoiceModel invoice) throws UblSenderException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new UblSenderException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo;
		if (invoice.getInvoiceTypeCode().equals(CodigoTipoDocumento.FACTURA.getCodigo())) {
			codigo = CodigoTipoDocumento.FACTURA.getCodigo();
		} else if (invoice.getInvoiceTypeCode().equals(CodigoTipoDocumento.BOLETA.getCodigo())) {
			codigo = CodigoTipoDocumento.BOLETA.getCodigo();
		} else {
			throw new UblSenderException("Invalid invoice code", new Throwable());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(invoice.getID());
		return sb.toString();
	}

	private String generateXmlFileName(OrganizationModel organization, CreditNoteModel creditNote)
			throws UblSenderException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new UblSenderException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo = CodigoTipoDocumento.NOTA_CREDITO.getCodigo();
		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(creditNote.getID());
		return sb.toString();
	}

	private String generateXmlFileName(OrganizationModel organization, DebitNoteModel debitNote)
			throws UblSenderException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new UblSenderException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo = CodigoTipoDocumento.NOTA_DEBITO.getCodigo();
		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(debitNote.getID());
		return sb.toString();
	}

	private String generateXmlFileName(OrganizationModel organization, VoidedDocumentModel voidedDocument)
			throws UblSenderException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new UblSenderException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo = CodigoTipoDocumento.BAJA.getCodigo();
		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(voidedDocument.getDocumentId());
		return sb.toString();
	}

	private String generateXmlFileName(OrganizationModel organization, SummaryDocumentModel summaryDocument)
			throws UblSenderException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new UblSenderException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo = CodigoTipoDocumento.RESUMEN_DIARIO.getCodigo();
		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(summaryDocument.getDocumentId());
		return sb.toString();
	}

	private String generateXmlFileName(OrganizationModel organization, RetentionModel retention)
			throws UblSenderException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new UblSenderException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo = CodigoTipoDocumento.RETENCION.getCodigo();
		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(retention.getDocumentId());
		return sb.toString();
	}

	private String generateXmlFileName(OrganizationModel organization, PerceptionModel perception)
			throws UblSenderException {
		if (organization.getAssignedIdentificationId() == null) {
			throw new UblSenderException("Organization doesn't have assignedIdentificationId", new Throwable());
		}
		String codigo = CodigoTipoDocumento.PERCEPCION.getCodigo();
		StringBuilder sb = new StringBuilder();
		sb.append(organization.getAssignedIdentificationId()).append("-");
		sb.append(codigo).append("-");
		sb.append(perception.getDocumentId());
		return sb.toString();
	}
}
