package org.openfact.pe.models.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.openfact.models.ModelException;
import org.openfact.pe.model.types.PerceptionType;
import org.openfact.pe.model.types.RetentionType;
import org.openfact.pe.model.types.SummaryDocumentsType;
import org.openfact.pe.model.types.SunatFactory;
import org.openfact.pe.model.types.VoidedDocumentsType;
import org.w3c.dom.Document;

public class SunatDocumentToType {
	private static JAXBContext factory;
	private static Unmarshaller unmarshal;

	public static VoidedDocumentsType toVoidedDocumentsType(Document document) {
		try {
			if (factory == null) {
				factory = JAXBContext.newInstance(SunatFactory.class);
			}
			if (unmarshal == null) {
				unmarshal = factory.createUnmarshaller();
			}
			@SuppressWarnings("unchecked")
			JAXBElement<VoidedDocumentsType> jaxbVoidedDocumentsType = (JAXBElement<VoidedDocumentsType>) unmarshal
					.unmarshal(document);
			VoidedDocumentsType voidedDocumentsType = jaxbVoidedDocumentsType.getValue();
			return voidedDocumentsType;
		} catch (JAXBException e) {
			throw new ModelException(e);
		}
	}

	public static RetentionType toRetentionType(Document document) {
		try {
			if (factory == null) {
				factory = JAXBContext.newInstance(SunatFactory.class);
			}
			if (unmarshal == null) {
				unmarshal = factory.createUnmarshaller();
			}
			@SuppressWarnings("unchecked")
			JAXBElement<RetentionType> jaxbRetentionType = (JAXBElement<RetentionType>) unmarshal.unmarshal(document);
			RetentionType retentionType = jaxbRetentionType.getValue();
			return retentionType;
		} catch (JAXBException e) {
			throw new ModelException(e);
		}
	}

	public static SummaryDocumentsType toSummaryDocumentsType(Document document) {
		try {
			if (factory == null) {
				factory = JAXBContext.newInstance(SunatFactory.class);
			}
			if (unmarshal == null) {
				unmarshal = factory.createUnmarshaller();
			}
			@SuppressWarnings("unchecked")
			JAXBElement<SummaryDocumentsType> jaxbSummaryDocumentsType = (JAXBElement<SummaryDocumentsType>) unmarshal
					.unmarshal(document);
			SummaryDocumentsType summaryDocumentsType = jaxbSummaryDocumentsType.getValue();
			return summaryDocumentsType;
		} catch (JAXBException e) {
			throw new ModelException(e);
		}
	}

	public static PerceptionType toPerceptionType(Document document) {
		try {
			if (factory == null) {
				factory = JAXBContext.newInstance(SunatFactory.class);
			}
			if (unmarshal == null) {
				unmarshal = factory.createUnmarshaller();
			}
			@SuppressWarnings("unchecked")
			JAXBElement<PerceptionType> jaxbPerceptionType = (JAXBElement<PerceptionType>) unmarshal
					.unmarshal(document);
			PerceptionType perceptionType = jaxbPerceptionType.getValue();
			return perceptionType;
		} catch (JAXBException e) {
			throw new ModelException(e);
		}
	}
}