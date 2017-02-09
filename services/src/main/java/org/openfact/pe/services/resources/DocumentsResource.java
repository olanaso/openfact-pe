package org.openfact.pe.services.resources;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.openfact.file.FileModel;
import org.openfact.file.InternetMediaType;
import org.openfact.models.DocumentModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.SendEventModel;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.SendEventStatus;
import org.openfact.services.ErrorResponse;
import org.openfact.services.resources.admin.AdminEventBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author carlosthe19916@sistcoop.com
 */
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentsResource {

    protected static final Logger logger = Logger.getLogger(DocumentsResource.class);

    private final OpenfactSession session;
    private final OrganizationModel organization;
    private final AdminEventBuilder adminEvent;

    public DocumentsResource(OpenfactSession session, OrganizationModel organization, AdminEventBuilder adminEvent) {
        this.session = session;
        this.organization = organization;
        this.adminEvent = adminEvent;
    }

    @GET
    @Path("{documentId}/cdr")
    @NoCache
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getCdr(@PathParam("documentId") final String documentId) {
        DocumentModel document = session.documents().getDocumentById(documentId, organization);
        if (document == null) {
            return ErrorResponse.error("Invoice not found", Response.Status.NOT_FOUND);
        }

        Map<String, String> params = new HashMap<>();
        params.put(DocumentModel.SEND_EVENT_DESTINY, DestinyType.THIRD_PARTY.toString());
        params.put(DocumentModel.SEND_EVENT_STATUS, SendEventStatus.SUCCESS.toString());

        List<SendEventModel> sendEvents = document.searchForSendEvent(params, 0, 2);
        if (sendEvents.isEmpty()) {
            return ErrorResponse.error("No se encontro un evio valido a SUNAT", Response.Status.BAD_REQUEST);
        } else {
            SendEventModel sendEvent = sendEvents.get(0);
            List<FileModel> responseFiles = sendEvent.getAttachedFiles();
            if (responseFiles.isEmpty()) {
                return ErrorResponse.error("Cdr no encontrado", Response.Status.NOT_FOUND);
            } else if (responseFiles.size() > 1) {
                return ErrorResponse.error("Se encontraron mas de un cdr, no se puede identificar el valido", Response.Status.CONFLICT);
            } else {
                FileModel cdrFile = responseFiles.get(0);
                ResponseBuilder response = Response.ok(cdrFile.getFile());
                String internetMediaType = InternetMediaType.getMymeTypeFromExtension(cdrFile.getExtension());
                if (internetMediaType != null && !internetMediaType.isEmpty()) response.type(internetMediaType);
                response.header("content-disposition", "attachment; filename=\"" + cdrFile.getFileName() + "\"");
                return response.build();
            }
        }
    }
}