package org.openfact.pe.ubl.ubl21.perception;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.pe.ubl.ubl21.factories.SunatDocumentToType;
import org.openfact.pe.ubl.ubl21.factories.SunatTypeToDocument;
import org.openfact.ubl.UBLReaderWriter;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;
import org.w3c.dom.Document;

import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;

@Stateless
@UBLProviderType("sunat")
@UBLDocumentType("PERCEPTION")
public class SunatUBLPerceptionReaderWriter implements UBLPerceptionReaderWriter {

    @Override
    public UBLReaderWriter.UBLReader<PerceptionType> reader() {
        return new UBLReader<PerceptionType>() {
            @Override
            public PerceptionType read(Document document) {
                return SunatDocumentToType.toPerceptionType(document);
            }

            @Override
            public PerceptionType read(byte[] bytes) {
                try {
                    Document document = DocumentUtils.byteToDocument(bytes);
                    return read(document);
                } catch (Exception e) {
                    throw new ModelException(e);
                }
            }
        };
    }

    @Override
    public UBLWriter<PerceptionType> writer() {
        return (organization, perceptionType) -> {
            try {
                return SunatTypeToDocument.toDocument(organization, perceptionType);
            } catch (JAXBException e) {
                throw new ModelException(e);
            }
        };
    }

}
