package org.openfact.pe.ubl.ubl21.creditnote;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.jpa.entities.SerieNumeroController;
import org.openfact.models.OrganizationModel;
import org.openfact.models.types.DocumentType;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.creditnote.UBLCreditNoteIDGenerator;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.AbstractMap;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("CREDIT_NOTE")
public class SunatUBLCreditNoteIDGenerator extends AbstractCreditNoteProvider implements UBLCreditNoteIDGenerator {

    @Inject
    private SerieNumeroController serieNumeroController;

    @Override
    public String generateID(OrganizationModel organization, Object o) {
        CreditNoteType creditNoteType = resolve(o);

        String invoiceDocumentReferenceID = null;
        if (creditNoteType.getDiscrepancyResponseCount() > 0) {
            invoiceDocumentReferenceID = creditNoteType.getDiscrepancyResponse().get(0).getReferenceIDValue();
        } else if (creditNoteType.getBillingReferenceCount() > 0) {
            invoiceDocumentReferenceID = creditNoteType.getBillingReference().get(0).getInvoiceDocumentReference().getIDValue();
        }

        String primeraLetra = creditNoteType.getBillingReference().get(0).getInvoiceDocumentReference().getIDValue().substring(0, 1);
        AbstractMap.SimpleEntry<Integer, Integer> serieNumero = serieNumeroController.getSiguienteSerieNumero(
                organization.getId(),
                DocumentType.CREDIT_NOTE.toString(),
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
