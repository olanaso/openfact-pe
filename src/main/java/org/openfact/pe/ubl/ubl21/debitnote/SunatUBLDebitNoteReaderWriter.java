package org.openfact.pe.ubl.ubl21.debitnote;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;
import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.models.ModelRuntimeException;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.debitnote.UBLDebitNoteReaderWriter;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.w3c.dom.Document;

import javax.ejb.Stateless;
import java.io.ByteArrayOutputStream;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("DEBIT_NOTE")
public class SunatUBLDebitNoteReaderWriter implements UBLDebitNoteReaderWriter {

    @Override
    public UBLReader<DebitNoteType> reader() {
        return new UBLReader<DebitNoteType>() {
            @Override
            public DebitNoteType read(Document document) {
                return UBL21Reader.debitNote().read(document);
            }

            @Override
            public DebitNoteType read(byte[] bytes) {
                return UBL21Reader.debitNote().read(bytes);
            }
        };
    }

    @Override
    public UBLWriter<DebitNoteType> writer() {
        return (organizationModel, debitNoteType) -> {
            try {
                MapBasedNamespaceContext mapBasedNamespace = SunatMarshallerUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl21:schema:xsd:DebitNote-2");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                MicroWriter.writeToStream(UBL21Writer.debitNote().getAsMicroDocument(debitNoteType), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));

                return DocumentUtils.byteToDocument(out.toByteArray());
            } catch (Exception e) {
                throw new ModelRuntimeException(e);
            }
        };
    }

}
