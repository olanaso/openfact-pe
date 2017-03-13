package org.openfact.pe.ubl.ubl21.invoice;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.openfact.models.DocumentLineModel;
import org.openfact.models.DocumentModel;
import org.openfact.models.OrganizationModel;
import org.openfact.models.types.DocumentRequiredAction;
import org.openfact.models.utils.TypeToModel;
import org.openfact.ubl.ubl21.invoice.UBLInvoiceCustomizator;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Stateless
@UBLProviderType("sunat")
@UBLDocumentType("INVOICE")
public class SunatUBLInvoiceCustomizator extends AbstractInvoiceProvider implements UBLInvoiceCustomizator {

    @Inject
    private TypeToModel typeToModel;

    @Override
    public void config(OrganizationModel organization, DocumentModel document, Object o) {
        InvoiceType invoiceType = resolve(o);

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

    @Override
    public DocumentRequiredAction[] getRequiredActions() {
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_CUSTOMER, DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }
}
