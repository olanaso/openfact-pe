//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.ubl.common;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;


import org.openfact.models.ubl.common.BinaryObjectMimeCodeContentType;

public class VideoType {

    protected byte[] value;
    protected String format;
    protected BinaryObjectMimeCodeContentType mimeCode;
    protected String encodingCode;
    protected String uri;
    protected String filename;
    protected String id;

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = ((byte[]) value);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String value) {
        this.format = value;
    }

    public BinaryObjectMimeCodeContentType getMimeCode() {
        return mimeCode;
    }

    public void setMimeCode(BinaryObjectMimeCodeContentType value) {
        this.mimeCode = value;
    }

    public String getEncodingCode() {
        return encodingCode;
    }

    public void setEncodingCode(String value) {
        this.encodingCode = value;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String value) {
        this.uri = value;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String value) {
        this.filename = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
