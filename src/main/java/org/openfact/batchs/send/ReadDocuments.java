package org.openfact.batchs.send;

import org.openfact.models.DocumentProvider;

import javax.batch.api.chunk.ItemReader;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class ReadDocuments implements ItemReader {

    @Inject
    private DocumentProvider documentProvider;

    @Override
    public void open(Serializable checkpoint) throws Exception {
//        documentProvider.createQuery()
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Object readItem() throws Exception {
        return null;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }

}
