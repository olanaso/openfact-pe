package org.openfact.pe.models;

import org.json.JSONObject;
import org.openfact.ubl.UBLModel;

public class PerceptionUBLModel implements UBLModel {

    public static final String PERCEPTION_TEMPLATE_NAME = "perception.jrxml";

    private PerceptionModel model;

    public PerceptionUBLModel(PerceptionModel model) {
        this.model = model;
    }

    @Override
    public String getTemplateName() {
        return PERCEPTION_TEMPLATE_NAME;
    }

    @Override
    public String getDocumentId() {
        return model.getDocumentId();
    }

    @Override
    public JSONObject getXmlAsJSONObject() {
        return model.getXmlAsJSONObject();
    }

}
