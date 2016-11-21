package org.openfact.pe.models;

import java.util.Set;

import org.openfact.models.enums.RequiredActionDocument;

public interface PerceptionModel {

    String getId();

    void getXmlDocument();

    void setXmlDocument(byte[] object);

    /**
     * Required Actions
     */
    Set<String> getRequiredActions();

    void addRequiredAction(String action);

    void removeRequiredAction(String action);

    void addRequiredAction(RequiredActionDocument action);

    void removeRequiredAction(RequiredActionDocument action);

}
