package org.openfact.pe.ubl.ubl21.debitnote;

import org.openfact.models.*;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.pe.ws.sunat.SunatSenderManager;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.UBLThirdPartySender;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("DEBIT_NOTE")
public class SunatUBLDebitNoteThirdPartySender implements UBLThirdPartySender {

    @Inject
    private SunatSenderManager sunatSenderManager;

    @Override
    public SendEventModel send(OrganizationModel organization, DocumentModel document) throws ModelInsuficientData, SendEventException {
        return sunatSenderManager.sendBillCPE(organization, document, generateFileName(organization, document));
    }

    private String generateFileName(OrganizationModel organization, DocumentModel document) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }
        String codigo = TipoComprobante.NOTA_DEBITO.getCodigo();
        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(codigo).append("-");
        sb.append(document.getDocumentId());
        return sb.toString();
    }

}
