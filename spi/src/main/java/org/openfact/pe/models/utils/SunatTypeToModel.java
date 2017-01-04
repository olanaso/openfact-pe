package org.openfact.pe.models.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.*;
import org.openfact.models.ContactModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyLegalEntityModel;
import org.openfact.models.PartyModel;
import org.openfact.models.SupplierPartyModel;
import org.openfact.pe.models.*;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AdditionalAccountIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.NoteType;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.types.perception.SUNATPerceptionDocumentReferenceType;
import org.openfact.pe.models.types.perception.SUNATPerceptionInformationType;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.pe.models.types.retention.SUNATRetentionDocumentReferenceType;
import org.openfact.pe.models.types.retention.SUNATRetentionInformationType;
import org.openfact.pe.models.types.summary.SummaryDocumentsType;
import org.openfact.pe.models.types.voided.VoidedDocumentsLineType;
import org.openfact.pe.models.types.voided.VoidedDocumentsType;

public class SunatTypeToModel {

    public static LocalDate toDate(XMLGregorianCalendar xmlCal) {
        Date utilDate = xmlCal.toGregorianCalendar().getTime();
        return LocalDateTime.ofInstant(utilDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toDateTime(XMLGregorianCalendar date) {
        return date.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
    }

    public static void importPerception(OpenfactSession session, OrganizationModel organization, PerceptionModel model,
                                        PerceptionType type) {
        if (type.getReceiverParty() != null) {
            updateModel(model, type.getReceiverParty());
        }
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
            model.setIssueDateTime(toDateTime(type.getIssueDate().getValue()));
        }
        if (type.getSunatPerceptionSystemCode() != null && type.getSunatPerceptionSystemCode().getValue() != null) {
            model.setSunatPerceptionSystemCode(type.getSunatPerceptionSystemCode().getValue());
        }
        if (type.getSunatPerceptionPercent() != null && type.getSunatPerceptionPercent().getValue() != null) {
            model.setSunatPerceptionPercent(type.getSunatPerceptionPercent().getValue());
        }
        if (type.getTotalInvoiceAmount() != null && type.getTotalInvoiceAmount().getValue() != null) {
            model.setTotalPerceptionAmount(type.getTotalInvoiceAmount().getValue());
        }
        if (type.getSunatTotalCashed() != null && type.getSunatTotalCashed().getValue() != null) {
            model.setTotalCashed(type.getSunatTotalCashed().getValue());
        }

        if (type.getNote() != null && !type.getNote().isEmpty()) {
            for (NoteType item : type.getNote()) {
                model.setNote(item.getValue());
            }
        }
        if (type.getSunatPerceptionDocumentReference() != null
                && !type.getSunatPerceptionDocumentReference().isEmpty()) {
            for (SUNATPerceptionDocumentReferenceType item : type.getSunatPerceptionDocumentReference()) {
                updateModel(model.addPerceptionLine(), item);
            }
        }
    }

    private static void updateModel(PerceptionModel model, PartyType type) {
        if (type.getPartyIdentification() != null) {
            model.setEntityDocumentType(type.getPartyIdentificationAtIndex(0).getID().getSchemeID());
            model.setEntityDocumentNuber(type.getPartyIdentificationAtIndex(0).getIDValue());
        }
        if (type.getPartyName() != null) {
            model.setEntityName(type.getPartyName().get(0).getNameValue());
        }
        if (type.getPostalAddress() != null) {
            updateModel(model, type.getPostalAddress());
        }
        if (type.getContact() != null) {
            updateModel(model, type.getContact());
        }
    }

    private static void updateModel(PerceptionModel model, ContactType type) {
        if (type.getElectronicMailValue() != null) {
            model.setEntityEmail(type.getElectronicMailValue());
        }
    }

    private static void updateModel(PerceptionModel model, AddressType type) {
        if (type.getStreetName() != null) {
            model.setEntityAddress(type.getStreetNameValue());
        }
    }

    private static void updateModel(PerceptionLineModel model, SUNATPerceptionDocumentReferenceType type) {
        if (type.getId() != null && type.getId().getValue() != null) {
            model.setRelatedDocumentNumber(type.getId().getValue());
        }
        if (type.getIssueDate() != null && type.getIssueDate().getValue() != null) {
            model.setRelatedIssueDateTime(toDateTime(type.getIssueDate().getValue()));
        }
        if (type.getTotalInvoiceAmount() != null && type.getTotalInvoiceAmount().getValue() != null) {
            model.setTotalDocumentRelated(type.getTotalInvoiceAmount().getValue());
        }
        if (type.getSunatPerceptionInformation() != null) {
            updateModel(model, type.getSunatPerceptionInformation());
        }
    }

    private static void updateModel(PerceptionLineModel model, SUNATPerceptionInformationType type) {
        if (type.getSunatPerceptionAmount() != null && type.getSunatPerceptionAmount().getValue() != null) {
            model.setSunatNetPerceptionAmount(type.getSunatPerceptionAmount().getValue());
        }
        if (type.getSunatNetTotalCashed() != null && type.getSunatNetTotalCashed().getValue() != null) {
            model.setSunatNetCashed(type.getSunatNetTotalCashed().getValue());
        }
        if (type.getSunatPerceptionDate() != null && type.getSunatPerceptionDate().getValue() != null) {
            model.setPerceptionIssueDateTime(toDateTime(type.getSunatPerceptionDate().getValue()));
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
        if (type.getReceiverParty() != null) {
            updateModel(model, type.getReceiverParty());
        }
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
            model.setIssueDateTime(toDateTime(type.getIssueDate().getValue()));
        }
        if (type.getSunatRetentionSystemCode() != null && type.getSunatRetentionSystemCode().getValue() != null) {
            model.setSunatRetentionSystemCode(type.getSunatRetentionSystemCode().getValue());
        }
        if (type.getSunatRetentionPercent() != null && type.getSunatRetentionPercent().getValue() != null) {
            model.setSunatRetentionPercent(type.getSunatRetentionPercent().getValue());
        }
        if (type.getTotalInvoiceAmount() != null && type.getTotalInvoiceAmount().getValue() != null) {
            model.setTotalRetentionAmount(type.getTotalInvoiceAmount().getValue());
        }
        if (type.getSunatTotalPaid() != null && type.getSunatTotalPaid().getValue() != null) {
            model.setTotalCashed(type.getSunatTotalPaid().getValue());
        }
        if (type.getReceiverParty() != null) {
            updateModel(model, type.getReceiverParty());
        }
        if (type.getNote() != null && !type.getNote().isEmpty()) {
            for (NoteType item : type.getNote()) {
                model.setNote(item.getValue());
            }
        }
        if (type.getSunatRetentionDocumentReference() != null && !type.getSunatRetentionDocumentReference().isEmpty()) {
            for (SUNATRetentionDocumentReferenceType item : type.getSunatRetentionDocumentReference()) {
                updateModel(model.addRetentionLine(), item);
            }
        }
    }

    private static void updateModel(RetentionModel model, PartyType type) {
        if (type.getPartyIdentification() != null) {
            model.setEntityDocumentType(type.getPartyIdentificationAtIndex(0).getID().getSchemeID());
            model.setEntityDocumentNuber(type.getPartyIdentificationAtIndex(0).getIDValue());
        }
        if (type.getPartyName() != null) {
            model.setEntityName(type.getPartyName().get(0).getNameValue());
        }
        if (type.getPostalAddress() != null) {
            updateModel(model, type.getPostalAddress());
        }
        if (type.getContact() != null) {
            updateModel(model, type.getContact());
        }
    }

    private static void updateModel(RetentionModel model, ContactType type) {
        if (type.getElectronicMailValue() != null) {
            model.setEntityEmail(type.getElectronicMailValue());
        }
    }

    private static void updateModel(RetentionModel model, AddressType type) {
        if (type.getStreetName() != null) {
            model.setEntityAddress(type.getStreetNameValue());
        }
    }

    private static void updateModel(RetentionLineModel model, SUNATRetentionDocumentReferenceType type) {
        if (type.getID() != null && type.getID().getValue() != null) {
            model.setRelatedDocumentNumber(type.getID().getValue());
        }
        if (type.getIssueDate() != null && type.getIssueDate().getValue() != null) {
            model.setRelatedIssueDateTime(toDateTime(type.getIssueDate().getValue()));
        }
        if (type.getTotalInvoiceAmount() != null && type.getTotalInvoiceAmount().getValue() != null) {
            model.setTotalDocumentRelated(type.getTotalInvoiceAmount().getValue());
        }
        if (type.getSUNATRetentionInformation() != null) {
            updateModel(model, type.getSUNATRetentionInformation());
        }
    }

    private static void updateModel(RetentionLineModel model, SUNATRetentionInformationType type) {
        if (type.getSUNATRetentionAmount() != null && type.getSUNATRetentionAmount().getValue() != null) {
            model.setSunatNetRetentionAmount(type.getSUNATRetentionAmount().getValue());
        }
        if (type.getSUNATNetTotalPaid() != null && type.getSUNATNetTotalPaid().getValue() != null) {
            model.setSunatNetCashed(type.getSUNATNetTotalPaid().getValue());
        }
        if (type.getSUNATRetentionDate() != null && type.getSUNATRetentionDate().getValue() != null) {
            model.setRetentionIssueDateTime(toDateTime(type.getSUNATRetentionDate().getValue()));
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
        if (type.getUBLVersionID() != null && type.getUBLVersionID().getValue() != null) {
            model.setUblVersionID(type.getUBLVersionID().getValue());
        }
        if (type.getCustomizationID() != null && type.getCustomizationID().getValue() != null) {
            model.setCustomizationID(type.getCustomizationID().getValue());
        }
        if (type.getIssueDate() != null && type.getIssueDate().getValue() != null) {
            model.setIssueDateTime(toDateTime(type.getIssueDate().getValue()));
        }
        if (type.getVoidedDocumentsLine() != null) {
            for (VoidedDocumentsLineType line : type.getVoidedDocumentsLine()) {
                updateModel(model.addVoidedDocumentLines(), line);
            }
        }

    }
    private static void updateModel(VoidedDocumentLineModel model, VoidedDocumentsLineType type) {
        if(type.getLineID()!=null){
            model.setLineId(type.getLineID().getValue());
        }
        if(type.getDocumentTypeCode()!=null){
            model.setDocumentTypeCode(type.getDocumentTypeCode().getValue());
        }
        if(type.getDocumentNumberID()!=null){
            model.setDocumentNumberId(type.getDocumentNumberID().getValue());
        }
        if(type.getDocumentSerialID()!=null){
            model.setDocumentSerialId(type.getDocumentSerialID().getValue());
        }
        if(type.getVoidReasonDescription()!=null){
            model.setVoidReasonDescription(type.getVoidReasonDescription().getValue());
        }
    }
}
