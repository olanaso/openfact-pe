package org.openfact.models.ubl;

import java.util.ArrayList;
import java.util.List;

import org.openfact.models.ubl.type.*;

/**
 * A class to describe purchasing, sales, or payment conditions.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:18:35 a. m.
 */
public class TransactionConditionsModel {

    /**
     * A code signifying a type of action relating to sales or payment
     * conditions.
     */
    private CodeModel actionCode;
    /**
     * Text describing the transaction conditions.
     */
    private TextModel description;
    /**
     * An identifier for conditions of the transaction, typically purchase/sales
     * conditions.
     */
    private IdentifierModel ID;
    private List<DocumentReferenceModel> documentReferences = new ArrayList<>();

}
