package org.openfact.pe.ubl.ubl21.retention;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.pe.ubl.ubl21.factories.SunatDocumentToType;
import org.openfact.pe.ubl.ubl21.factories.SunatTypeToDocument;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;
import org.w3c.dom.Document;

import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;

@Stateless
@UBLProviderType("sunat")
@UBLDocumentType("RETENTION")
public class SunatUBLRetentionReaderWriter implements UBLRetentionReaderWriter {

    @Override
    public UBLReader<RetentionType> reader() {
        return new UBLReader<RetentionType>() {
            @Override
            public RetentionType read(Document document) {
                return SunatDocumentToType.toRetentionType(document);
            }

            @Override
            public RetentionType read(byte[] bytes) {
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
    public UBLWriter<RetentionType> writer() {
        return (organization, retentionType) -> {
            try {
                return SunatTypeToDocument.toDocument(organization, retentionType);
            } catch (JAXBException e) {
                throw new ModelException(e);
            }
        };
    }

}
