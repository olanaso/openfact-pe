package org.openfact.pe.ubl.ubl21.summarydocument;

import org.openfact.models.types.DocumentRequiredAction;

public interface UBLSummaryDocumentCustomizator extends UBLSummaryDocumentCustomizatorFactory {

    default DocumentRequiredAction[] getRequiredActions() {
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }

}
