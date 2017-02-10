package org.openfact.pe.services.ubl.data.xml;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openfact.JSONObjectUtils;
import org.openfact.pe.models.enums.TipoConceptosTributarios;
import org.openfact.pe.services.ubl.data.xml.entity.XmlSUNATPerceptionDocumentReference;
import org.openfact.pe.services.ubl.data.xml.entity.XmlSUNATRetentionDocumentReference;
import org.openfact.ubl.data.xml.XMLAttributeContainer;
import org.openfact.ubl.data.xml.XmlConverter;
import org.openfact.ubl.data.xml.entity.XmlInvoiceDocumentLineEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import static com.google.common.base.Verify.verifyNotNull;

public enum SunatXmlSupportedAttribute {

    SUNAT_TOTAL_VALOR_VENTA_OPERACIONES_GRAVADAS(XMLAttributeContainer.simpleKey(value -> {
        if (value == null) {
            return null;
        }
        return getSunatAdditionalMonetaryTotal(value, TipoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRAVADAS);
    }, "UBLExtensions", "UBLExtension")),

    SUNAT_TOTAL_VALOR_VENTA_OPERACIONES_EXONERADAS(XMLAttributeContainer.simpleKey(value -> {
        if (value == null) {
            return null;
        }
        return getSunatAdditionalMonetaryTotal(value, TipoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_EXONERADAS);
    }, "UBLExtensions", "UBLExtension")),

    SUNAT_TOTAL_VALOR_VENTA_OPERACIONES_INAFECTAS(XMLAttributeContainer.simpleKey(value -> {
        if (value == null) {
            return null;
        }
        return getSunatAdditionalMonetaryTotal(value, TipoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_INAFECTAS);
    }, "UBLExtensions", "UBLExtension")),

    SUNAT_TOTAL_VALOR_VENTA_OPERACIONES_GRATUITAS(XMLAttributeContainer.simpleKey(value -> {
        if (value == null) {
            return null;
        }
        return getSunatAdditionalMonetaryTotal(value, TipoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRATUITAS);
    }, "UBLExtensions", "UBLExtension")),

