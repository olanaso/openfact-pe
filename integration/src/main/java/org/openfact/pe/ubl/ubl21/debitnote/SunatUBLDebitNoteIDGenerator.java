package org.openfact.pe.ubl.ubl21.debitnote;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.jpa.entities.SerieNumeroController;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.debitnote.UBLDebitNoteIDGenerator;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.AbstractMap;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("DEBIT_NOTE")
public class SunatUBLDebitNoteIDGenerator extends AbstractDebitNoteProvider implements UBLDebitNoteIDGenerator {

    @Inject
    private SerieNumeroController serieNumeroController;

    @Override
    public String generateID(OrganizationModel organization, Object o) {
        DebitNoteType debitNoteType = resolve(o);

        String invoiceDocumentReferenceID = null;
        if (debitNoteType.getDiscrepancyResponseCount() > 0) {
            invoiceDocumentReferenceID = debitNoteType.getDiscrepancyResponse().get(0).getReferenceIDValue();
        } else if (debitNoteType.getBillingReferenceCount() > 0) {
            invoiceDocumentReferenceID = debitNoteType.getBillingReference().get(0).getInvoiceDocumentReference().getIDValue();
        }

        String primeraLetra = debitNoteType.getBillingReference().get(0).getInvoiceDocumentReference().getIDValue().substring(0, 1);
        AbstractMap.SimpleEntry<Integer, Integer> serieNumero = serieNumeroController.getSiguienteSerieNumero(
                organization.getId(),
                TipoComprobante.NOTA_DEBITO.toString(),
                primeraLetra

        );

        int nextSeries = serieNumero.getKey();
        int nextNumber = serieNumero.getValue();

        StringBuilder documentId = new StringBuilder();
        documentId.append(primeraLetra);
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }
}
