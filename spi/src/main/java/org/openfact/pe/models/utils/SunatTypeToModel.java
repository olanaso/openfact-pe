package org.openfact.pe.models.utils;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyType;
import org.openfact.models.DocumentModel;
import org.openfact.models.OrganizationModel;
import org.openfact.models.utils.TypeToModel;
import org.openfact.pe.ubl.ubl21.perception.PerceptionType;
import org.openfact.pe.ubl.ubl21.retention.RetentionType;
import org.openfact.pe.ubl.ubl21.summary.SummaryDocumentsType;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsType;

import javax.ejb.Stateless;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class SunatTypeToModel {

    public static final String SUNAT_PERCEPTION_SYSTEM_CODE = "sunatPerceptionSystemCode";
    public static final String SUNAT_PERCEPTION_PERCENT = "sunatPerceptionPercent";

    public static final String SUNAT_TOTAL_CASHED_AMOUNT = "sunatTotalCashedAmount";
    public static final String SUNAT_TOTAL_CASHED_CURRENCY_ID = "sunatTotalCashedCurrencyID";

    public static final String SUNAT_RETENTION_SYSTEM_CODE = "sunatRetentionSystemCode";
    public static final String SUNAT_RETENTION_PERCENT = "sunatRetentionPercent";

    public static final String SUNAT_TOTAL_PAID_AMOUNT = "sunatTotalPaidAmount";
    public static final String SUNAT_TOTAL_PAID_CURRENCY_ID = "sunatTotalPaidAmountCurrencyID";

    public static final String RECEIVER_PARTY_REGISTRATION_NAME = "receiverPartyRegistrationName";
    public static final String RECEIVER_PARTY_IDENTIFICATION_ID = "receiverPartyIdentificationID";

    public static final String TOTAL_OPERACIONES_GRAVADAS = "totalOperacionesGravadas";
    public static final String TOTAL_OPERACIONES_EXONERADAS = "totalOperacionesExoneradas";
    public static final String TOTAL_OPERACIONES_INAFECTAS = "totalOperacionesInafectas";
    public static final String TOTAL_OPERACIONES_GRATUITAS = "totalOperacionesGratuitas";
    public static final String TOTAL_IGV = "totalIgv";
    public static final String VALOR_IGV = "valorIgv";
    public static final String PORCENTAJE_DESCUENTO_GLOBAL_APLICADO = "pocentajeDescuentoGlobalAplicado";
    public static final String TOTAL_OTROS_CARGOS_APLICADO = "totalOtrosCargosAplicado";
    public static final String ES_OPERACION_GRATUITA = "esOperacionGratuita";

    public static final String NUMERO_TICKET = "ticket";

    public static LocalDate toDate(XMLGregorianCalendar xmlCal) {
        Date utilDate = xmlCal.toGregorianCalendar().getTime();
        return LocalDateTime.ofInstant(utilDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toDateTime(XMLGregorianCalendar date) {
        return date.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
    }

    public void importPerception(OrganizationModel organization, DocumentModel model, PerceptionType type) {
        if (type.getIssueDate() != null) {
            model.setSingleAttribute(TypeToModel.ISSUE_DATE, type.getIssueDate().getValue().toString());
        }
        if (type.getSunatPerceptionSystemCode() != null) {
            model.setSingleAttribute(SUNAT_PERCEPTION_SYSTEM_CODE, type.getSunatPerceptionSystemCode().getValue());
        }
        if (type.getSunatPerceptionPercent() != null) {
            model.setSingleAttribute(SUNAT_PERCEPTION_PERCENT, type.getSunatPerceptionPercent().getValue().toString());
        }
        if (type.getSunatTotalCashed() != null) {
            model.setSingleAttribute(SUNAT_TOTAL_CASHED_AMOUNT, type.getSunatTotalCashed().getValue().toString());
            model.setSingleAttribute(SUNAT_TOTAL_CASHED_CURRENCY_ID, type.getSunatTotalCashed().getCurrencyID());
        }
        if (type.getReceiverParty() != null) {
            addReceiverAttributes(type.getReceiverParty(), model);
        }
    }

    public void importRetention(OrganizationModel organization, DocumentModel model, RetentionType type) {
        if (type.getIssueDate() != null) {
            model.setSingleAttribute(TypeToModel.ISSUE_DATE, type.getIssueDate().getValue().toString());
        }
        if (type.getSunatRetentionSystemCode() != null) {
            model.setSingleAttribute(SUNAT_RETENTION_SYSTEM_CODE, type.getSunatRetentionSystemCode().getValue());
        }
        if (type.getSunatRetentionPercent() != null) {
            model.setSingleAttribute(SUNAT_RETENTION_PERCENT, type.getSunatRetentionPercent().getValue().toString());
        }
        if (type.getSunatTotalPaid() != null) {
            model.setSingleAttribute(SUNAT_TOTAL_PAID_AMOUNT, type.getSunatTotalPaid().getValue().toString());
            model.setSingleAttribute(SUNAT_TOTAL_PAID_CURRENCY_ID, type.getSunatTotalPaid().getCurrencyID().toString());
        }
    }

    public void importSummaryDocument(OrganizationModel organization, DocumentModel model, SummaryDocumentsType type) {
        if (type.getIssueDate() != null) {
            model.setSingleAttribute(TypeToModel.ISSUE_DATE, type.getIssueDate().getValue().toString());
        }
    }

    public void importVoidedDocument(OrganizationModel organization, DocumentModel model, VoidedDocumentsType type) {
        if (type.getIssueDate() != null) {
            model.setSingleAttribute(TypeToModel.ISSUE_DATE, type.getIssueDate().getValue().toString());
        }
    }

    public void addReceiverAttributes(PartyType partyType, DocumentModel documentModel) {
        if (partyType.getPartyIdentification() != null && !partyType.getPartyIdentification().isEmpty()) {
            List<String> partyIdentification = partyType.getPartyIdentification().stream().map(f -> f.getIDValue()).collect(Collectors.toList());
            documentModel.setAttribute(RECEIVER_PARTY_IDENTIFICATION_ID, partyIdentification);
        }
        if (partyType.getPartyLegalEntity() != null && !partyType.getPartyLegalEntity().isEmpty()) {
            List<String> partyLegalEntityName = partyType.getPartyLegalEntity().stream().map(f -> f.getRegistrationNameValue()).collect(Collectors.toList());
            documentModel.setAttribute(RECEIVER_PARTY_REGISTRATION_NAME, partyLegalEntityName);
        }
    }

}
