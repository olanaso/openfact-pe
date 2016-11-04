package org.openfact.ubl.pe;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.transform.dom.DOMResult;

import org.apache.commons.lang.ArrayUtils;
import org.jboss.logging.Logger;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ubl.pe.PerceptionModel;
import org.openfact.models.ubl.pe.RetentionModel;
import org.openfact.models.ubl.pe.SummaryDocumentsModel;
import org.openfact.models.ubl.pe.VoidedDocumentsModel;
import org.openfact.services.util.pe.ModelToType_PE;
import org.openfact.ubl.pe.sunat.PerceptionType;
import org.openfact.ubl.pe.sunat.RetentionType;
import org.openfact.ubl.pe.sunat.SummaryDocumentsType;
import org.openfact.ubl.pe.sunat.SunatFactory;
import org.openfact.ubl.pe.sunat.VoidedDocumentsType;
import org.w3c.dom.Document;

public class UblGenerator_PE {
	private static final Logger log = Logger.getLogger(UblGenerator_PE.class);

	protected OpenfactSession session;

	public UblGenerator_PE(OpenfactSession session) {
		this.session = session;
	}

	public Document generate(OrganizationModel organization, PerceptionModel perceptionModel) throws Exception {
		PerceptionType perceptionType = ModelToType_PE.toType(perceptionModel);
		SunatFactory factory = new SunatFactory();
		JAXBContext context = JAXBContext.newInstance(SunatFactory.class);
		Marshaller marshallerElement = context.createMarshaller();
		JAXBElement<PerceptionType> jaxbElement = factory.createPerception(perceptionType);
		DOMResult res = new DOMResult();
		marshallerElement.marshal(jaxbElement, res);
		// Element element = ((Document) res.getNode()).getDocumentElement();
		Document document = ((Document) res.getNode()).getOwnerDocument();
		// Sign new Document
		Document ubl = UblSignature_PE.signUblDocument(session, organization, document);
		// validate signature
		if (UblSignature_PE.isSignUblDocumentValid(session, organization, ubl)) {
			perceptionModel.setXmlDocument(ArrayUtils.toObject(DocumentUtils.getBytesFromDocument(ubl)));
		} else {
			throw new ModelException("Signature invalid, please verify the invoice signature");
		}
		return ubl;
	}

	public Document generate(OrganizationModel organization, RetentionModel retentionModel) throws Exception {
		RetentionType retentionType = ModelToType_PE.toType(retentionModel);
		SunatFactory factory = new SunatFactory();
		JAXBContext context = JAXBContext.newInstance(SunatFactory.class);
		Marshaller marshallerElement = context.createMarshaller();
		JAXBElement<RetentionType> jaxbElement = factory.createRetention(retentionType);
		DOMResult res = new DOMResult();
		marshallerElement.marshal(jaxbElement, res);
		// Element element = ((Document) res.getNode()).getDocumentElement();
		Document document = ((Document) res.getNode()).getOwnerDocument();
		// Sign new Document
		Document ubl = UblSignature_PE.signUblDocument(session, organization, document);
		// validate signature
		if (UblSignature_PE.isSignUblDocumentValid(session, organization, ubl)) {
			retentionModel.setXmlDocument(ArrayUtils.toObject(DocumentUtils.getBytesFromDocument(ubl)));
		} else {
			throw new ModelException("Signature invalid, please verify the invoice signature");
		}
		return ubl;
	}

	public Document generate(OrganizationModel organization, SummaryDocumentsModel summaryDocumentsModel)
			throws Exception {
		SummaryDocumentsType summaryDocumentsType = ModelToType_PE.toType(summaryDocumentsModel);
		SunatFactory factory = new SunatFactory();
		JAXBContext context = JAXBContext.newInstance(SunatFactory.class);
		Marshaller marshallerElement = context.createMarshaller();
		JAXBElement<SummaryDocumentsType> jaxbElement = factory.createSummaryDocuments(summaryDocumentsType);
		DOMResult res = new DOMResult();
		marshallerElement.marshal(jaxbElement, res);
		// Element element = ((Document) res.getNode()).getDocumentElement();
		Document document = ((Document) res.getNode()).getOwnerDocument();
		// Sign new Document
		Document ubl = UblSignature_PE.signUblDocument(session, organization, document);
		// validate signature
		if (UblSignature_PE.isSignUblDocumentValid(session, organization, ubl)) {
			summaryDocumentsModel.setXmlDocument(ArrayUtils.toObject(DocumentUtils.getBytesFromDocument(ubl)));
		} else {
			throw new ModelException("Signature invalid, please verify the invoice signature");
		}
		return ubl;
	}

	public Document generate(OrganizationModel organization, VoidedDocumentsModel voidedDocumentsModel)
			throws Exception {
		VoidedDocumentsType voidedDocumentsType = ModelToType_PE.toType(voidedDocumentsModel);
		SunatFactory factory = new SunatFactory();
		JAXBContext context = JAXBContext.newInstance(SunatFactory.class);
		Marshaller marshallerElement = context.createMarshaller();
		JAXBElement<VoidedDocumentsType> jaxbElement = factory.createVoidedDocuments(voidedDocumentsType);
		DOMResult res = new DOMResult();
		marshallerElement.marshal(jaxbElement, res);
		// Element element = ((Document) res.getNode()).getDocumentElement();
		Document document = ((Document) res.getNode()).getOwnerDocument();
		// Sign new Document
		Document ubl = UblSignature_PE.signUblDocument(session, organization, document);
		// validate signature
		if (UblSignature_PE.isSignUblDocumentValid(session, organization, ubl)) {
			voidedDocumentsModel.setXmlDocument(ArrayUtils.toObject(DocumentUtils.getBytesFromDocument(ubl)));
		} else {
			throw new ModelException("Signature invalid, please verify the invoice signature");
		}
		return ubl;
	}
}
