package org.openfact.pe.ubl.ubl21.voideddocument;

import org.openfact.models.types.DocumentRequiredAction;
import org.openfact.pe.ubl.ubl21.voided.VoidedDocumentsType;
import org.openfact.ubl.UBLCustomizationProvider;

public interface UBLVoidedDocumentCustomizationProvider extends UBLCustomizationProvider<VoidedDocumentsType> {

    default DocumentRequiredAction[] getRequiredActions() {
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_CUSTOMER, DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }

}
