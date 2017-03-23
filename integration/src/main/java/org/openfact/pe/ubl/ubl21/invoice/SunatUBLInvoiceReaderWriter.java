package org.openfact.pe.ubl.ubl21.invoice;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.models.ModelRuntimeException;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.invoice.UBLInvoiceReaderWriter;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.w3c.dom.Document;

import javax.ejb.Stateless;
import java.io.ByteArrayOutputStream;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("INVOICE")
public class SunatUBLInvoiceReaderWriter implements UBLInvoiceReaderWriter {

    @Override
    public UBLReader<InvoiceType> reader() {
        return new UBLReader<InvoiceType>() {
            @Override
            public InvoiceType read(Document document) {
                return UBL21Reader.invoice().read(document);
            }

            @Override
            public InvoiceType read(byte[] bytes) {
                return UBL21Reader.invoice().read(bytes);
            }
        };
    }

    @Override
    public UBLWriter<InvoiceType> writer() {
        return (organizationModel, invoiceType) -> {
            try {
                MapBasedNamespaceContext mapBasedNamespace = SunatMarshallerUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl21:schema:xsd:Invoice-2");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                MicroWriter.writeToStream(UBL21Writer.invoice().getAsMicroDocument(invoiceType), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));

                return DocumentUtils.byteToDocument(out.toByteArray());
            } catch (Exception e) {
                throw new ModelRuntimeException(e);
            }
        };
    }

}
