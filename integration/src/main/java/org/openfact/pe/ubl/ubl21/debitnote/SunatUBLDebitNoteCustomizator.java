package org.openfact.pe.ubl.ubl21.debitnote;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.models.types.DocumentType;
import org.openfact.models.utils.TypeToModel;
import org.openfact.ubl.ubl21.debitnote.UBLDebitNoteCustomizator;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@UBLProviderType("default")
@UBLDocumentType("DEBIT_NOTE")
public class SunatUBLDebitNoteCustomizator extends AbstractDebitNoteProvider implements UBLDebitNoteCustomizator {

    @Inject
    private TypeToModel typeToModel;

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public void config(OrganizationModel organization, DocumentModel document, Object o) {
        DebitNoteType debitNoteType = resolve(o);

        typeToModel.importDebitNote(organization, document, debitNoteType);

        // attach file
        if (debitNoteType.getBillingReference() != null && !debitNoteType.getBillingReference().isEmpty()) {
            debitNoteType.getBillingReference().stream()
                    .filter(p -> p.getInvoiceDocumentReference() != null)
                    .forEach(c -> {
                        String invoiceDocumentId = c.getInvoiceDocumentReference().getIDValue();
                        DocumentModel attachedDocument = documentProvider.getDocumentByTypeAndDocumentId(DocumentType.INVOICE, invoiceDocumentId, organization);
                        if (attachedDocument != null) {
                            document.addAttachedDocument(attachedDocument);
                        }
                    });
        }
    }
}
