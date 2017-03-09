package org.openfact.pe.ubl.ubl21.voided;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.LineIDType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.TextType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VoidedDocumentsLineType", propOrder = {"lineID", "documentTypeCode", "documentSerialID", "documentNumberID", "voidReasonDescription"}
)
public class VoidedDocumentsLineType {

    @XmlElement(name = "LineID", namespace = "urn:oasis:names:specification:ubl21:schema:xsd:CommonBasicComponents-2", required = true)
    protected LineIDType lineID;

    @XmlElement(name = "DocumentTypeCode", namespace = "urn:oasis:names:specification:ubl21:schema:xsd:CommonBasicComponents-2", required = true)
    protected DocumentTypeCodeType documentTypeCode;

    @XmlElement(name = "DocumentSerialID", namespace = "urn:sunat:names:specification:ubl21:peru:schema:xsd:SunatAggregateComponents-1", required = true)
    protected IdentifierType documentSerialID;

    @XmlElement(name = "DocumentNumberID", namespace = "urn:sunat:names:specification:ubl21:peru:schema:xsd:SunatAggregateComponents-1", required = true)
    protected IdentifierType documentNumberID;

    @XmlElement(name = "VoidReasonDescription", namespace = "urn:sunat:names:specification:ubl21:peru:schema:xsd:SunatAggregateComponents-1", required = true)
    protected TextType voidReasonDescription;

    /**
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:sunat:names:specification:ubl21:peru:schema:xsd:SunatAggregateComponents-1" xmlns:cac="urn:oasis:names:specification:ubl21:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl21:schema:xsd:CommonBasicComponents-2" xmlns:qdt="urn:oasis:names:specification:ubl21:schema:xsd:QualifiedDatatypes-2" xmlns:udt="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;&lt;ccts:DictionaryEntryName&gt;Consolidated Invoice Line. Identifier
     * 							&lt;/ccts:DictionaryEntryName&gt;&lt;ccts:Definition&gt;Identifies the Consolidated Invoice Line.
     * 							&lt;/ccts:Definition&gt;&lt;ccts:Cardinality&gt;1&lt;/ccts:Cardinality&gt;&lt;ccts:ObjectClass&gt;Consolidated Invoice Line&lt;/ccts:ObjectClass&gt;&lt;ccts:PropertyTerm&gt;Identifier&lt;/ccts:PropertyTerm&gt;&lt;ccts:RepresentationTerm&gt;Identifier&lt;/ccts:RepresentationTerm&gt;&lt;ccts:DataType&gt;Identifier. Type&lt;/ccts:DataType&gt;
     * 						&lt;/ccts:Component&gt;
     * </pre>
     *
     * @return possible object is {@link LineIDType }
     */
    public LineIDType getLineID() {
        return lineID;
    }

    /**
     * Sets the value of the lineID property.
     *
     * @param value allowed object is {@link LineIDType }
     */
    public void setLineID(LineIDType value) {
        this.lineID = value;
    }

    /**
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:sunat:names:specification:ubl21:peru:schema:xsd:SunatAggregateComponents-1" xmlns:cac="urn:oasis:names:specification:ubl21:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl21:schema:xsd:CommonBasicComponents-2" xmlns:qdt="urn:oasis:names:specification:ubl21:schema:xsd:QualifiedDatatypes-2" xmlns:udt="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;ccts:ComponentType&gt;BBIE&lt;/ccts:ComponentType&gt;&lt;ccts:DictionaryEntryName&gt;Consolidated Invoice Line Type Code.
     * 								Code
     * 							&lt;/ccts:DictionaryEntryName&gt;&lt;ccts:Definition&gt;Code specifying the type of the Invoice.
     * 							&lt;/ccts:Definition&gt;&lt;ccts:Cardinality&gt;0..1&lt;/ccts:Cardinality&gt;&lt;ccts:ObjectClass&gt;Consolidated Invoice Line&lt;/ccts:ObjectClass&gt;&lt;ccts:PropertyTerm&gt;Consolidated Invoice Line Type Code
     * 							&lt;/ccts:PropertyTerm&gt;&lt;ccts:RepresentationTerm&gt;Code&lt;/ccts:RepresentationTerm&gt;&lt;ccts:DataType&gt;Code. Type&lt;/ccts:DataType&gt;
     * 						&lt;/ccts:Component&gt;
     * </pre>
     *
     * @return possible object is {@link DocumentTypeCodeType }
     */
    public DocumentTypeCodeType getDocumentTypeCode() {
        return documentTypeCode;
    }

    /**
     * Sets the value of the documentTypeCode property.
     *
     * @param value allowed object is {@link DocumentTypeCodeType }
     */
    public void setDocumentTypeCode(DocumentTypeCodeType value) {
        this.documentTypeCode = value;
    }

    /**
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:sunat:names:specification:ubl21:peru:schema:xsd:SunatAggregateComponents-1" xmlns:cac="urn:oasis:names:specification:ubl21:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl21:schema:xsd:CommonBasicComponents-2" xmlns:qdt="urn:oasis:names:specification:ubl21:schema:xsd:QualifiedDatatypes-2" xmlns:udt="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;&lt;ccts:DictionaryEntryName&gt;Consolidated Invoice Line Billing
     * 								Payment
     * 							&lt;/ccts:DictionaryEntryName&gt;&lt;ccts:Definition&gt;An association to Billing Payment.
     * 							&lt;/ccts:Definition&gt;&lt;ccts:Cardinality&gt;1&lt;/ccts:Cardinality&gt;&lt;ccts:ObjectClass&gt;Consolidated Invoice Line&lt;/ccts:ObjectClass&gt;&lt;ccts:PropertyTerm&gt;Billing Payment&lt;/ccts:PropertyTerm&gt;&lt;ccts:AssociatedObjectClass&gt;Consolidated Invoice Line
     * 							&lt;/ccts:AssociatedObjectClass&gt;
     * 						&lt;/ccts:Component&gt;
     * </pre>
     *
     * @return possible object is {@link IdentifierType }
     */
    public IdentifierType getDocumentSerialID() {
        return documentSerialID;
    }

