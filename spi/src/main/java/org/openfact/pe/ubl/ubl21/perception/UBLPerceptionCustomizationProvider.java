package org.openfact.pe.ubl.ubl21.perception;

import org.openfact.models.types.DocumentRequiredAction;
import org.openfact.ubl.UBLCustomizationProvider;

public interface UBLPerceptionCustomizationProvider extends UBLCustomizationProvider<PerceptionType> {

    default DocumentRequiredAction[] getRequiredActions() {
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }

}
