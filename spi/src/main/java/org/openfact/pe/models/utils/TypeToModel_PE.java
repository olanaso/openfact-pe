package org.openfact.pe.models.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.types.PerceptionType;
import org.openfact.pe.types.RetentionType;
import org.openfact.pe.types.SUNATPerceptionDocumentReferenceType;
import org.openfact.pe.types.SummaryDocumentsType;
import org.openfact.pe.types.VoidedDocumentsType;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.NoteType;

public class TypeToModel_PE {

    public static LocalDate toDate(XMLGregorianCalendar xmlCal) {
        Date utilDate = xmlCal.toGregorianCalendar().getTime();
        return LocalDateTime.ofInstant(utilDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    public static void importPerception(OpenfactSession session, OrganizationModel organization,
            PerceptionModel model, PerceptionType type) {
        if (type.getUblVersionID() != null && type.getUblVersionID().getValue() != null) {
            model.setUblVersionID(type.getUblVersionID().getValue());
        }
        if (type.getAgentParty() != null) {
            updateModel(model.getAgentPartyAsNotNull(), type.getAgentParty());
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
        if (type.getNote() != null) {
            for (NoteType item : type.getNote()) {
                model.getNotes().add(item.getValue());
            }
        }
        if (type.getReceiverParty() != null) {
            updateModel(model.getReceiverPartyAsNotNull(), type.getReceiverParty());
        }
        if (type.getSunatPerceptionDocumentReference() != null) {
            for (SUNATPerceptionDocumentReferenceType item : type.getSunatPerceptionDocumentReference()) {
                updateModel(model.addSUNATPerceptionDocumentReference(), item);
            }
        }
        if (type.getSunatPerceptionPercent() != null && type.getSunatPerceptionPercent().getValue() != null) {
            model.setSUNATPerceptionPercent(type.getSunatPerceptionPercent().getValue());
        }
        if (type.getSunatTotalCashed() != null && type.getSunatTotalCashed().getValue() != null) {
            model.setSUNATTotalCashed(type.getSunatTotalCashed().getValue());
        }
        if (type.getTotalInvoiceAmount() != null && type.getTotalInvoiceAmount().getValue() != null) {
            model.setTotalInvoiceAmount(type.getTotalInvoiceAmount().getValue());
        }
        if (type.getTotalInvoiceAmount() != null && type.getTotalInvoiceAmount().getValue() != null) {
            model.setTotalInvoiceAmount(type.getTotalInvoiceAmount().getValue());
        }
    }

    public static void importRetention(OpenfactSession session, OrganizationModel organization,
            RetentionModel perception, RetentionType type) {

    }

    public static void importSummaryDocument(OpenfactSession session, OrganizationModel organization,
            SummaryDocumentModel perception, SummaryDocumentsType type) {

    }

    public static void importVoidedDocument(OpenfactSession session, OrganizationModel organization,
            VoidedDocumentModel perception, VoidedDocumentsType type) {

    }

}
