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
import org.openfact.models.CreditNoteModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLCreditNoteProvider;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;
import org.w3c.dom.Document;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;

public class SunatUBLCreditNoteProvider implements UBLCreditNoteProvider {

    protected OpenfactSession session;

    public SunatUBLCreditNoteProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public UBLIDGenerator<CreditNoteType> idGenerator() {
        return new UBLIDGenerator<CreditNoteType>() {
            
            @Override
            public void close() {    
            }
            
            @Override
            public String generateID(OrganizationModel organization, CreditNoteType creditNoteType) {
                CodigoTipoDocumento creditNoteCode = CodigoTipoDocumento.NOTA_CREDITO;

                CreditNoteModel lastCreditNote = null;
                ScrollModel<CreditNoteModel> creditNotes = session.creditNotes().getCreditNotesScroll(organization, false, 4, 2);
                Iterator<CreditNoteModel> iterator = creditNotes.iterator();
               
                Pattern pattern = Pattern.compile(creditNoteCode.getMask());        
                while (iterator.hasNext()) {
                    CreditNoteModel creditNote = iterator.next();
                    String documentId = creditNote.getDocumentId();
                    
                    Matcher matcher = pattern.matcher(documentId);            
                    if(matcher.find()) {
                        lastCreditNote = creditNote;
                        break;
                    }
                }
                
                int series = 0;
                int number = 0;
                if (lastCreditNote != null) {
                    String[] splits = lastCreditNote.getDocumentId().split("-");
                    series = Integer.parseInt(splits[0].substring(1));
                    number = Integer.parseInt(splits[1]);
                }

                int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
                int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
                StringBuilder documentId = new StringBuilder();
                documentId.append(creditNoteCode.getMask().substring(0, 1));
                documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
                documentId.append("-");
                documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

                return documentId.toString();
            }
        };
    }

    @Override
    public UBLReader<CreditNoteType> reader() {
        return new UBLReader<CreditNoteType>() {

            @Override
            public void close() {        
            }

            @Override
            public CreditNoteType read(byte[] bytes) {
                return UBL21Reader.creditNote().read(bytes);
            }

            @Override
            public CreditNoteType read(Document document) {
                return UBL21Reader.creditNote().read(document);
            }
            
        };
    }

    @Override
    public UBLWriter<CreditNoteType> writer() {
        return new UBLWriter<CreditNoteType>() {

            @Override
            public void close() {
            }

            @Override
            public Document write(OrganizationModel organization, CreditNoteType creditNoteType, Map<String, String> attributes) {
                try {
                    MapBasedNamespaceContext mapBasedNamespace = SunatUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2");
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    MicroWriter.writeToStream(UBL21Writer.creditNote().getAsMicroDocument(creditNoteType), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));

                    Document document = DocumentUtils.byteToDocument(out.toByteArray());
                    return document;
                } catch (DatatypeConfigurationException e) {
                    throw new ModelException(e);
                } catch (Exception e) {
                    throw new ModelException(e);
                }
            }

            @Override
            public Document write(OrganizationModel organization, CreditNoteType t) {
                return write(organization, t, Collections.emptyMap());
            }
        };
    }

    @Override
    public UBLSender<CreditNoteModel> sender() {
        // TODO Auto-generated method stub
        return null;
    }

}
