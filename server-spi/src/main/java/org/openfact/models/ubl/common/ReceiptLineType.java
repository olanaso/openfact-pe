//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.14 at 11:44:49 AM PET 
//

package org.openfact.models.ubl.common;

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


import org.openfact.models.ubl.common.DocumentReferenceType;
import org.openfact.models.ubl.common.IDType;
import org.openfact.models.ubl.common.ItemType;
import org.openfact.models.ubl.common.LineReferenceType;
import org.openfact.models.ubl.common.NoteType;
import org.openfact.models.ubl.common.OrderLineReferenceType;
import org.openfact.models.ubl.common.OversupplyQuantityType;
import org.openfact.models.ubl.common.ReceivedDateType;
import org.openfact.models.ubl.common.ReceivedQuantityType;
import org.openfact.models.ubl.common.RejectActionCodeType;
import org.openfact.models.ubl.common.RejectReasonCodeType;
import org.openfact.models.ubl.common.RejectReasonType;
import org.openfact.models.ubl.common.RejectedQuantityType;
import org.openfact.models.ubl.common.ShipmentType;
import org.openfact.models.ubl.common.ShortQuantityType;
import org.openfact.models.ubl.common.ShortageActionCodeType;
import org.openfact.models.ubl.common.TimingComplaintCodeType;
import org.openfact.models.ubl.common.TimingComplaintType;
import org.openfact.models.ubl.common.UUIDType;

public class ReceiptLineType {

    protected IDType ID;
    protected UUIDType uuid;
    protected NoteType note;
    protected ReceivedQuantityType receivedQuantity;
    protected ShortQuantityType shortQuantity;
    protected ShortageActionCodeType shortageActionCode;
    protected RejectedQuantityType rejectedQuantity;
    protected RejectReasonCodeType rejectReasonCode;
    protected RejectReasonType rejectReason;
    protected RejectActionCodeType rejectActionCode;
    protected OversupplyQuantityType oversupplyQuantity;
    protected ReceivedDateType receivedDate;
    protected TimingComplaintCodeType timingComplaintCode;
    protected TimingComplaintType timingComplaint;
    protected OrderLineReferenceType orderLineReference;
    protected List<LineReferenceType> despatchLineReference;
    protected List<DocumentReferenceType> documentReference;
    protected List<ItemType> item;
    protected List<ShipmentType> shipment;
    protected String id;

    public IDType getID() {
        return ID;
    }

    public void setID(IDType value) {
        this.ID = value;
    }

    public UUIDType getUUID() {
        return uuid;
    }

    public void setUUID(UUIDType value) {
        this.uuid = value;
    }

    public NoteType getNote() {
        return note;
    }

    public void setNote(NoteType value) {
        this.note = value;
    }

    public ReceivedQuantityType getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(ReceivedQuantityType value) {
        this.receivedQuantity = value;
    }

    public ShortQuantityType getShortQuantity() {
        return shortQuantity;
    }

    public void setShortQuantity(ShortQuantityType value) {
        this.shortQuantity = value;
    }

    public ShortageActionCodeType getShortageActionCode() {
        return shortageActionCode;
    }

    public void setShortageActionCode(ShortageActionCodeType value) {
        this.shortageActionCode = value;
    }

    public RejectedQuantityType getRejectedQuantity() {
        return rejectedQuantity;
    }

    public void setRejectedQuantity(RejectedQuantityType value) {
        this.rejectedQuantity = value;
    }

    public RejectReasonCodeType getRejectReasonCode() {
        return rejectReasonCode;
    }

    public void setRejectReasonCode(RejectReasonCodeType value) {
        this.rejectReasonCode = value;
    }

    public RejectReasonType getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(RejectReasonType value) {
        this.rejectReason = value;
    }

    public RejectActionCodeType getRejectActionCode() {
        return rejectActionCode;
    }

    public void setRejectActionCode(RejectActionCodeType value) {
        this.rejectActionCode = value;
    }

    public OversupplyQuantityType getOversupplyQuantity() {
        return oversupplyQuantity;
    }

    public void setOversupplyQuantity(OversupplyQuantityType value) {
        this.oversupplyQuantity = value;
    }

    public ReceivedDateType getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(ReceivedDateType value) {
        this.receivedDate = value;
    }

    public TimingComplaintCodeType getTimingComplaintCode() {
        return timingComplaintCode;
    }

    public void setTimingComplaintCode(TimingComplaintCodeType value) {
        this.timingComplaintCode = value;
    }

    public TimingComplaintType getTimingComplaint() {
        return timingComplaint;
    }

    public void setTimingComplaint(TimingComplaintType value) {
        this.timingComplaint = value;
    }

    public OrderLineReferenceType getOrderLineReference() {
        return orderLineReference;
    }

    public void setOrderLineReference(OrderLineReferenceType value) {
        this.orderLineReference = value;
    }

    public List<LineReferenceType> getDespatchLineReference() {
        if (despatchLineReference == null) {
            despatchLineReference = new ArrayList<LineReferenceType>();
        }
        return this.despatchLineReference;
    }

    public void setDespatchLineReference(List<LineReferenceType> despatchLineReference) {
        this.despatchLineReference = despatchLineReference;
    }

    public List<DocumentReferenceType> getDocumentReference() {
        if (documentReference == null) {
            documentReference = new ArrayList<DocumentReferenceType>();
        }
        return this.documentReference;
    }

    public void setDocumentReference(List<DocumentReferenceType> documentReference) {
        this.documentReference = documentReference;
    }

    public List<ItemType> getItem() {
        if (item == null) {
            item = new ArrayList<ItemType>();
        }
        return this.item;
    }

    public void setItem(List<ItemType> item) {
        this.item = item;
    }

    public List<ShipmentType> getShipment() {
        if (shipment == null) {
            shipment = new ArrayList<ShipmentType>();
        }
        return this.shipment;
    }

    public void setShipment(List<ShipmentType> shipment) {
        this.shipment = shipment;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
