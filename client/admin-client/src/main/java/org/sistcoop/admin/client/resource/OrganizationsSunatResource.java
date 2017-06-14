package org.sistcoop.admin.client.resource;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface OrganizationsSunatResource extends OrganizationsResource {

    @Path("/{organization}")
    OrganizationSunatResource organization(@PathParam("organization") String organization);

}
