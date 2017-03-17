package org.openfact.pe.ubl.ubl21.creditnote;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;
import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.creditnote.UBLCreditNoteReaderWriter;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.w3c.dom.Document;

import javax.ejb.Stateless;
import java.io.ByteArrayOutputStream;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("CREDIT_NOTE")
public class SunatUBLCreditNoteReaderWriter implements UBLCreditNoteReaderWriter {

    @Override
    public UBLReader<CreditNoteType> reader() {
        return new UBLReader<CreditNoteType>() {
            @Override
            public CreditNoteType read(Document document) {
                return UBL21Reader.creditNote().read(document);
            }

            @Override
            public CreditNoteType read(byte[] bytes) {
                return UBL21Reader.creditNote().read(bytes);
            }
        };
    }

    @Override
    public UBLWriter<CreditNoteType> writer() {
        return (organizationModel, creditNoteType) -> {
            try {
                MapBasedNamespaceContext mapBasedNamespace = SunatMarshallerUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl21:schema:xsd:CreditNote-2");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                MicroWriter.writeToStream(UBL21Writer.creditNote().getAsMicroDocument(creditNoteType), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));

                return DocumentUtils.byteToDocument(out.toByteArray());
            } catch (Exception e) {
                throw new ModelException(e);
            }
        };
    }

}
