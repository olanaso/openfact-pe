package org.openfact.pe.spi;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.ubl.DebitNoteIDGeneratorProvider;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;

public class SunatDebitNoteIDGeneratorProvider implements DebitNoteIDGeneratorProvider {

    protected OpenfactSession session;

    public SunatDebitNoteIDGeneratorProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public String generateID(OrganizationModel organization, DebitNoteType debitNoteType) {
        CodigoTipoDocumento debitNoteCode = CodigoTipoDocumento.NOTA_DEBITO;
        
        DebitNoteModel lastDebitNote = null;
        ScrollModel<DebitNoteModel> invoices = session.debitNotes().getDebitNotesScroll(organization, false, 4, 2);
        Iterator<DebitNoteModel> iterator = invoices.iterator();
       
        Pattern pattern = Pattern.compile(debitNoteCode.getMask());        
        while (iterator.hasNext()) {
            DebitNoteModel invoice = iterator.next();
            String documentId = invoice.getDocumentId();
            
            Matcher matcher = pattern.matcher(documentId);            
            if(matcher.find()) {
                lastDebitNote = invoice;
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

}
