package org.openfact.pe.spi;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLDebitNoteProvider;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;
import org.w3c.dom.Document;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;

public class SunatUBLDebitNoteProvider implements UBLDebitNoteProvider {

    protected OpenfactSession session;

    public SunatUBLDebitNoteProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public UBLIDGenerator<DebitNoteType> idGenerator() {
        return new UBLIDGenerator<DebitNoteType>() {
            
            @Override
            public void close() {    
            }
            
            @Override
            public String generateID(OrganizationModel organization, DebitNoteType debitNoteType) {
                CodigoTipoDocumento debitNoteCode = CodigoTipoDocumento.NOTA_DEBITO;

                DebitNoteModel lastDebitNote = null;
                ScrollModel<DebitNoteModel> debitNotes = session.debitNotes().getDebitNotesScroll(organization, false, 4, 2);
                Iterator<DebitNoteModel> iterator = debitNotes.iterator();
               
                Pattern pattern = Pattern.compile(debitNoteCode.getMask());        
                while (iterator.hasNext()) {
                    DebitNoteModel debitNote = iterator.next();
                    String documentId = debitNote.getDocumentId();
                    
                    Matcher matcher = pattern.matcher(documentId);            
                    if(matcher.find()) {
                        lastDebitNote = debitNote;
                        break;
                    }
                }
                
                int series = 0;
                int number = 0;
                if (lastDebitNote != null) {
                    String[] splits = lastDebitNote.getDocumentId().split("-");
                    series = Integer.parseInt(splits[0].substring(1));
                    number = Integer.parseInt(splits[1]);
                }

                int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
                int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
                StringBuilder documentId = new StringBuilder();
                documentId.append(debitNoteCode.getMask().substring(0, 1));
                documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
                documentId.append("-");
                documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

                return documentId.toString();
            }
        };
    }

    @Override
    public UBLReader<DebitNoteType> reader() {
        return new UBLReader<DebitNoteType>() {

            @Override
            public void close() {        
            }

            @Override
            public DebitNoteType read(byte[] bytes) {
                return UBL21Reader.debitNote().read(bytes);
            }

            @Override
            public DebitNoteType read(Document document) {
                return UBL21Reader.debitNote().read(document);
            }
            
        };
    }

    @Override
    public UBLWriter<DebitNoteType> writer() {
        return new UBLWriter<DebitNoteType>() {

            @Override
            public void close() {
            }

            @Override
            public Document write(OrganizationModel organization, DebitNoteType debitNoteType, Map<String, String> attributes) {
                try {
                    MapBasedNamespaceContext mapBasedNamespace = SunatUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:DebitNote-2");
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    MicroWriter.writeToStream(UBL21Writer.debitNote().getAsMicroDocument(debitNoteType), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));

                    Document document = DocumentUtils.byteToDocument(out.toByteArray());
                    return document;
                } catch (DatatypeConfigurationException e) {
                    throw new ModelException(e);
                } catch (Exception e) {
                    throw new ModelException(e);
                }
            }

            @Override
            public Document write(OrganizationModel organization, DebitNoteType t) {
                return write(organization, t, Collections.emptyMap());
            }
        };
    }

    @Override
    public UBLSender<DebitNoteModel> sender() {
        // TODO Auto-generated method stub
        return null;
    }

}
