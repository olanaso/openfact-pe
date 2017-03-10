package org.openfact.pe.ubl.ubl21.perception;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentQuery;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.pe.ubl.types.SunatDocumentType;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@UBLProviderType("sunat")
@UBLDocumentType("PERCEPTION")
public class SunatUBLPerceptionIDGenerator implements UBLPerceptionIDGenerator {

    @Inject
    private DocumentQuery documentQuery;

    @Override
    public String generateID(OrganizationModel organization, PerceptionType perceptionType) {
        TipoComprobante perceptionCode = TipoComprobante.PERCEPCION;
        DocumentModel lastPerception = null;
        ScrollModel<DocumentModel> perceptions = documentQuery.organization(organization)
                .documentType(SunatDocumentType.PERCEPTION.toString())
                .entityQuery()
                .orderByDesc(DocumentModel.DOCUMENT_ID)
                .resultScroll()
                .getScrollResult(10);

        Iterator<DocumentModel> iterator = perceptions.iterator();

        Pattern pattern = Pattern.compile(perceptionCode.getMask());
        while (iterator.hasNext()) {
            DocumentModel perception = iterator.next();
            String documentId = perception.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastPerception = perception;
                break;
            }
        }

        int series = 0;
        int number = 0;
        if (lastPerception != null) {
            String[] splits = lastPerception.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = SunatMarshallerUtils.getNextNumber(number, 99_999_999);
        int nextSeries = SunatMarshallerUtils.getNextSerie(series, number, 999, 99_999_999);
        StringBuilder documentId = new StringBuilder();
        documentId.append(perceptionCode.getMask().substring(2, 3));
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }

}
