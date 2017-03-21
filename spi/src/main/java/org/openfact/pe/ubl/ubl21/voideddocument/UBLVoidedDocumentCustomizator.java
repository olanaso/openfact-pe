package org.openfact.pe.ubl.ubl21.voideddocument;

import org.openfact.models.types.DocumentRequiredAction;

public interface UBLVoidedDocumentCustomizator extends UBLVoidedDocumentCustomizatorFactory {

    default DocumentRequiredAction[] getRequiredActions() {
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_CUSTOMER, DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }

}
