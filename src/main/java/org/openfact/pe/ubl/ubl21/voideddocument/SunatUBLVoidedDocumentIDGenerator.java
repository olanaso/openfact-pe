package org.openfact.pe.ubl.ubl21.voideddocument;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.pe.ubl.types.SunatDocumentType;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.pe.ubl.ubl21.factories.SunatMarshallerUtils;
import org.openfact.provider.ProviderType;
import org.openfact.ubl.ubl21.qualifiers.UBLDocumentType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@ProviderType("sunat")
@UBLDocumentType("VOIDED_DOCUMENTS")
public class SunatUBLVoidedDocumentIDGenerator implements UBLVoidedDocumentIDGenerator {

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public String generateID(OrganizationModel organization, Object o) {
        TipoComprobante voidedDocumentCode = TipoComprobante.BAJA;
        DocumentModel lastVoidedDocument = null;
        ScrollModel<DocumentModel> voidedDocuments = documentProvider.createQuery(organization)
                .documentType(SunatDocumentType.VOIDED_DOCUMENTS.toString())
                .entityQuery()
                .orderByDesc(DocumentModel.DOCUMENT_ID)
                .resultScroll()
                .getScrollResult(10);

        Iterator<DocumentModel> iterator = voidedDocuments.iterator();

        Pattern pattern = Pattern.compile(voidedDocumentCode.getMask());
        while (iterator.hasNext()) {
            DocumentModel voidedDocument = iterator.next();
            String documentId = voidedDocument.getDocumentId();

            Matcher matcher = pattern.matcher(documentId);
            if (matcher.find()) {
                lastVoidedDocument = voidedDocument;
                break;
            }
        }

        int number = 0, secuence = 0;
        if (lastVoidedDocument != null) {
            String[] splits = lastVoidedDocument.getDocumentId().split("-");
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
        documentId.append(voidedDocumentCode.getMask().substring(2, 4));
        documentId.append("-");
        documentId.append(nextSeries);
        documentId.append("-");
        documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 5, "0"));

        return documentId.toString();
    }
}
