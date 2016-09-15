package org.openfact.models.ubl;

import org.openfact.models.ubl.type.*;

/**
 * A class to define an identifier for a party.
 * 
 * @author Erik
 * @version 1.0
 * @created 07-Set.-2016 9:16:26 a. m.
 */
public interface PartyIdentificationModel {

    IdentifierModel getID();

    void setID(IdentifierModel ID);

}