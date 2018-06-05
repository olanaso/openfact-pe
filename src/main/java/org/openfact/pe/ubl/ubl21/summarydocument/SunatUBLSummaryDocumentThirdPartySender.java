package org.openfact.pe.ubl.ubl21.summarydocument;

import org.openfact.models.*;
import org.openfact.pe.ws.sunat.SunatSenderManager;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.UBLThirdPartySender;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.utils.UBLUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("SUMMARY_DOCUMENTS")
public class SunatUBLSummaryDocumentThirdPartySender implements UBLThirdPartySender {

    @Inject
    private SunatSenderManager sunatSenderManager;

    @Inject
    private UBLUtil ublUtil;

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public SendEventModel send(OrganizationModel organization, DocumentModel document) throws ModelInsuficientData, SendEventException {
        SendEventModel sendEvent = sunatSenderManager.sendSummaryCPE(organization, document, generateFileName(organization, document));
        return sendEvent;
    }

    private String generateFileName(OrganizationModel organization, DocumentModel document) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(document.getDocumentId());
        return sb.toString();
    }

}
