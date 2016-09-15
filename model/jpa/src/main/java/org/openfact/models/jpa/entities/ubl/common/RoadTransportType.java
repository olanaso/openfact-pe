//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.jpa.entities.ubl.common;

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

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "RoadTransportType")
@Table(name = "ROADTRANSPORTTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class RoadTransportType {

    protected LicensePlateIDType licensePlateID;
    protected String id;

    @ManyToOne(targetEntity = LicensePlateIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "LICENSEPLATEID_ROADTRANSPORT_0")
    public LicensePlateIDType getLicensePlateID() {
        return licensePlateID;
    }

    public void setLicensePlateID(LicensePlateIDType value) {
        this.licensePlateID = value;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}