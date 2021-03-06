package org.openfact.pe.ubl.ubl21.voideddocument;

import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.models.types.DocumentRequiredAction;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.ubl.types.SunatRequiredAction;
import org.openfact.pe.ubl.types.TipoDocumentoRelacionadoBaja;
import org.openfact.pe.ubl.types.TipoDocumentoRelacionadoPercepcionRetencion;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsType;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("VOIDED_DOCUMENTS")
public class SunatUBLVoidedDocumentCustomizator extends AbstractVoidedDocumentProvider implements UBLVoidedDocumentCustomizator {

    @Inject
    private SunatTypeToModel typeToModel;

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public void config(OrganizationModel organization, DocumentModel document, Object o) {
        VoidedDocumentsType voidedDocumentsType = resolve(o);

        typeToModel.importVoidedDocument(organization, document, voidedDocumentsType);

        if (voidedDocumentsType.getVoidedDocumentsLine() != null && !voidedDocumentsType.getVoidedDocumentsLine().isEmpty()) {
            voidedDocumentsType.getVoidedDocumentsLine().stream().forEach(c -> {
                String attachedDocumentId = c.getDocumentSerialID().getValue() + "-" + c.getDocumentNumberID().getValue();
                String attachedDocumentCodeType = c.getDocumentTypeCode().getValue();
                Optional<TipoDocumentoRelacionadoBaja> tipoDocumentoRelacionadoPercepcion = Arrays
                        .stream(TipoDocumentoRelacionadoBaja.values())
                        .filter(p -> p.getCodigo().equals(attachedDocumentCodeType))
                        .findAny();
                if (tipoDocumentoRelacionadoPercepcion.isPresent()) {
                    DocumentModel attachedDocument = documentProvider.getDocumentByTypeAndDocumentId(tipoDocumentoRelacionadoPercepcion.get().getDocumentType(), attachedDocumentId, organization);
                    if (attachedDocument != null) {
                        document.addAttachedDocument(attachedDocument);
                    }
                }
            });
        }
    }

    @Override
    public DocumentRequiredAction[] getRequiredActions() {
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_CUSTOMER, DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }

    @Override
    public String[] getExtraRequiredActions() {
        return new String[]{SunatRequiredAction.CONSULTAR_TICKET.toString()};
    }

}
