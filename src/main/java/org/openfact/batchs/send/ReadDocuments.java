package org.openfact.batchs.send;

import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.types.DocumentRequiredAction;

import javax.batch.api.chunk.ItemReader;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
public class ReadDocuments implements ItemReader {

    @Inject
    private DocumentProvider documentProvider;

    private List<DocumentModel> resultList;
    private int readPosition;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        resultList = documentProvider.getAllClosedDocuments(DocumentRequiredAction.SEND_TO_THIRD_PARTY.toString());
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public Object readItem() throws Exception {
        if (readPosition >= resultList.size()) {
            return null;
        }
        return resultList.get(readPosition++);
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }

}
