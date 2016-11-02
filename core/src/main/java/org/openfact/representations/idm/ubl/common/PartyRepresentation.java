package org.openfact.representations.idm.ubl.common;

import java.util.ArrayList;
import java.util.List;

public class PartyRepresentation {
    private Boolean markCareIndicator;
    private Boolean markAttentionIndicator;
    private String websiteURI;
    private String logoReferenceID;
    private String endpointID;
    private List<String> partyIdentification;
    private List<String> partyName;
    private LanguageRepresentation language;
    private AddressRepresentation postalAddress;
    private LocationCommAggRepresentation physicalLocation;
    private List<PartyTaxSchemeRepresentation> partyTaxScheme;
    private List<PartyLegalEntityRepresentation> partyLegalEntity;
    private ContactRepresentation contact;
    private PersonRepresentation person;
    private PartyRepresentation agentParty;
    private String id;

    /**/
    public void addPartyLegalEntity(PartyLegalEntityRepresentation representation) {
        if (partyLegalEntity == null) {
            partyLegalEntity = new ArrayList<>();
        }
        partyLegalEntity.add(representation);
    }

    public void addPartyTaxSchemeEntity(PartyTaxSchemeRepresentation representation) {
        if (partyTaxScheme == null) {
            partyTaxScheme = new ArrayList<>();
        }
        partyTaxScheme.add(representation);
    }

    public void addPartyName(String representation) {
        if (partyName == null) {
            partyName = new ArrayList<>();
        }
        partyName.add(representation);
    }

    public void addPartyIdentification(String representation) {
        if (partyIdentification == null) {
            partyIdentification = new ArrayList<>();
        }
        partyIdentification.add(representation);
    }
    /**/

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PartyRepresentation getAgentParty() {
        return this.agentParty;
    }

    public void setAgentParty(PartyRepresentation agentParty) {
        this.agentParty = agentParty;
    }

    public List<PartyLegalEntityRepresentation> getPartyLegalEntity() {
        return this.partyLegalEntity;
    }

    public void setPartyLegalEntity(List<PartyLegalEntityRepresentation> partyLegalEntity) {
        this.partyLegalEntity = partyLegalEntity;
    }

    public Boolean getMarkAttentionIndicator() {
        return this.markAttentionIndicator;
    }

    public void setMarkAttentionIndicator(Boolean markAttentionIndicator) {
        this.markAttentionIndicator = markAttentionIndicator;
    }

    public String getEndpointID() {
        return this.endpointID;
    }

    public void setEndpointID(String endpointID) {
        this.endpointID = endpointID;
    }

    public PersonRepresentation getPerson() {
        return this.person;
    }

    public void setPerson(PersonRepresentation person) {
        this.person = person;
    }

    public Boolean getMarkCareIndicator() {
        return this.markCareIndicator;
    }

    public void setMarkCareIndicator(Boolean markCareIndicator) {
        this.markCareIndicator = markCareIndicator;
    }

    public String getLogoReferenceID() {
        return this.logoReferenceID;
    }

    public void setLogoReferenceID(String logoReferenceID) {
        this.logoReferenceID = logoReferenceID;
    }

    public List<String> getPartyName() {
        return this.partyName;
    }

    public void setPartyName(List<String> partyName) {
        this.partyName = partyName;
    }

    public String getWebsiteURI() {
        return this.websiteURI;
    }

    public void setWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }

    public ContactRepresentation getContact() {
        return this.contact;
    }

    public void setContact(ContactRepresentation contact) {
        this.contact = contact;
    }

    public LocationCommAggRepresentation getPhysicalLocation() {
        return this.physicalLocation;
    }

    public void setPhysicalLocation(LocationCommAggRepresentation physicalLocation) {
        this.physicalLocation = physicalLocation;
    }

    public List<PartyTaxSchemeRepresentation> getPartyTaxScheme() {
        return this.partyTaxScheme;
    }

    public void setPartyTaxScheme(List<PartyTaxSchemeRepresentation> partyTaxScheme) {
        this.partyTaxScheme = partyTaxScheme;
    }

    public List<String> getPartyIdentification() {
        return this.partyIdentification;
    }

    public void setPartyIdentification(List<String> partyIdentification) {
        this.partyIdentification = partyIdentification;
    }

    public LanguageRepresentation getLanguage() {
        return this.language;
    }

    public void setLanguage(LanguageRepresentation language) {
        this.language = language;
    }

    public AddressRepresentation getPostalAddress() {
        return this.postalAddress;
    }

    public void setPostalAddress(AddressRepresentation postalAddress) {
        this.postalAddress = postalAddress;
    }
}