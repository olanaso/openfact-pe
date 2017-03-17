package org.openfact.pe.ubl.ubl21.voideddocument;

import org.openfact.models.*;
import org.openfact.pe.ubl.types.SunatDocumentType;
import org.openfact.pe.ubl.types.TipoDocumentoRelacionadoPercepcionRetencion;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsType;
import org.openfact.pe.ws.sunat.SunatSenderManager;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.UBLReaderWriter;
import org.openfact.ubl.UBLThirdPartySender;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.utils.UBLUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("VOIDED_DOCUMENTS")
public class SunatUBLVoidedDocumentThirdPartySender implements UBLThirdPartySender {

    @Inject
    private SunatSenderManager sunatSenderManager;

    @Inject
    private UBLUtil ublUtil;

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public SendEventModel send(OrganizationModel organization, DocumentModel document) throws ModelInsuficientData, SendEventException {
        SendEventModel sendEvent = sunatSenderManager.sendSummary(organization, document, generateFileName(organization, document));

        // Disable all related documents
        UBLReaderWriter readerWriter = ublUtil.getReaderWriter(SunatDocumentType.VOIDED_DOCUMENTS.toString());
        VoidedDocumentsType voidedDocumentsType = (VoidedDocumentsType) readerWriter.reader().read(document.getXmlAsFile().getFile());
        voidedDocumentsType.getVoidedDocumentsLine().stream().forEach(c -> {
            String attachedDocumentId = c.getDocumentSerialID().getValue() + "-" + c.getDocumentNumberID().getValue();
            String attachedDocumentCodeType = c.getDocumentTypeCode().getValue();
            Optional<TipoDocumentoRelacionadoPercepcionRetencion> tipoDocumentoRelacionadoPercepcion = Arrays
                    .stream(TipoDocumentoRelacionadoPercepcionRetencion.values())
                    .filter(p -> p.getCodigo().equals(attachedDocumentCodeType))
                    .findAny();
            if (tipoDocumentoRelacionadoPercepcion.isPresent()) {
                DocumentModel attachedDocument = documentProvider.getDocumentByTypeAndDocumentId(tipoDocumentoRelacionadoPercepcion.get().getDocumentType(), attachedDocumentId, organization);
                attachedDocument.disable();
            }
        });

        return sendEvent;
    }

    private String generateFileName(OrganizationModel organization, DocumentModel document) throws ModelInsuficientData {
        if (organization.getAssignedIdentificationId() == null) {
            throw new ModelInsuficientData("Organization doesn't have assignedIdentificationId");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(organization.getAssignedIdentificationId()).append("-");
        sb.append(document.getDocumentId());
        return sb.toString();
    }

}
