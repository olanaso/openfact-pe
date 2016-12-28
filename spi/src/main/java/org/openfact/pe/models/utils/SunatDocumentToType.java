package org.openfact.pe.models.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.openfact.models.ModelException;
import org.openfact.pe.models.types.perception.PerceptionFactory;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.types.retention.RetentionFactory;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.pe.models.types.summary.SummaryDocumentFactory;
import org.openfact.pe.models.types.summary.SummaryDocumentsType;
import org.openfact.pe.models.types.voided.VoidedDocumentFactory;
import org.openfact.pe.models.types.voided.VoidedDocumentsType;
import org.w3c.dom.Document;

public class SunatDocumentToType {
	private static JAXBContext factory;
	private static Unmarshaller unmarshal;

	public static VoidedDocumentsType toVoidedDocumentsType(Document document) {
		try {
			if (factory == null) {
				factory = JAXBContext.newInstance(VoidedDocumentFactory.class);
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
				factory = JAXBContext.newInstance(RetentionFactory.class);
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
				factory = JAXBContext.newInstance(SummaryDocumentFactory.class);
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
				factory = JAXBContext.newInstance(PerceptionFactory.class);
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
