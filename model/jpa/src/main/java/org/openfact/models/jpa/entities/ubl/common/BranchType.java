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
import org.openfact.models.ubl.common.AddressType;
import org.openfact.models.ubl.common.FinancialInstitutionType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.NameTypeCommBas;

@Entity(name = "BranchType")
@Table(name = "BRANCHTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class BranchType {

    protected IDType ID;
    protected NameTypeCommBas name;
    protected FinancialInstitutionType financialInstitution;
    protected AddressType address;
    protected String id;

    @ManyToOne(targetEntity = IDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ID_BRANCHTYPE_OFID")
    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    @ManyToOne(targetEntity = NameTypeCommBas.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "NAME__BRANCHTYPE_OFID")
    public NameTypeCommBas getName() {
        return name;
    }

    public void setName(NameTypeCommBas value) {
        this.name = value;
    }

    @ManyToOne(targetEntity = FinancialInstitutionType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "FINANCIALINSTITUTION_BRANCHT_0")
    public FinancialInstitutionType getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(FinancialInstitutionType value) {
        this.financialInstitution = value;
    }

    @ManyToOne(targetEntity = AddressType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ADDRESS_BRANCHTYPE_OFID")
    public AddressType getAddress() {
        return address;
    }

    public void setAddress(AddressType value) {
        this.address = value;
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
