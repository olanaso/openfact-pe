package org.openfact.pe.ubl.ubl21.summarydocument;

import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.models.types.DocumentRequiredAction;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.ubl.ubl21.summary.SummaryDocumentsType;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("SUMMARY_DOCUMENTS")
public class SunatUBLSummaryDocumentCustomizator extends AbstractSummaryDocumentProvider implements UBLSummaryDocumentCustomizator {

    @Inject
    private SunatTypeToModel typeToModel;

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public void config(OrganizationModel organization, DocumentModel document, Object o) {
        SummaryDocumentsType summaryDocumentsType = resolve(o);

        typeToModel.importSummaryDocument(organization, document, summaryDocumentsType);
    }

    @Override
    public DocumentRequiredAction[] getRequiredActions() {
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }

}