    /**
     * Perception
     */
    SUNAT_AP_PARTY_IDENTIFICATION_ID(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "AgentParty", "PartyIdentification", "ID", "content")),

    SUNAT_AP_PARTY_NAME(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "AgentParty", "PartyName", "Name")),

    SUNAT_AP_POSTAL_ADDRESS_ID(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "AgentParty", "PostalAddress", "ID")),

    SUNAT_AP_POSTAL_ADDRESS_STREET_NAME(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "AgentParty", "PostalAddress", "StreetName")),

    SUNAT_AP_POSTAL_ADDRESS_CITY_SUBDIVISION_NAME(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "AgentParty", "PostalAddress", "CitySubdivisionName")),

    SUNAT_AP_POSTAL_ADDRESS_CITY_NAME(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "AgentParty", "PostalAddress", "CityName")),

    SUNAT_AP_POSTAL_ADDRESS_CITY_SUBENTITY(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "AgentParty", "PostalAddress", "CountrySubentity")),

    SUNAT_AP_POSTAL_ADDRESS_DISTRICT(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "AgentParty", "PostalAddress", "District")),

    SUNAT_AP_POSTAL_ADDRESS_COUNTRY_IDENTIFICATION_CODE(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "AgentParty", "PostalAddress", "Country", "IdentificationCode")),

    SUNAT_AP_LEGAL_ENTITY_REGISTRATION_NAME(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "AgentParty", "PartyLegalEntity", "RegistrationName")),


    SUNAT_RP_PARTY_IDENTIFICATION_ID(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "ReceiverParty", "PartyIdentification", "ID", "content")),

    SUNAT_RP_PARTY_NAME(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "ReceiverParty", "PartyName", "Name")),

    SUNAT_RP_POSTAL_ADDRESS_ID(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "ReceiverParty", "PostalAddress", "ID")),

    SUNAT_RP_POSTAL_ADDRESS_STREET_NAME(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "ReceiverParty", "PostalAddress", "StreetName")),

    SUNAT_RP_POSTAL_ADDRESS_CITY_SUBDIVISION_NAME(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "ReceiverParty", "PostalAddress", "CitySubdivisionName")),

    SUNAT_RP_POSTAL_ADDRESS_CITY_NAME(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "ReceiverParty", "PostalAddress", "CityName")),

    SUNAT_RP_POSTAL_ADDRESS_CITY_SUBENTITY(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "ReceiverParty", "PostalAddress", "CountrySubentity")),

    SUNAT_RP_POSTAL_ADDRESS_DISTRICT(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "ReceiverParty", "PostalAddress", "District")),

    SUNAT_RP_POSTAL_ADDRESS_COUNTRY_IDENTIFICATION_CODE(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "ReceiverParty", "PostalAddress", "Country", "IdentificationCode")),

    SUNAT_RP_LEGAL_ENTITY_REGISTRATION_NAME(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "ReceiverParty", "PartyLegalEntity", "RegistrationName")),


    SUNAT_TOTAL_INVOICE_AMOUNT(XMLAttributeContainer.simpleKey(value -> value != null ? new BigDecimal(String.valueOf(value)) : null,
            "TotalInvoiceAmount", "content")),
    SUNAT_TOTAL_INVOICE_CURRENCY_CODE(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "TotalInvoiceAmount", "currencyID")),

    SUNAT_TOTAL_CASHED(XMLAttributeContainer.simpleKey(value -> value != null ? new BigDecimal(String.valueOf(value)) : null,
            "SUNATTotalCashed", "content")),
    SUNAT_TOTAL_CASHED_CURRENCY_CODE(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "SUNATTotalCashed", "currencyID")),

    SUNAT_PERCEPTION_DOCUMENT_REFERENCE(XMLAttributeContainer.objectArrayKey(XmlSUNATPerceptionDocumentReference.class, "SUNATPerceptionDocumentReference")),

    /**
     * Retention*/
    SUNAT_TOTAL_PAID(XMLAttributeContainer.simpleKey(value -> value != null ? new BigDecimal(String.valueOf(value)) : null,
            "SUNATTotalPaid", "content")),
    SUNAT_TOTAL_PAID_CURRENCY_CODE(XMLAttributeContainer.simpleKey(value -> value != null ? String.valueOf(value) : null,
            "SUNATTotalPaid", "currencyID")),

    SUNAT_RETENTION_DOCUMENT_REFERENCE(XMLAttributeContainer.objectArrayKey(XmlSUNATRetentionDocumentReference.class, "SUNATRetentionDocumentReference"));

    private XmlConverter converter;

    SunatXmlSupportedAttribute(XmlConverter converter) {
        this.converter = converter;
    }

    public Object asObject(JSONObject json) {
        verifyNotNull(converter);
        return converter.asObject(json);
    }

    public static SunatXmlSupportedAttribute fromString(String text) {
        Optional<SunatXmlSupportedAttribute> op = Arrays.stream(SunatXmlSupportedAttribute.values())
                .filter(p -> p.toString().equals(text))
                .findFirst();
        return op.isPresent() ? op.get() : null;
    }

    public static BigDecimal getSunatAdditionalMonetaryTotal(Object value, TipoConceptosTributarios tipoConcepto) {
        if (value instanceof JSONObject) {
            value = JSONObjectUtils.getObject((JSONObject) value, "ExtensionContent", "AdditionalInformation", "AdditionalMonetaryTotal");
        }
        if (value instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) value;
            Iterator it = jsonArray.iterator();
            while (it.hasNext()) {
                value = JSONObjectUtils.getObject((JSONObject) it.next(), "ExtensionContent", "AdditionalInformation", "AdditionalMonetaryTotal");
                break;
            }
        }

        if (value == null) {
            return null;
        }

        String codigo = tipoConcepto.getCodigo();

        BigDecimal result = null;
        if (value instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) value;
            String ID = String.valueOf(JSONObjectUtils.getObject(jsonObject, "ID"));
            if (codigo.equals(ID)) {
                Object object = JSONObjectUtils.getObject(jsonObject, "PayableAmount", "content");
                result = new BigDecimal(String.valueOf(object));
            }
        } else if (value instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) value;
            Iterator it = jsonArray.iterator();
            while (it.hasNext()) {
                JSONObject jsonObject = (JSONObject) it.next();
                String ID = String.valueOf(JSONObjectUtils.getObject(jsonObject, "ID"));
                if (codigo.equals(ID)) {
                    Object object = JSONObjectUtils.getObject(jsonObject, "PayableAmount", "content");
                    result = new BigDecimal(String.valueOf(object));
                    break;
                }
            }
        }

        return result;
    }

}
