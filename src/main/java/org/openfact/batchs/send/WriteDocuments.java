package org.openfact.batchs.send;

import org.openfact.models.DocumentProvider;
import org.openfact.pe.services.resources.admin.SunatResourceManager;
import org.openfact.services.managers.DocumentManager;

import javax.batch.api.chunk.ItemReader;
import javax.batch.api.chunk.ItemWriter;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Named
public class WriteDocuments implements ItemWriter {

    @Inject
    private DocumentManager documentManager;

    @Override
    public void open(Serializable checkpoint) throws Exception {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void writeItems(List<Object> items) throws Exception {
//        documentManager.sendToThirdParty(organization, document);
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
