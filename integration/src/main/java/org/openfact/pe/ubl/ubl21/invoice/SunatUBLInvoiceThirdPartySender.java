package org.openfact.pe.ubl.ubl21.invoice;

import org.openfact.models.*;
import org.openfact.models.utils.TypeToModel;
import org.openfact.pe.ubl.types.TipoInvoice;
import org.openfact.pe.ws.sunat.SunatSenderManager;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.UBLThirdPartySender;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("INVOICE")
public class SunatUBLInvoiceThirdPartySender implements UBLThirdPartySender {

    @Inject
    private SunatSenderManager sunatSenderManager;

    @Override
    public SendEventModel send(OrganizationModel organization, DocumentModel document) throws ModelInsuficientData, SendEventException {
        return sunatSenderManager.sendBillAddress1(organization, document, generateFileName(organization, document));
    }

    private String generateFileName(OrganizationModel organization, DocumentModel document) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }

        String invoiceTypeCode = null;
        if (document.getFirstAttribute(TypeToModel.INVOICE_TYPE_CODE).equals(TipoInvoice.FACTURA.getCodigo())) {
            invoiceTypeCode = TipoInvoice.FACTURA.getCodigo();
        } else if (document.getFirstAttribute(TypeToModel.INVOICE_TYPE_CODE).equals(TipoInvoice.BOLETA.getCodigo())) {
            invoiceTypeCode = TipoInvoice.BOLETA.getCodigo();
        } else {
            throw new ModelInsuficientData("Invoice Type Code invalido");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(invoiceTypeCode).append("-");
        sb.append(document.getDocumentId());
        return sb.toString();
    }

}
