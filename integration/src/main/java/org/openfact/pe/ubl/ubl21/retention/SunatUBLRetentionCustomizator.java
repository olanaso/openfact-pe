package org.openfact.pe.ubl.ubl21.retention;

import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.ubl.types.TipoDocumentoRelacionadoPercepcionRetencion;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;

@Stateless
@ProviderType("default")
@UBLDocumentType("RETENTION")
public class SunatUBLRetentionCustomizator extends AbstractRetentionProvider implements UBLRetentionCustomizator {

    @Inject
    private SunatTypeToModel typeToModel;

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public void config(OrganizationModel organization, DocumentModel document, Object o) {
        RetentionType retentionType = resolve(o);

        typeToModel.importRetention(organization, document, retentionType);

        if (retentionType.getSunatRetentionDocumentReference() != null && !retentionType.getSunatRetentionDocumentReference().isEmpty()) {
            retentionType.getSunatRetentionDocumentReference().stream().forEach(c -> {
                String attachedDocumentId = c.getID().getValue();
                String attachedDocumentCodeType = c.getID().getSchemeID();
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
}
