package org.openfact.pe.ubl.ubl21.factories;


import org.openfact.models.ModelException;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.ubl.ubl21.perception.PerceptionFactory;
import org.openfact.pe.ubl.ubl21.perception.PerceptionType;
import org.openfact.pe.ubl.ubl21.retention.RetentionFactory;
import org.openfact.pe.ubl.ubl21.retention.RetentionType;
import org.openfact.pe.ubl.ubl21.summary.SummaryDocumentFactory;
import org.openfact.pe.ubl.ubl21.summary.SummaryDocumentsType;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentFactory;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;

public class SunatTypeToDocument {

    public static Document toDocument(OrganizationModel organization, PerceptionType type) throws JAXBException {
        try {
            PerceptionFactory factory = new PerceptionFactory();
            JAXBContext context = JAXBContext.newInstance(PerceptionFactory.class);
            Marshaller marshallerElement = context.createMarshaller();
            marshallerElement.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerElement.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
            JAXBElement<PerceptionType> jaxbElement = factory.createPerception(type);
            DOMResult res = new DOMResult();
            marshallerElement.marshal(jaxbElement, res);
            Element element = ((Document) res.getNode()).getDocumentElement();

            Document document = element.getOwnerDocument();
            return document;
        } catch (FactoryConfigurationError e) {
            throw new ModelException(e);
        }
    }

    public static Document toDocument(OrganizationModel organization, RetentionType type) throws JAXBException {
        try {
            RetentionFactory factory = new RetentionFactory();
            JAXBContext context = JAXBContext.newInstance(RetentionFactory.class);

            Marshaller marshallerElement = context.createMarshaller();
            marshallerElement.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerElement.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
            JAXBElement<RetentionType> jaxbElement = factory.createRetention(type);
            DOMResult res = new DOMResult();
            marshallerElement.marshal(jaxbElement, res);
            Element element = ((Document) res.getNode()).getDocumentElement();

            Document document = element.getOwnerDocument();
            return document;

        } catch (FactoryConfigurationError e) {
            throw new ModelException(e);
        }
    }


    public static Document toDocument(OrganizationModel organization, SummaryDocumentsType type) throws JAXBException {

        try {
            SummaryDocumentFactory factory = new SummaryDocumentFactory();
            JAXBContext context = JAXBContext.newInstance(SummaryDocumentFactory.class);
            Marshaller marshallerElement = context.createMarshaller();
            marshallerElement.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerElement.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
            JAXBElement<SummaryDocumentsType> jaxbElement = factory.createSummaryDocuments(type);
            DOMResult res = new DOMResult();
            marshallerElement.marshal(jaxbElement, res);
            Element element = ((Document) res.getNode()).getDocumentElement();

            Document document = element.getOwnerDocument();
            return document;
        } catch (FactoryConfigurationError e) {
            throw new ModelException(e);
        }
    }

    public static Document toDocument(OrganizationModel organization, VoidedDocumentsType type) throws JAXBException {

        try {
            VoidedDocumentFactory factory = new VoidedDocumentFactory();
            JAXBContext context = JAXBContext.newInstance(VoidedDocumentFactory.class);
            Marshaller marshallerElement = context.createMarshaller();
            marshallerElement.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerElement.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
            JAXBElement<VoidedDocumentsType> jaxbElement = factory.createVoidedDocuments(type);
            DOMResult res = new DOMResult();
            marshallerElement.marshal(jaxbElement, res);
            Element element = ((Document) res.getNode()).getDocumentElement();

            Document document = element.getOwnerDocument();
            return document;
        } catch (FactoryConfigurationError e) {
            throw new ModelException(e);
        }
    }
}
