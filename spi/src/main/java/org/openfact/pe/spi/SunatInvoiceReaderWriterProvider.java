package org.openfact.pe.spi;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.ubl.InvoiceReaderWriterProvider;
import org.w3c.dom.Document;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public class SunatInvoiceReaderWriterProvider implements InvoiceReaderWriterProvider {

    protected OpenfactSession session;

    public SunatInvoiceReaderWriterProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public InvoiceType read(byte[] bytes) {        
        return UBL21Reader.invoice().read(bytes);
    }

    @Override
    public Document writeAsDocument(OrganizationModel organization, InvoiceType invoice, Map<String, String> attributes) {
        try {
            MapBasedNamespaceContext mapBasedNamespace = SunatUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2");
            ByteArrayOutputStream out = new ByteArrayOutputStream();            
            MicroWriter.writeToStream(UBL21Writer.invoice().getAsMicroDocument(invoice), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));
            
            Document document = DocumentUtils.byteToDocument(out.toByteArray());                        
            return document;
        } catch (DatatypeConfigurationException e) {            
            throw new ModelException(e);
        } catch (Exception e) {            
            throw new ModelException(e);
        }
    }    

}
