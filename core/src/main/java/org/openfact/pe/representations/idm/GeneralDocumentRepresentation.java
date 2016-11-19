package org.openfact.pe.representations.idm;

public class GeneralDocumentRepresentation {

    protected String type;
    protected DocumentRepresentation document;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DocumentRepresentation getDocument() {
        return document;
    }

    public void setDocument(DocumentRepresentation document) {
        this.document = document;
    }

}