package org.openfact.pe.services.resources;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.openfact.common.ClientConnection;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.StorageFileModel;
import org.openfact.models.StorageFileProvider;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.SunatStorageFileProvider;
import org.openfact.services.ServicesLogger;
import org.openfact.services.resources.admin.AdminEventBuilder;
import org.openfact.services.resources.admin.OrganizationAuth;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by lxpary on 28/12/16.
 */

public class SunatStorageFilesAdminResource {
    private static final ServicesLogger logger = ServicesLogger.LOGGER;

    @Context
    protected UriInfo uriInfo;
    @Context
    protected OpenfactSession session;
    @Context
    protected ClientConnection clientConnection;
    @Context
    protected HttpHeaders headers;
    protected OrganizationModel organization;

    public SunatStorageFilesAdminResource(OpenfactSession session, OrganizationModel organization) {
        this.session = session;
        this.organization = organization;
    }
    @GET
    @Path("{idFile}")
    @NoCache
    public Response getFile(@PathParam("idFile") String idFile) {

        SunatStorageFileProvider storageFileProvider = session.getProvider(SunatStorageFileProvider.class);
        StorageFileModel file = storageFileProvider.getFileById(organization, idFile);
        if (file == null) {
            throw new NotFoundException("File not found");
        }

        Response.ResponseBuilder response = Response.ok(file.getFile());
        response.type(file.getMimeType());
        response.header("content-disposition", "attachment; filename=\"" + file.getFileName() + "\"");

        return response.build();
    }

}
