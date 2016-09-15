package org.openfact.models.ubl;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to define a reference to a billing document.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:13:07 a. m.
 */
public class BillingReferenceModel {

    private List<BillingReferenceLineModel> billingReferenceLines = new ArrayList<>();
    private DocumentReferenceModel selfBilledInvoiceDocumentReference;
    private DocumentReferenceModel selfBilledCreditNoteDocumentReference;
    private DocumentReferenceModel reminderDocumentReference;
    private DocumentReferenceModel creditNoteDocumentReference;
    private DocumentReferenceModel debitNoteDocumentReference;
    private DocumentReferenceModel invoiceDocumentReference;
    private DocumentReferenceModel additionalDocumentReference;

}