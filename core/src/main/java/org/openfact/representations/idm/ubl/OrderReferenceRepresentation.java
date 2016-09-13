package org.openfact.representations.idm.ubl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.openfact.representations.idm.ubl.type.CodeRepresentation;
import org.openfact.representations.idm.ubl.type.IdentifierRepresentation;
import org.openfact.representations.idm.ubl.type.TextRepresentation;

/**
 * A class to define a reference to an Order.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:16:18 a. m.
 */
public class OrderReferenceRepresentation {

    /**
     * Indicates whether the referenced OrderRepresentation is a copy (true) or
     * the original (false).
     */
    private boolean copyIndicator;
    /**
     * Text used for tagging purchasing card transactions.
     */
    private TextRepresentation customerReference;
    /**
     * An identifier for this order reference, assigned by the buyer.
     */
    private IdentifierRepresentation ID;
    /**
     * The date on which the referenced OrderRepresentation was issued.
     */
    private LocalDate issueDate;
    /**
     * The time at which the referenced OrderRepresentation was issued.
     */
    private LocalTime issueTime;
    /**
     * A code signifying the type of the referenced Order.
     */
    private CodeRepresentation orderTypeCode;
    /**
     * An identifier for this order reference, assigned by the seller.
     */
    private IdentifierRepresentation salesOrderID;
    /**
     * A universally unique identifier for this order reference.
     */
    private IdentifierRepresentation UUID;
    private List<DocumentReferenceRepresentation> documentReferences = new ArrayList<>();

}
