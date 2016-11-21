package org.openfact.pe.models.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.dom.DOMResult;

import org.openfact.pe.types.PerceptionType;
import org.openfact.pe.types.RetentionType;
import org.openfact.pe.types.SummaryDocumentsType;
import org.openfact.pe.types.SunatFactory;
import org.openfact.pe.types.VoidedDocumentsType;
import org.w3c.dom.Document;

public class TypeToDocument {

    public static Document toDocument(PerceptionType type) throws JAXBException {
        SunatFactory factory = new SunatFactory();
        JAXBContext context = JAXBContext.newInstance(SunatFactory.class);
        Marshaller marshallerElement = context.createMarshaller();
        JAXBElement<PerceptionType> jaxbElement = factory.createPerception(type);
        DOMResult res = new DOMResult();
        marshallerElement.marshal(jaxbElement, res);

        return ((Document) res.getNode()).getOwnerDocument();
    }

    public static Document toDocument(RetentionType type) throws JAXBException {
        SunatFactory factory = new SunatFactory();
        JAXBContext context = JAXBContext.newInstance(SunatFactory.class);
        Marshaller marshallerElement = context.createMarshaller();
        JAXBElement<RetentionType> jaxbElement = factory.createRetention(type);
        DOMResult res = new DOMResult();
        marshallerElement.marshal(jaxbElement, res);

        return ((Document) res.getNode()).getOwnerDocument();
    }

    public static Document toDocument(SummaryDocumentsType type) throws JAXBException {
        SunatFactory factory = new SunatFactory();
        JAXBContext context = JAXBContext.newInstance(SunatFactory.class);
        Marshaller marshallerElement = context.createMarshaller();
        JAXBElement<SummaryDocumentsType> jaxbElement = factory.createSummaryDocuments(type);
        DOMResult res = new DOMResult();
        marshallerElement.marshal(jaxbElement, res);

        return ((Document) res.getNode()).getOwnerDocument();
    }

    public static Document toDocument(VoidedDocumentsType type) throws JAXBException {
        SunatFactory factory = new SunatFactory();
        JAXBContext context = JAXBContext.newInstance(SunatFactory.class);
        Marshaller marshallerElement = context.createMarshaller();
        JAXBElement<VoidedDocumentsType> jaxbElement = factory.createVoidedDocuments(type);
        DOMResult res = new DOMResult();
        marshallerElement.marshal(jaxbElement, res);

        return ((Document) res.getNode()).getOwnerDocument();
    }

}
