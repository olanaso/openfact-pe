package org.openfact.pe.ubl.ubl21.invoice;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.openfact.models.DocumentLineModel;
import org.openfact.models.DocumentModel;
import org.openfact.models.OrganizationModel;
import org.openfact.models.utils.TypeToModel;
import org.openfact.ubl.ubl21.invoice.UBLInvoiceCustomizationProvider;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Stateless
@UBLProviderType("default")
@UBLDocumentType("INVOICE")
public class SunatUBLInvoiceCustomizationProvider implements UBLInvoiceCustomizationProvider {

    @Inject
    private TypeToModel typeToModel;

    @Override
    public void config(OrganizationModel organization, DocumentModel document, InvoiceType invoiceType) {
        typeToModel.importInvoice(organization, document, invoiceType);


        if (invoiceType.getInvoiceLine() != null && !invoiceType.getInvoiceLine().isEmpty()) {
            Consumer<InvoiceLineType> consumer = invoiceLineType -> {
                DocumentLineModel documentLine = document.addDocumentLine();
                if (invoiceLineType.getItem() != null && invoiceLineType.getItem().getDescription() != null && !invoiceLineType.getItem().getDescription().isEmpty()) {
                    documentLine.setAttribute("itemDescription", invoiceLineType.getItem().getDescription().stream().map(f -> f.getValue()).collect(Collectors.joining(",")));
                }
                if (invoiceLineType.getInvoicedQuantity() != null) {
                    documentLine.setAttribute("quantity", invoiceLineType.getInvoicedQuantityValue());
                }
                if (invoiceLineType.getPrice() != null && invoiceLineType.getPrice().getPriceAmount() != null) {
                    documentLine.setAttribute("priceAmount", invoiceLineType.getPrice().getPriceAmountValue());
                }
                if (invoiceLineType.getTaxTotal() != null && !invoiceLineType.getTaxTotal().isEmpty()) {
                    // IGV
                    TaxTotalType taxTotalType = invoiceLineType.getTaxTotal().get(0);
                    if (taxTotalType.getTaxSubtotal() != null && !taxTotalType.getTaxSubtotal().isEmpty()) {
                        TaxSubtotalType taxSubtotalType = taxTotalType.getTaxSubtotal().get(0);
                        if (taxSubtotalType.getTaxCategory() != null && taxSubtotalType.getTaxCategory().getTaxExemptionReasonCode() != null) {
                            documentLine.setAttribute("taxExemptionReasonCodeIGV", taxSubtotalType.getTaxCategory().getTaxExemptionReasonCodeValue());
                        }
                    }
                }
            };
            invoiceType.getInvoiceLine().forEach(consumer);
        }
    }

}
