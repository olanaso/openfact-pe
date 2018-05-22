package org.openfact.pe.ubl.ubl21.perception;

import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.models.types.DocumentRequiredAction;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.ubl.types.TipoDocumentoRelacionadoPercepcionRetencion;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("PERCEPTION")
public class SunatUBLPerceptionCustomizator extends AbstractPerceptionProvider implements UBLPerceptionCustomizationProvider {

    @Inject
    private SunatTypeToModel typeToModel;

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public void config(OrganizationModel organization, DocumentModel document, Object o) {
        PerceptionType perceptionType = resolve(o);

        typeToModel.importPerception(organization, document, perceptionType);

        if (perceptionType.getSunatPerceptionDocumentReference() != null && !perceptionType.getSunatPerceptionDocumentReference().isEmpty()) {
            perceptionType.getSunatPerceptionDocumentReference().stream().forEach(c -> {
                String attachedDocumentId = c.getId().getValue();
                String attachedDocumentCodeType = c.getId().getSchemeID();
                Optional<TipoDocumentoRelacionadoPercepcionRetencion> tipoDocumentoRelacionadoPercepcion = Arrays
                        .stream(TipoDocumentoRelacionadoPercepcionRetencion.values())
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
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }

    @Override
    public String[] getExtraRequiredActions() {
        return new String[0];
    }

}
