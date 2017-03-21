package org.openfact.pe.ubl.ubl21.debitnote;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.types.DocumentType;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.debitnote.UBLDebitNoteIDGenerator;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("CREDIT_NOTE")
public class SunatUBLDebitNoteIDGenerator extends AbstractDebitNoteProvider implements UBLDebitNoteIDGenerator {

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public String generateID(OrganizationModel organization, Object o) {
        DebitNoteType debitNoteType = resolve(o);

        String invoiceDocumentReferenceID = null;
        if (debitNoteType.getDiscrepancyResponseCount() > 0) {
            invoiceDocumentReferenceID = debitNoteType.getDiscrepancyResponse().get(0).getReferenceIDValue();
        } else if (debitNoteType.getBillingReferenceCount() > 0) {
            invoiceDocumentReferenceID = debitNoteType.getBillingReference().get(0).getInvoiceDocumentReference().getIDValue();
        }

        TipoComprobante tipoComprobante = TipoComprobante.NOTA_DEBITO;

        DocumentModel lastDebitNote = null;
        ScrollModel<DocumentModel> debitNotes = documentProvider.createQuery(organization)
                .documentType(DocumentType.DEBIT_NOTE)
                .entityQuery()
                .orderByDesc(DocumentModel.DOCUMENT_ID)
                .resultScroll()
                .getScrollResult(10);

        Iterator<DocumentModel> iterator = debitNotes.iterator();

        Pattern pattern = Pattern.compile(tipoComprobante.getMask());
        while (iterator.hasNext()) {
            DocumentModel debitNote = iterator.next();
            String documentId = debitNote.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
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

        int nextNumber = SunatMarshallerUtils.getNextNumber(number, 99_999_999);
        int nextSeries = SunatMarshallerUtils.getNextSerie(series, number, 999, 99_999_999);

        StringBuilder documentId = new StringBuilder();
        documentId.append(invoiceDocumentReferenceID.substring(0, 1));
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }
}