    /**
     * Sets the value of the documentSerialID property.
     *
     * @param value allowed object is {@link IdentifierType }
     */
    public void setDocumentSerialID(IdentifierType value) {
        this.documentSerialID = value;
    }

    /**
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:sunat:names:specification:ubl21:peru:schema:xsd:SunatAggregateComponents-1" xmlns:cac="urn:oasis:names:specification:ubl21:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl21:schema:xsd:CommonBasicComponents-2" xmlns:qdt="urn:oasis:names:specification:ubl21:schema:xsd:QualifiedDatatypes-2" xmlns:udt="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;&lt;ccts:DictionaryEntryName&gt;Consolidated Invoice Line Billing
     * 								Payment
     * 							&lt;/ccts:DictionaryEntryName&gt;&lt;ccts:Definition&gt;An association to Billing Payment.
     * 							&lt;/ccts:Definition&gt;&lt;ccts:Cardinality&gt;1&lt;/ccts:Cardinality&gt;&lt;ccts:ObjectClass&gt;Consolidated Invoice Line&lt;/ccts:ObjectClass&gt;&lt;ccts:PropertyTerm&gt;Billing Payment&lt;/ccts:PropertyTerm&gt;&lt;ccts:AssociatedObjectClass&gt;Consolidated Invoice Line
     * 							&lt;/ccts:AssociatedObjectClass&gt;
     * 						&lt;/ccts:Component&gt;
     * </pre>
     *
     * @return possible object is {@link IdentifierType }
     */
    public IdentifierType getDocumentNumberID() {
        return documentNumberID;
    }

    /**
     * Sets the value of the documentNumberID property.
     *
     * @param value allowed object is {@link IdentifierType }
     */
    public void setDocumentNumberID(IdentifierType value) {
        this.documentNumberID = value;
    }

    /**
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:sunat:names:specification:ubl21:peru:schema:xsd:SunatAggregateComponents-1" xmlns:cac="urn:oasis:names:specification:ubl21:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl21:schema:xsd:CommonBasicComponents-2" xmlns:qdt="urn:oasis:names:specification:ubl21:schema:xsd:QualifiedDatatypes-2" xmlns:udt="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;ccts:ComponentType&gt;ASBIE&lt;/ccts:ComponentType&gt;&lt;ccts:DictionaryEntryName&gt;Voided Document Line Void Reason
     * 								Description
     * 							&lt;/ccts:DictionaryEntryName&gt;&lt;ccts:Definition&gt;An association to Void Reason.&lt;/ccts:Definition&gt;&lt;ccts:Cardinality&gt;1&lt;/ccts:Cardinality&gt;&lt;ccts:ObjectClass&gt;Voided Document Line&lt;/ccts:ObjectClass&gt;&lt;ccts:PropertyTerm&gt;Voided Document&lt;/ccts:PropertyTerm&gt;&lt;ccts:AssociatedObjectClass&gt;Voided Document Line
     * 							&lt;/ccts:AssociatedObjectClass&gt;
     * 						&lt;/ccts:Component&gt;
     * </pre>
     *
     * @return possible object is {@link TextType }
     */
    public TextType getVoidReasonDescription() {
        return voidReasonDescription;
    }

    /**
     * Sets the value of the voidReasonDescription property.
     *
     * @param value allowed object is {@link TextType }
     */
    public void setVoidReasonDescription(TextType value) {
        this.voidReasonDescription = value;
    }

    @Nonnull
    public LineIDType setLineID(@Nullable final String valueParam) {
        LineIDType aObj = getLineID();
        if (aObj == null) {
            aObj = new LineIDType(valueParam);
            setLineID(aObj);
        } else {
            aObj.setValue(valueParam);
        }
        return aObj;
    }

    public DocumentTypeCodeType setDocumentTypeCode(@Nullable final String valueParam) {
        DocumentTypeCodeType type = getDocumentTypeCode();
        if (type == null) {
            type = new DocumentTypeCodeType(valueParam);
            setDocumentTypeCode(type);
        } else {
            type.setValue(valueParam);
        }
        return type;
    }

    public TextType setVoidReasonDescription(@Nullable final String valueParam) {
        TextType type = getVoidReasonDescription();
        if (type == null) {
            type = new TextType(valueParam);
            setVoidReasonDescription(type);
        } else {
            type.setValue(valueParam);
        }
        return type;
    }

    public IdentifierType setDocumentSerialID(@Nullable final String valueParam) {
        IdentifierType type = getDocumentSerialID();
        if (type == null) {
            type = new IdentifierType(valueParam);
            setDocumentSerialID(type);
        } else {
            type.setValue(valueParam);
        }
        return type;
    }

    public IdentifierType setDocumentNumberID(@Nullable final String valueParam) {
        IdentifierType type = getDocumentNumberID();
        if (type == null) {
            type = new IdentifierType(valueParam);
            setDocumentNumberID(type);
        } else {
            type.setValue(valueParam);
        }
        return type;
    }

}
