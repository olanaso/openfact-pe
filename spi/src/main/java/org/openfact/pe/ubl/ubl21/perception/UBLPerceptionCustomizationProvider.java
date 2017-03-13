package org.openfact.pe.ubl.ubl21.perception;

import org.openfact.models.types.DocumentRequiredAction;

public interface UBLPerceptionCustomizationProvider extends UBLPerceptionCustomizatorFactory {

    default DocumentRequiredAction[] getRequiredActions() {
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }

}
