package org.openfact.pe.ubl.ubl21.creditnote;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.models.types.DocumentType;
import org.openfact.models.utils.TypeToModel;
import org.openfact.ubl.ubl21.creditnote.UBLCreditNoteCustomizator;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@UBLProviderType("sunat")
@UBLDocumentType("CREDIT_NOTE")
public class SunatUBLCreditNoteCustomizator extends AbstractCreditNoteProvider implements UBLCreditNoteCustomizator {

    @Inject
    private TypeToModel typeToModel;

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public void config(OrganizationModel organization, DocumentModel document, Object o) {
        CreditNoteType creditNoteType = resolve(o);

        typeToModel.importCreditNote(organization, document, creditNoteType);

        // attach file
        if (creditNoteType.getBillingReference() != null && !creditNoteType.getBillingReference().isEmpty()) {
            creditNoteType.getBillingReference().stream()
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
