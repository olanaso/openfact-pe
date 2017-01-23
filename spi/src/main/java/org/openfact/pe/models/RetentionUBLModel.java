package org.openfact.pe.models;

import org.json.JSONObject;
import org.openfact.ubl.UBLModel;

public class RetentionUBLModel implements UBLModel {

    public static final String PERCEPTION_TEMPLATE_NAME = "retention.jrxml";

    private RetentionModel model;

    public RetentionUBLModel(RetentionModel model) {
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
