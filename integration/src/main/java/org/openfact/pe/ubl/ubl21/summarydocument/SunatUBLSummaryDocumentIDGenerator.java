package org.openfact.pe.ubl.ubl21.summarydocument;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.*;
import org.openfact.pe.ubl.types.SunatDocumentType;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.pe.ubl.ubl21.retention.RetentionType;
import org.openfact.pe.ubl.ubl21.retention.UBLRetentionIDGenerator;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;
import org.openfact.ubl.ubl21.qualifiers.UBLProviderType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@UBLProviderType("sunat")
@UBLDocumentType("SUMMARY_DOCUMENTS")
public class SunatUBLSummaryDocumentIDGenerator implements UBLRetentionIDGenerator {

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public String generateID(OrganizationModel organization, RetentionType retentionType) {
        TipoComprobante summaryDocumentCode = TipoComprobante.RESUMEN_DIARIO;
        DocumentModel lastSummaryDocument = null;
        ScrollModel<DocumentModel> summaryDocuments = documentProvider.createQuery(organization)
                .documentType(SunatDocumentType.SUMMARY_DOCUMENTS.toString())
                .entityQuery()
                .orderByDesc(DocumentModel.DOCUMENT_ID)
                .resultScroll()
                .getScrollResult(10);

        Iterator<DocumentModel> iterator = summaryDocuments.iterator();

        Pattern pattern = Pattern.compile(summaryDocumentCode.getMask());
        while (iterator.hasNext()) {
            DocumentModel summaryDocument = iterator.next();
            String documentId = summaryDocument.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastSummaryDocument = summaryDocument;
                break;
            }
        }

        int number = 0, secuence = 0;
        if (lastSummaryDocument != null) {
            String[] splits = lastSummaryDocument.getDocumentId().split("-");
            secuence = Integer.parseInt(splits[1]);
            number = Integer.parseInt(splits[2]);
        }
        int nextSeries = SunatMarshallerUtils.getDateToNumber();
        int nextNumber = 0;
        if (secuence == nextSeries) {
            nextNumber = SunatMarshallerUtils.getNextNumber(number, 99999);
        } else {
            nextNumber = SunatMarshallerUtils.getNextNumber(0, 99999);
        }
        StringBuilder documentId = new StringBuilder();
        documentId.append(summaryDocumentCode.getMask().substring(2, 4));
        documentId.append("-");
        documentId.append(nextSeries);
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 5, "0"));

        return documentId.toString();
    }

}
