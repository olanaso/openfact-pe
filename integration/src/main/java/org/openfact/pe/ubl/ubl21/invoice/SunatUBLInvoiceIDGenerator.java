package org.openfact.pe.ubl.ubl21.invoice;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.jpa.entities.SerieNumeroController;
import org.openfact.models.*;
import org.openfact.models.types.DocumentType;
import org.openfact.pe.ubl.types.TipoInvoice;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.invoice.UBLInvoiceIDGenerator;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.AbstractMap;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("INVOICE")
public class SunatUBLInvoiceIDGenerator extends AbstractInvoiceProvider implements UBLInvoiceIDGenerator {

    @Inject
    private SerieNumeroController serieNumeroController;

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

        AbstractMap.SimpleEntry<Integer, Integer> serieNumero = serieNumeroController.getSiguienteSerieNumero(
                organization.getId(),
                DocumentType.INVOICE.toString(),
                tipoInvoice.getPrimeraLetra()
        );

        int nextSeries = serieNumero.getKey();
        int nextNumber = serieNumero.getValue();

        StringBuilder documentId = new StringBuilder();
        documentId.append(tipoInvoice.getPrimeraLetra());
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }
}
