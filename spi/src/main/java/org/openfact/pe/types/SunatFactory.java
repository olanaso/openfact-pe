package org.openfact.pe.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class SunatFactory {
    private final static QName _Perception_QNAME = new QName(
            "urn:sunat:names:specification:ubl:peru:schema:xsd:Perception-1", "Perception");
    private final static QName _Retention_QNAME = new QName(
            "urn:sunat:names:specification:ubl:peru:schema:xsd:Retention-1", "Retention");
    private final static QName _VoidedDocuments_QNAME = new QName(
            "urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1", "VoidedDocuments");
    private final static QName _SummaryDocuments_QNAME = new QName(
            "urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1", "SummaryDocuments");

    public SunatFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement
     * }{@code <}{@link AdditionalInformationTypeSunatAgg }{@code >}}
     */
    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:Perception-1", name = "Perception")
    public JAXBElement<PerceptionType> createPerception(PerceptionType value) {
        return new JAXBElement<PerceptionType>(_Perception_QNAME, PerceptionType.class, null, value);
    }

    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:Perception-1", name = "Perception")
    public JAXBElement<RetentionType> createRetention(RetentionType value) {
        return new JAXBElement<RetentionType>(_Retention_QNAME, RetentionType.class, null, value);
    }

    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1", name = "VoidedDocuments")
    public JAXBElement<VoidedDocumentsType> createVoidedDocuments(VoidedDocumentsType value) {
        return new JAXBElement<VoidedDocumentsType>(_VoidedDocuments_QNAME, VoidedDocumentsType.class, null,
                value);
    }

    @XmlElementDecl(namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1", name = "SummaryDocuments")
    public JAXBElement<SummaryDocumentsType> createSummaryDocuments(SummaryDocumentsType value) {
        return new JAXBElement<SummaryDocumentsType>(_SummaryDocuments_QNAME, SummaryDocumentsType.class,
                null, value);
    }
}
