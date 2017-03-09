package org.openfact.pe.ubl.data.model;

import org.jboss.logging.Logger;
import org.openfact.models.DocumentModel;
import org.openfact.ubl.data.UBLReportDataProvider;

import javax.ejb.Stateless;

@Stateless
public class SunatModelUBLReportDataProvider implements UBLReportDataProvider {

    private static final Logger logger = Logger.getLogger(SunatModelUBLReportDataProvider.class);

    @Override
    public Object getFieldValue(DocumentModel model, String fieldName) {
        SunatModelSupportedAttribute attribute = SunatModelSupportedAttribute.fromString(fieldName.toUpperCase());
        if (attribute != null) {
            switch (attribute) {
                case SUNAT_MODEL_TICKET:
                    return model.getFirstAttribute(SunatModelSupportedAttribute.SUNAT_MODEL_TICKET.getAdditionalInformation());
            }
        }
        return null;
    }
}
