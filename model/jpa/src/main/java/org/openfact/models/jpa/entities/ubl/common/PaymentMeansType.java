//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.jpa.entities.ubl.common;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.openfact.models.ubl.common.CardAccountType;
import org.openfact.models.ubl.common.CreditAccountType;
import org.openfact.models.ubl.common.FinancialAccountType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.InstructionIDType;
import org.openfact.models.ubl.common.InstructionNoteType;
import org.openfact.models.ubl.common.PaymentChannelCodeType;
import org.openfact.models.ubl.common.PaymentDueDateType;
import org.openfact.models.ubl.common.PaymentIDType;
import org.openfact.models.ubl.common.PaymentMeansCodeTypeCommBas;

@Entity(name = "PaymentMeansType")
@Table(name = "PAYMENTMEANSTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class PaymentMeansType {

    protected IDType ID;
    protected PaymentMeansCodeTypeCommBas paymentMeansCode;
    protected PaymentDueDateType paymentDueDate;
    protected PaymentChannelCodeType paymentChannelCode;
    protected InstructionIDType instructionID;
    protected List<InstructionNoteType> instructionNote;
    protected List<PaymentIDType> paymentID;
    protected CardAccountType cardAccount;
    protected FinancialAccountType payerFinancialAccount;
    protected FinancialAccountType payeeFinancialAccount;
    protected CreditAccountType creditAccount;
    protected String id;

    @ManyToOne(targetEntity = IDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "ID_PAYMENTMEANSTYPE_OFID")
    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    @ManyToOne(targetEntity = PaymentMeansCodeTypeCommBas.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYMENTMEANSCODE_PAYMENTMEAN_0")
    public PaymentMeansCodeTypeCommBas getPaymentMeansCode() {
        return paymentMeansCode;
    }

    public void setPaymentMeansCode(PaymentMeansCodeTypeCommBas value) {
        this.paymentMeansCode = value;
    }

    @ManyToOne(targetEntity = PaymentDueDateType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYMENTDUEDATE_PAYMENTMEANST_0")
    public PaymentDueDateType getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(PaymentDueDateType value) {
        this.paymentDueDate = value;
    }

    @ManyToOne(targetEntity = PaymentChannelCodeType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYMENTCHANNELCODE_PAYMENTME_0")
    public PaymentChannelCodeType getPaymentChannelCode() {
        return paymentChannelCode;
    }

    public void setPaymentChannelCode(PaymentChannelCodeType value) {
        this.paymentChannelCode = value;
    }

    @ManyToOne(targetEntity = InstructionIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "INSTRUCTIONID_PAYMENTMEANSTY_0")
    public InstructionIDType getInstructionID() {
        return instructionID;
    }

    public void setInstructionID(InstructionIDType value) {
        this.instructionID = value;
    }

    @OneToMany(targetEntity = InstructionNoteType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "INSTRUCTIONNOTE_PAYMENTMEANS_0")
    public List<InstructionNoteType> getInstructionNote() {
        if (instructionNote == null) {
            instructionNote = new ArrayList<InstructionNoteType>();
        }
        return this.instructionNote;
    }

    public void setInstructionNote(List<InstructionNoteType> instructionNote) {
        this.instructionNote = instructionNote;
    }

    @OneToMany(targetEntity = PaymentIDType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYMENTID_PAYMENTMEANSTYPE_H_0")
    public List<PaymentIDType> getPaymentID() {
        if (paymentID == null) {
            paymentID = new ArrayList<PaymentIDType>();
        }
        return this.paymentID;
    }

    public void setPaymentID(List<PaymentIDType> paymentID) {
        this.paymentID = paymentID;
    }

    @ManyToOne(targetEntity = CardAccountType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "CARDACCOUNT_PAYMENTMEANSTYPE_0")
    public CardAccountType getCardAccount() {
        return cardAccount;
    }

    public void setCardAccount(CardAccountType value) {
        this.cardAccount = value;
    }

    @ManyToOne(targetEntity = FinancialAccountType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYERFINANCIALACCOUNT_PAYMEN_0")
    public FinancialAccountType getPayerFinancialAccount() {
        return payerFinancialAccount;
    }

    public void setPayerFinancialAccount(FinancialAccountType value) {
        this.payerFinancialAccount = value;
    }

    @ManyToOne(targetEntity = FinancialAccountType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYEEFINANCIALACCOUNT_PAYMEN_0")
    public FinancialAccountType getPayeeFinancialAccount() {
        return payeeFinancialAccount;
    }

    public void setPayeeFinancialAccount(FinancialAccountType value) {
        this.payeeFinancialAccount = value;
    }

    @ManyToOne(targetEntity = CreditAccountType.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "CREDITACCOUNT_PAYMENTMEANSTY_0")
    public CreditAccountType getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(CreditAccountType value) {
        this.creditAccount = value;
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
