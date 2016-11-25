package org.openfact.pe.spi;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.CreditNoteModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.ubl.CreditNoteIDGeneratorProvider;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;

public class SunatCreditNoteIDGeneratorProvider implements CreditNoteIDGeneratorProvider {

    protected OpenfactSession session;

    public SunatCreditNoteIDGeneratorProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public String generateID(OrganizationModel organization, CreditNoteType creiditNoteType) {
        CodigoTipoDocumento creditNoteCode = CodigoTipoDocumento.NOTA_CREDITO;
        
        CreditNoteModel lastCreditNote = null;
        ScrollModel<CreditNoteModel> creditNotes = session.creditNotes().getCreditNotesScroll(organization, false, 4, 2);
        Iterator<CreditNoteModel> iterator = creditNotes.iterator();
       
        Pattern pattern = Pattern.compile(creditNoteCode.getMask());        
        while (iterator.hasNext()) {
            CreditNoteModel invoice = iterator.next();
            String documentId = invoice.getDocumentId();
            
            Matcher matcher = pattern.matcher(documentId);            
            if(matcher.find()) {
                lastCreditNote = invoice;
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

}
