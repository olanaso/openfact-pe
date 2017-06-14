package org.sistcoop.admin.client.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("/admin/organizations")
@Consumes(MediaType.APPLICATION_JSON)
public interface OrganizationsSunatResource extends OrganizationsResource {

    @Path("/{organization}")
    OrganizationSunatResource organization(@PathParam("organization") String organization);

}
