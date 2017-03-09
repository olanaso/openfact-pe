package org.openfact.pe.ubl.ubl21.summarydocument;

import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.ubl.types.TipoDocumentoRelacionadoPercepcionRetencion;
import org.openfact.pe.ubl.ubl21.retention.RetentionType;
import org.openfact.pe.ubl.ubl21.retention.UBLRetentionCustomizationProvider;
import org.openfact.pe.ubl.ubl21.summary.SummaryDocumentsType;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;

@Stateless
@UBLProviderType("default")
@UBLDocumentType("SUMMARY_DOCUMENTS")
public class SunatUBLSummaryDocumentCustomizationProvider implements UBLSummaryDocumentCustomizationProvider {

    @Inject
    private SunatTypeToModel typeToModel;

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public void config(OrganizationModel organization, DocumentModel document, SummaryDocumentsType summaryDocumentsType) {
        typeToModel.importSummaryDocument(organization, document, summaryDocumentsType);
    }
}
