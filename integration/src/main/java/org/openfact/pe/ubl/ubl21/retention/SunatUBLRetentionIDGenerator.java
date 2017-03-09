package org.openfact.pe.ubl.ubl21.retention;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentQuery;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.pe.ubl.types.SunatDocumentType;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.pe.ubl.ubl21.perception.PerceptionType;
import org.openfact.pe.ubl.ubl21.perception.UBLPerceptionIDGenerator;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@UBLProviderType("sunat")
@UBLDocumentType("RETENTION")
public class SunatUBLRetentionIDGenerator implements UBLRetentionIDGenerator {

    @Inject
    private DocumentQuery documentQuery;

    @Override
    public String generateID(OrganizationModel organization, RetentionType retentionType) {
        TipoComprobante retentionCode = TipoComprobante.RETENCION;
        DocumentModel lastRetention = null;
        ScrollModel<DocumentModel> retentions = documentQuery.organization(organization)
                .documentType(SunatDocumentType.RETENTION.toString())
                .entityQuery()
                .orderByDesc(DocumentModel.DOCUMENT_ID)
                .resultScroll()
                .getScrollResult(10);

        Iterator<DocumentModel> iterator = retentions.iterator();

        Pattern pattern = Pattern.compile(retentionCode.getMask());
        while (iterator.hasNext()) {
            DocumentModel retention = iterator.next();
            String documentId = retention.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastRetention = retention;
                break;
            }
        }

        int series = 0;
        int number = 0;
        if (lastRetention != null) {
            String[] splits = lastRetention.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = SunatMarshallerUtils.getNextNumber(number, 99_999_999);
        int nextSeries = SunatMarshallerUtils.getNextSerie(series, number, 999, 99_999_999);
        StringBuilder documentId = new StringBuilder();
        documentId.append(retentionCode.getMask().substring(2, 3));
        documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return documentId.toString();
    }

}
