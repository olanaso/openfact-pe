package org.openfact.pe.models;

import java.util.Set;

import org.openfact.models.enums.RequiredActionDocument;

public interface RetentionModel {

    String getId();

    byte[] getXmlDocument();

    void setXmlDocument(byte[] bytes);
    
    /**
     * Required Actions
     */
    Set<String> getRequiredActions();

    void addRequiredAction(String action);

    void removeRequiredAction(String action);

    void addRequiredAction(RequiredActionDocument action);

    void removeRequiredAction(RequiredActionDocument action);
}
