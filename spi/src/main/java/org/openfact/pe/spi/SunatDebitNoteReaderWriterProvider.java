package org.openfact.pe.spi;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.ubl.DebitNoteReaderWriterProvider;
import org.w3c.dom.Document;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;

public class SunatDebitNoteReaderWriterProvider implements DebitNoteReaderWriterProvider {

    protected OpenfactSession session;

    public SunatDebitNoteReaderWriterProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public DebitNoteType read(byte[] bytes) {        
        return UBL21Reader.debitNote().read(bytes);
    }

    @Override
    public Document writeAsDocument(OrganizationModel organization, DebitNoteType debitNote, Map<String, String> attributes) {
        try {
            MapBasedNamespaceContext mapBasedNamespace = SunatUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:DebitNote-2");
            ByteArrayOutputStream out = new ByteArrayOutputStream();            
            MicroWriter.writeToStream(UBL21Writer.debitNote().getAsMicroDocument(debitNote), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));
            
            Document document = DocumentUtils.byteToDocument(out.toByteArray());                        
            return document;
        } catch (DatatypeConfigurationException e) {            
            throw new ModelException(e);
        } catch (Exception e) {            
            throw new ModelException(e);
        }
    }   

}
