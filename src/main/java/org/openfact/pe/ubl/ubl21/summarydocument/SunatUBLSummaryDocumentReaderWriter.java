package org.openfact.pe.ubl.ubl21.summarydocument;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.models.ModelRuntimeException;
import org.openfact.pe.ubl.ubl21.factories.SunatDocumentToType;
import org.openfact.pe.ubl.ubl21.factories.SunatTypeToDocument;
import org.openfact.pe.ubl.ubl21.summary.SummaryDocumentsType;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.w3c.dom.Document;

import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("SUMMARY_DOCUMENTS")
public class SunatUBLSummaryDocumentReaderWriter implements UBLSummaryDocumentReaderWriter {

    @Override
    public UBLReader<SummaryDocumentsType> reader() {
        return new UBLReader<SummaryDocumentsType>() {
            @Override
            public SummaryDocumentsType read(Document document) {
                return SunatDocumentToType.toSummaryDocumentsType(document);
            }

            @Override
            public SummaryDocumentsType read(byte[] bytes) {
                try {
                    Document document = DocumentUtils.byteToDocument(bytes);
                    return read(document);
                } catch (Exception e) {
                    throw new ModelRuntimeException(e);
                }
            }
        };
    }

    @Override
    public UBLWriter<SummaryDocumentsType> writer() {
        return (organization, summaryDocumentsType) -> {
            try {
                return SunatTypeToDocument.toDocument(organization, summaryDocumentsType);
            } catch (JAXBException e) {
                throw new ModelRuntimeException(e);
            }
        };
    }

}
