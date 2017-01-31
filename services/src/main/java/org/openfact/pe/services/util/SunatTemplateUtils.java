package org.openfact.pe.services.util;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.openfact.common.util.ObjectUtil;
import org.openfact.models.DocumentModel;
import org.openfact.models.ModelInsuficientData;
import org.openfact.models.utils.TypeToModel;
import org.openfact.pe.models.enums.*;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.models.enums.TipoComprobante;
import org.openfact.pe.services.constants.SunatEventType;

import jodd.io.ZipBuilder;

public class SunatTemplateUtils {

    public static byte[] generateZip(byte[] document, String fileName) throws TransformerException, IOException {
        /* .addFolder("dummy/") */
        return ZipBuilder.createZipInMemory().add(document).path(fileName).save().toBytes();
    }

    public static String generateInvoiceFileName(OrganizationModel organization, DocumentModel document) throws ModelInsuficientData {
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

    public static String generateCreditNoteFileName(OrganizationModel organization, DocumentModel creditNote) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }
        String codigo = TipoComprobante.NOTA_CREDITO.getCodigo();
        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(codigo).append("-");
        sb.append(creditNote.getDocumentId());
        return sb.toString();
    }

    public static String generateDebitNoteFileName(OrganizationModel organization, DocumentModel debitNote) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }
        String codigo = TipoComprobante.NOTA_DEBITO.getCodigo();
        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(codigo).append("-");
        sb.append(debitNote.getDocumentId());
        return sb.toString();
    }

    public static String generatePerceptionFileName(OrganizationModel organization, DocumentModel perception) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }
        String codigo = TipoComprobante.PERCEPCION.getCodigo();
        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(codigo).append("-");
        sb.append(perception.getDocumentId());
        return sb.toString();
    }

    public static String generateRetentionFileName(OrganizationModel organization, DocumentModel retention) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }
        String codigo = TipoComprobante.RETENCION.getCodigo();
        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(codigo).append("-");
        sb.append(retention.getDocumentId());
        return sb.toString();
    }

    public static String generateVoidedDocumentFileName(OrganizationModel organization, DocumentModel voidedDocument) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(voidedDocument.getDocumentId());
        return sb.toString();
    }

    public static String generateSummaryFileName(OrganizationModel organization, DocumentModel summaryDocument) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(summaryDocument.getDocumentId());
        return sb.toString();
    }

    public static String getOrganizationName(OrganizationModel organization) {
        if (organization.getDisplayNameHtml() != null) {
            return organization.getDisplayNameHtml();
        } else if (organization.getDisplayName() != null) {
            return organization.getDisplayName();
        } else {
            return ObjectUtil.capitalize(organization.getName());
        }
    }

    public static String toCamelCase(SunatEventType event) {
        StringBuilder sb = new StringBuilder("event");
        for (String s : event.name().toString().toLowerCase().split("_")) {
            sb.append(ObjectUtil.capitalize(s));
        }
        return sb.toString();
    }
}
