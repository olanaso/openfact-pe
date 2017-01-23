package org.openfact.pe.services.util;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.openfact.common.util.ObjectUtil;
import org.openfact.models.CreditNoteModel;
import org.openfact.models.SendException;
import org.openfact.models.ModelInsuficientData;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.InvoiceModel;
import org.openfact.pe.models.enums.*;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.models.enums.TipoComprobante;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.services.constants.SunatEventType;

import jodd.io.ZipBuilder;

public class SunatTemplateUtils {

    public static byte[] generateZip(byte[] document, String fileName) throws TransformerException, IOException {
        /* .addFolder("dummy/") */
        return ZipBuilder.createZipInMemory().add(document).path(fileName).save().toBytes();
    }

    public static String generateFileName(OrganizationModel organization, InvoiceModel invoice) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }

        String invoiceTypeCode = null;
        if (invoice.getInvoiceTypeCode().equals(TipoInvoice.FACTURA.getCodigo())) {
            invoiceTypeCode = TipoInvoice.FACTURA.getCodigo();
        } else if (invoice.getInvoiceTypeCode().equals(TipoInvoice.BOLETA.getCodigo())) {
            invoiceTypeCode = TipoInvoice.BOLETA.getCodigo();
        } else {
            throw new ModelInsuficientData("InvoiceTypeCode invalido");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(invoiceTypeCode).append("-");
        sb.append(invoice.getDocumentId());
        return sb.toString();
    }

    public static String generateFileName(OrganizationModel organization, CreditNoteModel creditNote) throws ModelInsuficientData {
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

    public static String generateFileName(OrganizationModel organization, DebitNoteModel debitNote) throws ModelInsuficientData {
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

    public static String generateFileName(OrganizationModel organization, VoidedDocumentModel voidedDocument) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(voidedDocument.getDocumentId());
        return sb.toString();
    }

    public static String generateFileName(OrganizationModel organization, SummaryDocumentModel summaryDocument) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(summaryDocument.getDocumentId());
        return sb.toString();
    }

    public static String generateFileName(OrganizationModel organization, RetentionModel retention) throws ModelInsuficientData {
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

    public static String generateFileName(OrganizationModel organization, PerceptionModel perception) throws ModelInsuficientData {
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
