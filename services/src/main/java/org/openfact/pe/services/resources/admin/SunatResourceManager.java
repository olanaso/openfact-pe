package org.openfact.pe.services.resources.admin;

import org.jboss.logging.Logger;
import org.openfact.models.*;
import org.openfact.models.types.DestinyType;
import org.openfact.models.types.SendEventStatus;
import org.openfact.services.ModelErrorResponseException;
import org.openfact.services.managers.DocumentManager;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Stateless
public class SunatResourceManager {

    private static final Logger logger = Logger.getLogger(SunatResourceManager.class);

    @Inject
    private DocumentProvider documentProvider;

    @Inject
    private DocumentManager documentManager;

    @Inject
    private OrganizationProvider organizationProvider;

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendDocumentToThirdParty(String organizationId, String documentId) throws ModelErrorResponseException {
        OrganizationModel organization = organizationProvider.getOrganization(organizationId);
        DocumentModel document = documentProvider.getDocumentById(documentId, organization);
        try {
            documentManager.sendToThirdParty(organization, document);
        } catch (ModelInsuficientData e) {
            throw new ModelErrorResponseException(e.getMessage(), Response.Status.BAD_REQUEST);
        } catch (SendEventException e) {
            logger.error(e);
        }
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendDocumentToCustomer(String organizationId, String documentId) throws ModelErrorResponseException {
        OrganizationModel organization = organizationProvider.getOrganization(organizationId);
        DocumentModel document = documentProvider.getDocumentById(documentId, organization);
        try {
            documentManager.sendToCustomerParty(organization, document);
        } catch (ModelInsuficientData e) {
            logger.error(e.getMessage(), e);
            SendEventModel sendEventModel = document.addSendEvent(DestinyType.CUSTOMER);
            sendEventModel.setResult(SendEventStatus.ERROR);
            sendEventModel.setDescription(e.getMessage());
        } catch (SendEventException e) {
            logger.error(e.getMessage(), e);
            SendEventModel sendEventModel = document.addSendEvent(DestinyType.CUSTOMER);
            sendEventModel.setResult(SendEventStatus.ERROR);
            sendEventModel.setDescription("Error sending email");
        }
    }


}
