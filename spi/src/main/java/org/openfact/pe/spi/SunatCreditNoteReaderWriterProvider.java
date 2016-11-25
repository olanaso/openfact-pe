package org.openfact.pe.spi;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.ubl.CreditNoteReaderWriterProvider;
import org.w3c.dom.Document;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;

public class SunatCreditNoteReaderWriterProvider implements CreditNoteReaderWriterProvider {

    protected OpenfactSession session;

    public SunatCreditNoteReaderWriterProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public CreditNoteType read(byte[] bytes) {        
        return UBL21Reader.creditNote().read(bytes);
    }

    @Override
    public Document writeAsDocument(OrganizationModel organization, CreditNoteType creditNote, Map<String, String> attributes) {
        try {
            MapBasedNamespaceContext mapBasedNamespace = SunatUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2");
            ByteArrayOutputStream out = new ByteArrayOutputStream();            
            MicroWriter.writeToStream(UBL21Writer.creditNote().getAsMicroDocument(creditNote), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));
            
            Document document = DocumentUtils.byteToDocument(out.toByteArray());                        
            return document;
        } catch (DatatypeConfigurationException e) {            
            throw new ModelException(e);
        } catch (Exception e) {            
            throw new ModelException(e);
        }
    }   

}
