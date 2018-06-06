package org.openfact.batchs.send;

import org.jboss.logging.Logger;
import org.openfact.models.DocumentModel;
import org.openfact.models.DocumentProvider;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.services.resources.admin.SunatResourceManager;
import org.openfact.services.managers.DocumentManager;
import org.openfact.truststore.FileTruststoreProvider;

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

    private static final Logger logger = Logger.getLogger(WriteDocuments.class);

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
        for (Object item : items) {
            DocumentModel document = (DocumentModel) item;
            try {
                OrganizationModel organization = document.getOrganization();
                documentManager.sendToThirdParty(organization, document);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
