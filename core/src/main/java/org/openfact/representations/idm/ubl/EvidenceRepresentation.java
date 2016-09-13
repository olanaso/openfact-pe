package org.openfact.representations.idm.ubl;

import java.util.ArrayList;
import java.util.List;

import org.openfact.representations.idm.ubl.type.CodeRepresentation;
import org.openfact.representations.idm.ubl.type.IdentifierRepresentation;
import org.openfact.representations.idm.ubl.type.TextRepresentation;

/**
 * A class to describe an item of evidentiary support for representations of
 * capabilities or the ability to meet tendering requirements, which an economic
 * operator must provide for acceptance into a tendering process.
 * 
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:15:01 a. m.
 */
public class EvidenceRepresentation {

    /**
     * Information about a candidate statement that the contracting authority
     * accepts as a sufficient response.
     */
    private TextRepresentation candidateStatement;
    /**
     * The textual description for this Evidence.
     */
    private TextRepresentation description;
    /**
     * A code signifying the type of evidence.
     */
    private CodeRepresentation evidenceTypeCode;
    /**
     * An identifier for this item of evidentiary support.
     */
    private IdentifierRepresentation ID;
    private List<DocumentReferenceRepresentation> documentReferences = new ArrayList<>();
    private List<LanguageRepresentation> languages = new ArrayList<>();
    private PartyRepresentation evidenceIssuingParty;

}
