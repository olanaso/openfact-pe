package org.openfact.pe.ubl.ubl21.creditnote;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.types.DocumentType;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.ubl.ubl21.creditnote.UBLCreditNoteIDGenerator;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@UBLProviderType("sunat")
@UBLDocumentType("CREDIT_NOTE")
public class SunatUBLCreditNoteIDGenerator extends AbstractCreditNoteProvider implements UBLCreditNoteIDGenerator {

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public String generateID(OrganizationModel organization, Object o) {
        CreditNoteType creditNoteType = resolve(o);

        String invoiceDocumentReferenceID = null;
        if (creditNoteType.getDiscrepancyResponseCount() > 0) {
            invoiceDocumentReferenceID = creditNoteType.getDiscrepancyResponse().get(0).getReferenceIDValue();
        } else if (creditNoteType.getBillingReferenceCount() > 0) {
            invoiceDocumentReferenceID = creditNoteType.getBillingReference().get(0).getInvoiceDocumentReference().getIDValue();
        }

        TipoComprobante tipoComprobante = TipoComprobante.NOTA_CREDITO;

        DocumentModel lastCreditNote = null;
        ScrollModel<DocumentModel> creditNotes = documentProvider.createQuery(organization)
                .documentType(DocumentType.CREDIT_NOTE)
                .entityQuery()
                .orderByDesc(DocumentModel.DOCUMENT_ID)
                .resultScroll()
                .getScrollResult(10);

        Iterator<DocumentModel> iterator = creditNotes.iterator();

        Pattern pattern = Pattern.compile(tipoComprobante.getMask());
        while (iterator.hasNext()) {
            DocumentModel creditNote = iterator.next();
            String documentId = creditNote.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
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
