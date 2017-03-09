package org.openfact.pe.ubl.ubl21.summarydocument;

import org.openfact.models.types.DocumentRequiredAction;
import org.openfact.pe.ubl.ubl21.summary.SummaryDocumentsType;
import org.openfact.ubl.UBLCustomizationProvider;

public interface UBLSummaryDocumentCustomizationProvider extends UBLCustomizationProvider<SummaryDocumentsType> {

    default DocumentRequiredAction[] getRequiredActions() {
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }

}
