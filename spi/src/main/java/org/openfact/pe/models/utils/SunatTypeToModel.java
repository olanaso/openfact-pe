package org.openfact.pe.models.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openfact.models.ContactModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyLegalEntityModel;
import org.openfact.models.PartyModel;
import org.openfact.models.SupplierPartyModel;
import org.openfact.pe.model.types.PerceptionType;
import org.openfact.pe.model.types.RetentionType;
import org.openfact.pe.model.types.SUNATPerceptionDocumentReferenceType;
import org.openfact.pe.model.types.SUNATPerceptionInformationType;
import org.openfact.pe.model.types.SUNATRetentionDocumentReferenceType;
import org.openfact.pe.model.types.SUNATRetentionInformationType;
import org.openfact.pe.model.types.SummaryDocumentsType;
import org.openfact.pe.model.types.VoidedDocumentsType;
import org.openfact.pe.models.PerceptionDocumentReferenceModel;
import org.openfact.pe.models.PerceptionInformationModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionDocumentReferenceModel;
import org.openfact.pe.models.RetentionInformationModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ContactType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AdditionalAccountIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.NoteType;

public class SunatTypeToModel {

	public static LocalDate toDate(XMLGregorianCalendar xmlCal) {
		Date utilDate = xmlCal.toGregorianCalendar().getTime();
		return LocalDateTime.ofInstant(utilDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
	}

	public static void importPerception(OpenfactSession session, OrganizationModel organization, PerceptionModel model,
			PerceptionType type) {
		if (type.getId() != null && type.getId().getValue() != null) {
			model.setDocumentId(type.getId().getValue());
		}
		if (type.getUblVersionID() != null && type.getUblVersionID().getValue() != null) {
			model.setUblVersionID(type.getUblVersionID().getValue());
		}
		if (type.getCustomizationID() != null && type.getCustomizationID().getValue() != null) {
			model.setCustomizationID(type.getCustomizationID().getValue());
		}
		if (type.getDocumentCurrencyCode() != null && type.getDocumentCurrencyCode().getValue() != null) {
			model.setDocumentCurrencyCode(type.getDocumentCurrencyCode().getValue());
		}
		if (type.getIssueDate() != null && type.getIssueDate().getValue() != null) {
			model.setIssueDate(toDate(type.getIssueDate().getValue()));
		}
		if (type.getSunatPerceptionSystemCode() != null && type.getSunatPerceptionSystemCode().getValue() != null) {
			model.setSUNATPerceptionSystemCode(type.getSunatPerceptionSystemCode().getValue());
		}
		if (type.getSunatPerceptionPercent() != null && type.getSunatPerceptionPercent().getValue() != null) {
			model.setSUNATPerceptionPercent(type.getSunatPerceptionPercent().getValue());
		}
		if (type.getTotalInvoiceAmount() != null && type.getTotalInvoiceAmount().getValue() != null) {
			model.setTotalInvoiceAmount(type.getTotalInvoiceAmount().getValue());
		}
		if (type.getSunatTotalCashed() != null && type.getSunatTotalCashed().getValue() != null) {
			model.setSUNATTotalCashed(type.getSunatTotalCashed().getValue());
		}
		if (type.getAgentParty() != null) {
			updateModel(model.getAgentPartyAsNotNull(), type.getAgentParty());
		}
		if (type.getReceiverParty() != null) {
			updateModel(model.getReceiverPartyAsNotNull(), type.getReceiverParty());
		}
		if (type.getNote() != null && !type.getNote().isEmpty()) {
			for (NoteType item : type.getNote()) {
				model.getNotes().add(item.getValue());
			}
		}
		if (type.getSunatPerceptionDocumentReference() != null
				&& !type.getSunatPerceptionDocumentReference().isEmpty()) {
			for (SUNATPerceptionDocumentReferenceType item : type.getSunatPerceptionDocumentReference()) {
				updateModel(model.addSunatPerceptionDocumentReference(), item);
			}
		}
	}

	private static void updateModel(PerceptionDocumentReferenceModel model, SUNATPerceptionDocumentReferenceType type) {
		if (type.getId() != null && type.getId().getValue() != null) {
			model.setDocumentId(type.getId().getValue());
		}
		if (type.getIssueDate() != null && type.getIssueDate().getValue() != null) {
			model.setIssueDate(toDate(type.getIssueDate().getValue()));
		}
		if (type.getTotalInvoiceAmount() != null && type.getTotalInvoiceAmount().getValue() != null) {
			model.setTotalInvoiceAmount(type.getTotalInvoiceAmount().getValue());
		}
		if (type.getSunatPerceptionInformation() != null) {
			updateModel(model.getSunatPerceptionInformationAsNotNull(), type.getSunatPerceptionInformation());
		}
	}

	private static void updateModel(PerceptionInformationModel model, SUNATPerceptionInformationType type) {
		if (type.getSunatPerceptionAmount() != null && type.getSunatPerceptionAmount().getValue() != null) {
			model.setSunatPerceptionAmount(type.getSunatPerceptionAmount().getValue());
		}
		if (type.getSunatNetTotalCashed() != null && type.getSunatNetTotalCashed().getValue() != null) {
			model.setSunatNetTotalCashed(type.getSunatNetTotalCashed().getValue());
		}
		if (type.getSunatPerceptionDate() != null && type.getSunatPerceptionDate().getValue() != null) {
			model.setSunatPerceptionDate(toDate(type.getSunatPerceptionDate().getValue()));
		}
	}

	private static void updateModel(PartyModel model, PartyType type) {
		if (type.getContact() != null) {
			updateModel(model.getContactAsNotNull(), type.getContact());
		}
		if (type.getPartyLegalEntity() != null && !type.getPartyLegalEntity().isEmpty()) {
			for (PartyLegalEntityType item : type.getPartyLegalEntity()) {
				updateModel(model.addPartyLegalEntity(), item);
			}
		}
	}

	private static void updateModel(PartyLegalEntityModel model, PartyLegalEntityType type) {
		if (type.getRegistrationName() != null && type.getRegistrationName().getValue() != null) {
			model.setRegistrationName(type.getRegistrationName().getValue());
		}
		if (type.getCompanyID() != null && type.getCompanyID().getValue() != null) {
			model.setCompanyID(type.getCompanyID().getValue());
		}
	}

	private static void updateModel(ContactModel model, ContactType type) {
		if (type.getElectronicMail() != null && type.getElectronicMailValue() != null) {
			model.setElectronicMail(type.getElectronicMailValue());
		}
	}

	public static void importRetention(OpenfactSession session, OrganizationModel organization, RetentionModel model,
			RetentionType type) {
		if (type.getId() != null && type.getId().getValue() != null) {
			model.setDocumentId(type.getId().getValue());
		}
		if (type.getUblVersionID() != null && type.getUblVersionID().getValue() != null) {
			model.setUblVersionId(type.getUblVersionID().getValue());
		}
		if (type.getCustomizationID() != null && type.getCustomizationID().getValue() != null) {
			model.setCustomizationId(type.getCustomizationID().getValue());
		}
		if (type.getDocumentCurrencyCode() != null && type.getDocumentCurrencyCode().getValue() != null) {
			model.setDocumentCurrencyCode(type.getDocumentCurrencyCode().getValue());
		}
		if (type.getIssueDate() != null && type.getIssueDate().getValue() != null) {
			model.setIssueDate(toDate(type.getIssueDate().getValue()));
		}
		if (type.getSunatRetentionSystemCode() != null && type.getSunatRetentionSystemCode().getValue() != null) {
			model.setSunatRetentionSystemCode(type.getSunatRetentionSystemCode().getValue());
		}
		if (type.getSunatRetentionPercent() != null && type.getSunatRetentionPercent().getValue() != null) {
			model.setSunatRetentionPercent(type.getSunatRetentionPercent().getValue());
		}
		if (type.getTotalInvoiceAmount() != null && type.getTotalInvoiceAmount().getValue() != null) {
			model.setTotalInvoiceAmount(type.getTotalInvoiceAmount().getValue());
		}
		if (type.getSunatTotalPaid() != null && type.getSunatTotalPaid().getValue() != null) {
			model.setSunatTotalPaid(type.getSunatTotalPaid().getValue());
		}
		if (type.getAgentParty() != null) {
			updateModel(model.getAgentPartyAsNotNull(), type.getAgentParty());
		}
		if (type.getReceiverParty() != null) {
			updateModel(model.getReceiverPartyAsNotNull(), type.getReceiverParty());
		}
		if (type.getNote() != null && !type.getNote().isEmpty()) {
			for (NoteType item : type.getNote()) {
				model.getNotes().add(item.getValue());
			}
		}
		if (type.getSunatRetentionDocumentReference() != null && !type.getSunatRetentionDocumentReference().isEmpty()) {
			for (SUNATRetentionDocumentReferenceType item : type.getSunatRetentionDocumentReference()) {
				updateModel(model.addSunatRetentionDocumentReference(), item);
			}
		}
	}

	private static void updateModel(RetentionDocumentReferenceModel model, SUNATRetentionDocumentReferenceType type) {
		if (type.getID() != null && type.getID().getValue() != null) {
			model.setDocumentId(type.getID().getValue());
		}
		if (type.getIssueDate() != null && type.getIssueDate().getValue() != null) {
			model.setIssueDate(toDate(type.getIssueDate().getValue()));
		}
		if (type.getTotalInvoiceAmount() != null && type.getTotalInvoiceAmount().getValue() != null) {
			model.setTotalInvoiceAmount(type.getTotalInvoiceAmount().getValue());
		}
		if (type.getSUNATRetentionInformation() != null) {
			updateModel(model.getSunatRetentionInformation(), type.getSUNATRetentionInformation());
		}
	}

	private static void updateModel(RetentionInformationModel model, SUNATRetentionInformationType type) {
		if (type.getSUNATRetentionAmount() != null && type.getSUNATRetentionAmount().getValue() != null) {
			model.setSunatRetentionAmount(type.getSUNATRetentionAmount().getValue());
		}
		if (type.getSUNATNetTotalPaid() != null && type.getSUNATNetTotalPaid().getValue() != null) {
			model.setSunatNetTotalPaid(type.getSUNATNetTotalPaid().getValue());
		}
		if (type.getSUNATRetentionDate() != null && type.getSUNATRetentionDate().getValue() != null) {
			model.setSunatRetentionDate(toDate(type.getSUNATRetentionDate().getValue()));
		}
	}

	public static void importSummaryDocument(OpenfactSession session, OrganizationModel organization,
			SummaryDocumentModel model, SummaryDocumentsType type) {
		if (type.getId() != null && type.getId().getValue() != null) {
			model.setDocumentId(type.getId().getValue());
		}
		if (type.getDocumentCurrencyCode() != null && type.getDocumentCurrencyCode().getValue() != null) {
			model.setDocumentCurrencyCode(type.getDocumentCurrencyCode().getValue());
		}
		if (type.getUblVersionID() != null && type.getUblVersionID().getValue() != null) {
			model.setUblVersionId(type.getUblVersionID().getValue());
		}
		if (type.getCustomizationID() != null && type.getCustomizationID().getValue() != null) {
			model.setCustomizationId(type.getCustomizationID().getValue());
		}
		if (type.getReferenceDateTime() != null && type.getReferenceDateTime().getValue() != null) {
			model.setReferenceDate(toDate(type.getReferenceDateTime().getValue()));
		}
		if (type.getIssueDate() != null && type.getIssueDate().getValue() != null) {
			model.setIssueDate(toDate(type.getIssueDate().getValue()));
		}
		if (type.getAccountingSupplierParty() != null) {
			updateModel(model.getAccountingSupplierPartyAsNotNull(), type.getAccountingSupplierParty());
		}
	}

	private static void updateModel(SupplierPartyModel model, SupplierPartyType type) {
		if (type.getCustomerAssignedAccountID() != null && type.getCustomerAssignedAccountIDValue() != null) {
			model.setCustomerAssignedAccountID(type.getCustomerAssignedAccountIDValue());
		}
		if (type.getAdditionalAccountID() != null && !type.getAdditionalAccountID().isEmpty()) {
			for (AdditionalAccountIDType item : type.getAdditionalAccountID()) {
				model.getAdditionalAccountID().add(item.getValue());
			}
		}
		if (type.getParty() != null) {
			updateModel(model.getPartyAsNotNull(), type.getParty());
		}
	}

	public static void importVoidedDocument(OpenfactSession session, OrganizationModel organization,
			VoidedDocumentModel model, VoidedDocumentsType type) {
		if (type.getID() != null && type.getID().getValue() != null) {
			model.setDocumentId(type.getID().getValue());
		}
		if (type.getDocumentCurrencyCode() != null && type.getDocumentCurrencyCode().getValue() != null) {
			model.setDocumentCurrencyCode(type.getDocumentCurrencyCode().getValue());
		}
		if (type.getUBLVersionID() != null && type.getUBLVersionID().getValue() != null) {
			model.setUblVersionId(type.getUBLVersionID().getValue());
		}
		if (type.getCustomizationID() != null && type.getCustomizationID().getValue() != null) {
			model.setCustomizationId(type.getCustomizationID().getValue());
		}
		if (type.getReferenceDate() != null && type.getReferenceDate().getValue() != null) {
			model.setReferenceDate(toDate(type.getReferenceDate().getValue()));
		}
		if (type.getIssueDate() != null && type.getIssueDate().getValue() != null) {
			model.setIssueDate(toDate(type.getIssueDate().getValue()));
		}
		if (type.getAccountingSupplierParty() != null) {
			updateModel(model.getAccountingSupplierPartyAsNotNull(), type.getAccountingSupplierParty());
		}
		if (type.getNote() != null && !type.getNote().isEmpty()) {
			for (NoteType item : type.getNote()) {
				model.getNotes().add(item.getValue());
			}
		}
	}
}
