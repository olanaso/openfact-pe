package org.openfact.pe.spi;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.InvoiceModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.ubl.InvoiceIDGeneratorProvider;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public class SunatInvoiceIDGeneratorProvider implements InvoiceIDGeneratorProvider {

    protected OpenfactSession session;

    public SunatInvoiceIDGeneratorProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public String generateID(OrganizationModel organization, InvoiceType invoiceType) {
        CodigoTipoDocumento invoiceCode;
        if (CodigoTipoDocumento.FACTURA.getCodigo().equals(invoiceType.getInvoiceTypeCode())) {
            invoiceCode = CodigoTipoDocumento.FACTURA;
        } else if (CodigoTipoDocumento.BOLETA.getCodigo().equals(invoiceType.getInvoiceTypeCode())) {
            invoiceCode = CodigoTipoDocumento.BOLETA;
        } else {
            throw new ModelException("Invalid invoiceTypeCode");
        }
        
        InvoiceModel lastInvoice = null;
        ScrollModel<InvoiceModel> invoices = session.invoices().getInvoicesScroll(organization, false, 4, 2);
        Iterator<InvoiceModel> iterator = invoices.iterator();
       
        Pattern pattern = Pattern.compile(invoiceCode.getMask());        
        while (iterator.hasNext()) {
            InvoiceModel invoice = iterator.next();
            String documentId = invoice.getDocumentId();
            
            Matcher matcher = pattern.matcher(documentId);            
            if(matcher.find()) {
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

        int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
        int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
        StringBuilder documentId = new StringBuilder();
        documentId.append(invoiceCode.getMask().substring(0, 1));
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }   

}
