package org.openfact.pe.ubl.ubl21.voideddocument;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.*;
import org.openfact.models.jpa.entities.SerieNumeroController;
import org.openfact.models.types.DocumentType;
import org.openfact.pe.ubl.types.SunatDocumentType;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.pe.ubl.types.TipoDocumentoRelacionadoBaja;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsLineType;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsType;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("VOIDED_DOCUMENTS")
public class SunatUBLVoidedDocumentIDGenerator implements UBLVoidedDocumentIDGenerator {

    @Inject
    private SerieNumeroController serieNumeroController;

    private VoidedDocumentsType resolve(Object o) {
        VoidedDocumentsType type;
        if (o instanceof VoidedDocumentsType) {
            type = (VoidedDocumentsType) o;
        } else {
            throw new IllegalStateException("Object class " + o.getClass().getName() + " should be a children of " + VoidedDocumentsType.class.getName());
        }
        return type;
    }

    @Override
    public String generateID(OrganizationModel organization, Object o) {
        VoidedDocumentsType voidedDocumentsType = resolve(o);
        VoidedDocumentsLineType voidedDocumentsLineType = voidedDocumentsType.getVoidedDocumentsLine().get(0);
        String codigoTipoDocumento = voidedDocumentsLineType.getDocumentTypeCode().getValue();

        String primeraLetra;

        TipoDocumentoRelacionadoBaja tipoDocumentoRelacionadoBaja = TipoDocumentoRelacionadoBaja.fromCodigo(codigoTipoDocumento).orElseThrow(() -> new ModelRuntimeException("Codigo de tipo documento para dar de baja no esta soportado"));
        switch (tipoDocumentoRelacionadoBaja) {
            case FACTURA:
                primeraLetra = "RA";
                break;
            case BOLETA:
                primeraLetra = "RA";
                break;
            case NOTA_CREDITO:
                primeraLetra = "RA";
                break;
            case NOTA_DEBITO:
                primeraLetra = "RA";
                break;
            case PERCEPCION:
                primeraLetra = "RP";
                break;
            case RETENCION:
                primeraLetra = "RR";
                break;
            default:
                throw new ModelRuntimeException("Tipo de documento relacionado de baja no soportado");
        }

        String serie = SunatMarshallerUtils.getCurrentDateInFormatAndTimeZonePeru("yyyyMMdd");
        AbstractMap.SimpleEntry<Integer, Integer> serieNumero = serieNumeroController.getSiguienteSerieNumero(
                organization.getId(),
                SunatDocumentType.VOIDED_DOCUMENTS.toString(),
                primeraLetra,
                Integer.parseInt(serie)
        );

        int nextSeries = serieNumero.getKey();
        int nextNumber = serieNumero.getValue();

        StringBuilder documentId = new StringBuilder();
        documentId.append(primeraLetra);
        documentId.append("-");
        documentId.append(serie);
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 5, "0"));

        return documentId.toString();
    }
}
