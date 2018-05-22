package org.openfact.pe.ubl.ubl21.factories;

import org.openfact.models.ModelRuntimeException;
import org.openfact.pe.ubl.ubl21.perception.PerceptionFactory;
import org.openfact.pe.ubl.ubl21.perception.PerceptionType;
import org.openfact.pe.ubl.ubl21.retention.RetentionFactory;
import org.openfact.pe.ubl.ubl21.retention.RetentionType;
import org.openfact.pe.ubl.ubl21.summary.SummaryDocumentFactory;
import org.openfact.pe.ubl.ubl21.summary.SummaryDocumentsType;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentFactory;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsType;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class SunatDocumentToType {

    public static VoidedDocumentsType toVoidedDocumentsType(Document document) {
        try {
            JAXBContext factory = JAXBContext.newInstance(VoidedDocumentFactory.class);
            Unmarshaller unmarshal = factory.createUnmarshaller();

            @SuppressWarnings("unchecked")
            JAXBElement<VoidedDocumentsType> jaxbVoidedDocumentsType = (JAXBElement<VoidedDocumentsType>) unmarshal.unmarshal(document);
            VoidedDocumentsType voidedDocumentsType = jaxbVoidedDocumentsType.getValue();
            return voidedDocumentsType;
        } catch (JAXBException e) {
            throw new ModelRuntimeException(e);
        }
    }

    public static RetentionType toRetentionType(Document document) {
        try {
            JAXBContext factory = JAXBContext.newInstance(RetentionFactory.class);
            Unmarshaller unmarshal = factory.createUnmarshaller();

            @SuppressWarnings("unchecked")
            JAXBElement<RetentionType> jaxbRetentionType = (JAXBElement<RetentionType>) unmarshal.unmarshal(document);
            RetentionType retentionType = jaxbRetentionType.getValue();
            return retentionType;
        } catch (JAXBException e) {
            throw new ModelRuntimeException(e);
        }
    }

    public static SummaryDocumentsType toSummaryDocumentsType(Document document) {
        try {
            JAXBContext factory = JAXBContext.newInstance(SummaryDocumentFactory.class);
            Unmarshaller unmarshal = factory.createUnmarshaller();

            @SuppressWarnings("unchecked")
            JAXBElement<SummaryDocumentsType> jaxbSummaryDocumentsType = (JAXBElement<SummaryDocumentsType>) unmarshal.unmarshal(document);
            SummaryDocumentsType summaryDocumentsType = jaxbSummaryDocumentsType.getValue();
            return summaryDocumentsType;
        } catch (JAXBException e) {
            throw new ModelRuntimeException(e);
        }
    }

    public static PerceptionType toPerceptionType(Document document) {
        try {
            JAXBContext factory = JAXBContext.newInstance(PerceptionFactory.class);
            Unmarshaller unmarshal = factory.createUnmarshaller();


            @SuppressWarnings("unchecked")
            JAXBElement<PerceptionType> jaxbPerceptionType = (JAXBElement<PerceptionType>) unmarshal.unmarshal(document);
            PerceptionType perceptionType = jaxbPerceptionType.getValue();
            return perceptionType;
        } catch (JAXBException e) {
            throw new ModelRuntimeException(e);
        }
    }
}
