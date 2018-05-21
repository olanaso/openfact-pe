package org.openfact.pe.ubl.ubl21.retention;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.models.jpa.entities.SerieNumeroController;
import org.openfact.pe.ubl.types.SunatDocumentType;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("RETENTION")
public class SunatUBLRetentionIDGenerator implements UBLRetentionIDGenerator {

    @Inject
    private SerieNumeroController serieNumeroController;

    @Override
    public String generateID(OrganizationModel organization, Object o) {
        AbstractMap.SimpleEntry<Integer, Integer> serieNumero = serieNumeroController.getSiguienteSerieNumero(
                organization.getId(),
                SunatDocumentType.RETENTION.toString(),
                "R"

        );

        int nextSeries = serieNumero.getKey();
        int nextNumber = serieNumero.getValue();

        StringBuilder documentId = new StringBuilder();
        documentId.append("R");
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }
}
