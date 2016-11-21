package org.openfact.pe.models;

public interface VoidedDocumentsLineModel {

    String getId();

    String getLineId();

    void setLineId(String value);

    String getDocumentTypeCode();

    void setDocumentTypeCode(String value);

    String getDocumentSerialId();

    void setDocumentSerialId(String value);

    String getDocumentNumberId();

    void setDocumentNumberId(String value);

    String getVoidReasonDescription();

    void setVoidReasonDescription(String value);

}
