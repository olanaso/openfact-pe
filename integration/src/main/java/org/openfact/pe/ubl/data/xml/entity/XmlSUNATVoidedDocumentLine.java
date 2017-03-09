package org.openfact.pe.ubl.data.xml.entity;

import org.openfact.ubl.data.xml.annotations.JsonWrapper;
import org.openfact.ubl.data.xml.annotations.SimpleKey;
import org.openfact.ubl.data.xml.mappers.StringMapper;

@JsonWrapper
public class XmlSUNATVoidedDocumentLine {

    @SimpleKey(key = {"DocumentSerialID"}, mapper = StringMapper.class)
    private String document_serial_id;

    @SimpleKey(key = {"DocumentNumberID"}, mapper = StringMapper.class)
    private String document_number_id;

    @SimpleKey(key = {"DocumentTypeCode"}, mapper = StringMapper.class)
    private String document_type_code;

    @SimpleKey(key = {"VoidReasonDescription"}, mapper = StringMapper.class)
    private String void_reason_description;

    public String getDocument_serial_id() {
        return document_serial_id;
    }

    public void setDocument_serial_id(String document_serial_id) {
        this.document_serial_id = document_serial_id;
    }

    public String getDocument_number_id() {
        return document_number_id;
    }

    public void setDocument_number_id(String document_number_id) {
        this.document_number_id = document_number_id;
    }

    public String getDocument_type_code() {
        return document_type_code;
    }

    public void setDocument_type_code(String document_type_code) {
        this.document_type_code = document_type_code;
    }

    public String getVoid_reason_description() {
        return void_reason_description;
    }

    public void setVoid_reason_description(String void_reason_description) {
        this.void_reason_description = void_reason_description;
    }
}
