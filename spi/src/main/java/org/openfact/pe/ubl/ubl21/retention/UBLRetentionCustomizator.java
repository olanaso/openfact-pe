package org.openfact.pe.ubl.ubl21.retention;

import org.openfact.models.types.DocumentRequiredAction;

public interface UBLRetentionCustomizator extends UBLRetentionCustomizatorFactory {

    default DocumentRequiredAction[] getRequiredActions() {
        return new DocumentRequiredAction[]{DocumentRequiredAction.SEND_TO_THIRD_PARTY};
    }

}
