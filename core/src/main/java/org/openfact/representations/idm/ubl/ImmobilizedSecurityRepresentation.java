package org.openfact.representations.idm.ubl;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.openfact.representations.idm.ubl.type.IdentifierRepresentation;
import org.openfact.representations.idm.ubl.type.QuantityRepresentation;

/**
 * A class to describe an immobilized security to be used as a guarantee.
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:15:32 a. m.
 */
public class ImmobilizedSecurityRepresentation {

    /**
     * The value of the security on the day it was immobilized.
     */
    private BigDecimal faceValueAmount;
    /**
     * An identifier for the certificate of this immobilized security.
     */
    private IdentifierRepresentation immobilizationCertificateID;
    /**
     * The date on which this immobilized security was issued.
     */
    private LocalDate issueDate;
    /**
     * The current market value of the immobilized security.
     */
    private BigDecimal marketValueAmount;
    /**
     * An identifier for the security being immobilized.
     */
    private IdentifierRepresentation securityID;
    /**
     * The number of shares immobilized.
     */
    private QuantityRepresentation sharesNumberQuantity;
    private PartyRepresentation issuerParty;

}
