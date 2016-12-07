package org.openfact.pe.models.utils;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.pe.model.types.PerceptionType;
import org.openfact.pe.model.types.RetentionType;
import org.openfact.pe.model.types.SummaryDocumentsType;
import org.openfact.pe.model.types.SunatFactory;
import org.openfact.pe.model.types.VoidedDocumentsType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SunatTypeToDocument {

	public static Document toDocument(PerceptionType type) throws JAXBException {
		try {
			SunatFactory factory = new SunatFactory();
			JAXBContext context = JAXBContext.newInstance(SunatFactory.class);
			Marshaller marshallerElement = context.createMarshaller();
			JAXBElement<PerceptionType> jaxbElement = factory.createPerception(type);
			StringWriter xmlWriter = new StringWriter();
			XMLStreamWriter xmlStream = XMLOutputFactory.newInstance().createXMLStreamWriter(xmlWriter);
			xmlStream.setNamespaceContext(SunatUtils
					.getBasedNamespaceContext("urn:sunat:names:specification:ubl:peru:schema:xsd:Perception-1"));
			marshallerElement.marshal(jaxbElement, xmlStream);
			Document document = DocumentUtils.getStringToDocument(xmlWriter.toString());
			return document;
		} catch (XMLStreamException e) {
			throw new ModelException(e);
		} catch (FactoryConfigurationError e) {
			throw new ModelException(e);
		} catch (IOException e) {
			throw new ModelException(e);
		} catch (SAXException e) {
			throw new ModelException(e);
		} catch (ParserConfigurationException e) {
			throw new ModelException(e);
		}
	}

	public static Document toDocument(RetentionType type) throws JAXBException {
		SunatFactory factory = new SunatFactory();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(SunatFactory.class);
			Marshaller marshallerElement = context.createMarshaller();
			JAXBElement<RetentionType> jaxbElement = factory.createRetention(type);
			StringWriter xmlWriter = new StringWriter();
			XMLStreamWriter xmlStream = XMLOutputFactory.newInstance().createXMLStreamWriter(xmlWriter);
			xmlStream.setNamespaceContext(SunatUtils
					.getBasedNamespaceContext("urn:sunat:names:specification:ubl:peru:schema:xsd:Retention-1"));
			marshallerElement.marshal(jaxbElement, xmlStream);
			Document document = DocumentUtils.getStringToDocument(xmlWriter.toString());
			return document;
		} catch (XMLStreamException e) {
			throw new ModelException(e);
		} catch (FactoryConfigurationError e) {
			throw new ModelException(e);
		} catch (IOException e) {
			throw new ModelException(e);
		} catch (SAXException e) {
			throw new ModelException(e);
		} catch (ParserConfigurationException e) {
			throw new ModelException(e);
		}
	}

	public static Document toDocument(SummaryDocumentsType type) throws JAXBException {
		SunatFactory factory = new SunatFactory();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(SunatFactory.class);
			Marshaller marshallerElement = context.createMarshaller();
			JAXBElement<SummaryDocumentsType> jaxbElement = factory.createSummaryDocuments(type);
			StringWriter xmlWriter = new StringWriter();
			XMLStreamWriter xmlStream = XMLOutputFactory.newInstance().createXMLStreamWriter(xmlWriter);
			xmlStream.setNamespaceContext(SunatUtils
					.getBasedNamespaceContext("urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1"));
			marshallerElement.marshal(jaxbElement, xmlStream);
			Document document = DocumentUtils.getStringToDocument(xmlWriter.toString());
			return document;
		} catch (XMLStreamException e) {
			throw new ModelException(e);
		} catch (FactoryConfigurationError e) {
			throw new ModelException(e);
		} catch (IOException e) {
			throw new ModelException(e);
		} catch (SAXException e) {
			throw new ModelException(e);
		} catch (ParserConfigurationException e) {
			throw new ModelException(e);
		}
	}

	public static Document toDocument(VoidedDocumentsType type) throws JAXBException {
		SunatFactory factory = new SunatFactory();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(SunatFactory.class);
			Marshaller marshallerElement = context.createMarshaller();
			JAXBElement<VoidedDocumentsType> jaxbElement = factory.createVoidedDocuments(type);
			StringWriter xmlWriter = new StringWriter();
			XMLStreamWriter xmlStream = XMLOutputFactory.newInstance().createXMLStreamWriter(xmlWriter);
			xmlStream.setNamespaceContext(SunatUtils
					.getBasedNamespaceContext("urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1"));
			marshallerElement.marshal(jaxbElement, xmlStream);
			Document document = DocumentUtils.getStringToDocument(xmlWriter.toString());
			return document;
		} catch (XMLStreamException e) {
			throw new ModelException(e);
		} catch (FactoryConfigurationError e) {
			throw new ModelException(e);
		} catch (IOException e) {
			throw new ModelException(e);
		} catch (SAXException e) {
			throw new ModelException(e);
		} catch (ParserConfigurationException e) {
			throw new ModelException(e);
		}
	}
}
