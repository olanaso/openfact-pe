package org.openfact.pe.models.utils;

import org.openfact.models.DocumentModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.utils.TypeToModel;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.pe.models.types.summary.SummaryDocumentsType;
import org.openfact.pe.models.types.voided.VoidedDocumentsType;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SunatTypeToModel extends TypeToModel {

    public static final String SUNAT_PERCEPTION_SYSTEM_CODE = "sunatPerceptionSystemCode";
    public static final String SUNAT_PERCEPTION_PERCENT = "sunatPerceptionPercent";
    public static final String SUNAT_TOTAL_CASHED = "sunatTotalCashed";
    public static final String SUNAT_RETENTION_SYSTEM_CODE = "sunatRetentionSystemCode";
    public static final String SUNAT_RETENTION_PERCENT = "sunatRetentionPercent";
    public static final String SUNAT_TOTAL_PAID = "sunatTotalPaid";

    public static LocalDate toDate(XMLGregorianCalendar xmlCal) {
        Date utilDate = xmlCal.toGregorianCalendar().getTime();
        return LocalDateTime.ofInstant(utilDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toDateTime(XMLGregorianCalendar date) {
        return date.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
    }

    public static void importPerception(OpenfactSession session, OrganizationModel organization, DocumentModel model, PerceptionType type) {
        if (type.getIssueDate() != null) {
            model.setSingleAttribute(ISSUE_DATE, type.getIssueDate().getValue().toString());
        }
        if (type.getSunatPerceptionSystemCode() != null) {
            model.setSingleAttribute(SUNAT_PERCEPTION_SYSTEM_CODE, type.getSunatPerceptionSystemCode().getValue());
        }
        if (type.getSunatPerceptionPercent() != null) {
            model.setSingleAttribute(SUNAT_PERCEPTION_PERCENT, type.getSunatPerceptionPercent().getValue().toString());
        }
        if (type.getSunatTotalCashed() != null) {
            model.setSingleAttribute(SUNAT_TOTAL_CASHED, type.getSunatTotalCashed().getValue().toString());
        }
    }

    public static void importRetention(OpenfactSession session, OrganizationModel organization, DocumentModel model, RetentionType type) {
        if (type.getIssueDate() != null) {
            model.setSingleAttribute(ISSUE_DATE, type.getIssueDate().getValue().toString());
        }
        if (type.getSunatRetentionSystemCode() != null) {
            model.setSingleAttribute(SUNAT_RETENTION_SYSTEM_CODE, type.getSunatRetentionSystemCode().getValue());
        }
        if (type.getSunatRetentionPercent() != null) {
            model.setSingleAttribute(SUNAT_RETENTION_PERCENT, type.getSunatRetentionPercent().getValue().toString());
        }
        if (type.getSunatTotalPaid() != null) {
            model.setSingleAttribute(SUNAT_TOTAL_PAID, type.getSunatTotalPaid().getValue().toString());
        }
    }

    public static void importSummaryDocument(OpenfactSession session, OrganizationModel organization, DocumentModel model, SummaryDocumentsType type) {
        if (type.getIssueDate() != null) {
            model.setSingleAttribute(ISSUE_DATE, type.getIssueDate().getValue().toString());
        }
    }

    public static void importVoidedDocument(OpenfactSession session, OrganizationModel organization, DocumentModel model, VoidedDocumentsType type) {
        if (type.getIssueDate() != null) {
            model.setSingleAttribute(ISSUE_DATE, type.getIssueDate().getValue().toString());
        }
    }

}
