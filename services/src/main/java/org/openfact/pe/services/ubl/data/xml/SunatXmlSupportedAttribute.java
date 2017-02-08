package org.openfact.pe.services.ubl.data.xml;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openfact.JSONObjectUtils;
import org.openfact.pe.models.enums.TipoConceptosTributarios;
import org.openfact.ubl.data.xml.XMLAttributeContainer;
import org.openfact.ubl.data.xml.XmlConverter;

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
    }, "UBLExtensions", "UBLExtension"));

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
