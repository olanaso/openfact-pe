package org.openfact.pe.services.ubl.data.model;

import org.openfact.pe.models.utils.SunatTypeToModel;

import java.util.Arrays;
import java.util.Optional;

public enum SunatModelSupportedAttribute {

    SUNAT_MODEL_TICKET(SunatTypeToModel.NUMERO_TICKET);

    private String additionalInformation;

    SunatModelSupportedAttribute(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getAdditionalInformation() {
        return this.additionalInformation;
    }

    public static SunatModelSupportedAttribute fromString(String text) {
        Optional<SunatModelSupportedAttribute> op = Arrays.stream(SunatModelSupportedAttribute.values())
                .filter(p -> p.toString().equals(text))
                .findFirst();
        return op.isPresent() ? op.get() : null;
    }

}
