//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.ubl.common;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import org.openfact.models.ubl.common.EmergencyProceduresCodeType;
import org.openfact.models.ubl.common.ExtensionType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.PlacardEndorsementType;
import org.openfact.models.ubl.common.PlacardNotationType;

public class SecondaryHazardType {

    protected IDType ID;
    protected PlacardNotationType placardNotation;
    protected PlacardEndorsementType placardEndorsement;
    protected EmergencyProceduresCodeType emergencyProceduresCode;
    protected ExtensionType extension;
    protected String id;

    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    public PlacardNotationType getPlacardNotation() {
        return placardNotation;
    }

    public void setPlacardNotation(PlacardNotationType value) {
        this.placardNotation = value;
    }

    public PlacardEndorsementType getPlacardEndorsement() {
        return placardEndorsement;
    }

    public void setPlacardEndorsement(PlacardEndorsementType value) {
        this.placardEndorsement = value;
    }

    public EmergencyProceduresCodeType getEmergencyProceduresCode() {
        return emergencyProceduresCode;
    }

    public void setEmergencyProceduresCode(EmergencyProceduresCodeType value) {
        this.emergencyProceduresCode = value;
    }

    public ExtensionType getExtension() {
        return extension;
    }

    public void setExtension(ExtensionType value) {
        this.extension = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
