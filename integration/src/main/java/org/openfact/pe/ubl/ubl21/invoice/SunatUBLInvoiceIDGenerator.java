package org.openfact.pe.ubl.ubl21.invoice;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.*;
import org.openfact.models.types.DocumentType;
import org.openfact.pe.ubl.types.TipoInvoice;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.invoice.UBLInvoiceIDGenerator;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("INVOICE")
public class SunatUBLInvoiceIDGenerator extends AbstractInvoiceProvider implements UBLInvoiceIDGenerator {

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public String generateID(OrganizationModel organization, Object o) {
        InvoiceType invoiceType = resolve(o);

        String invoiceTypeCode = invoiceType.getInvoiceTypeCodeValue();

        TipoInvoice tipoInvoice;
        if (TipoInvoice.FACTURA.getCodigo().equals(invoiceTypeCode)) {
            tipoInvoice = TipoInvoice.FACTURA;
        } else if (TipoInvoice.BOLETA.getCodigo().equals(invoiceTypeCode)) {
            tipoInvoice = TipoInvoice.BOLETA;
        } else {
            throw new ModelRuntimeException("Invalid invoice type code");
        }

        DocumentModel lastInvoice = null;
        ScrollModel<DocumentModel> invoices = documentProvider.createQuery(organization)
                .documentType(DocumentType.INVOICE.toString())
                .entityQuery()
                .orderByDesc(DocumentModel.DOCUMENT_ID)
                .resultScroll()
                .getScrollResult(10);

        Iterator<DocumentModel> iterator = invoices.iterator();

        Pattern pattern = Pattern.compile(tipoInvoice.getDocumentIdPattern());
        while (iterator.hasNext()) {
            DocumentModel invoice = iterator.next();
            String documentId = invoice.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastInvoice = invoice;
                break;
            }
        }

        int series = 0;
        int number = 0;
        if (lastInvoice != null) {
            String[] splits = lastInvoice.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = SunatMarshallerUtils.getNextNumber(number, 99_999_999);
        int nextSeries = SunatMarshallerUtils.getNextSerie(series, number, 999, 99_999_999);

        StringBuilder documentId = new StringBuilder();
        documentId.append(tipoInvoice.getDocumentIdPattern().substring(2, 3));
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }
}
