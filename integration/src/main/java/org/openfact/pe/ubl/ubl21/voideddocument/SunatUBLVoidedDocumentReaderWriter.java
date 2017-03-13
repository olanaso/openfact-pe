package org.openfact.pe.ubl.ubl21.voideddocument;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.pe.ubl.ubl21.factories.SunatDocumentToType;
import org.openfact.pe.ubl.ubl21.factories.SunatTypeToDocument;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsType;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;
import org.w3c.dom.Document;

import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;

@Stateless
@UBLProviderType("sunat")
@UBLDocumentType("VOIDED_DOCUMENTS")
public class SunatUBLVoidedDocumentReaderWriter implements UBLVoidedDocumentReaderWriter {

    @Override
    public UBLReader<VoidedDocumentsType> reader() {
        return new UBLReader<VoidedDocumentsType>() {
            @Override
            public VoidedDocumentsType read(Document document) {
                return SunatDocumentToType.toVoidedDocumentsType(document);
            }

            @Override
            public VoidedDocumentsType read(byte[] bytes) {
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
    public UBLWriter<VoidedDocumentsType> writer() {
        return (organization, voidedDocumentsType) -> {
            try {
                return SunatTypeToDocument.toDocument(organization, voidedDocumentsType);
            } catch (JAXBException e) {
                throw new ModelException(e);
            }
        };
    }

}
