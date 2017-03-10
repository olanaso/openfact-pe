package org.openfact.pe.ubl.ubl21.retention;

import org.openfact.models.types.DocumentRequiredAction;
import org.openfact.ubl.UBLCustomizationProvider;

public interface UBLRetentionCustomizationProvider extends UBLCustomizationProvider<RetentionType> {

    default DocumentRequiredAction[] getRequiredActions() {
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }

}
