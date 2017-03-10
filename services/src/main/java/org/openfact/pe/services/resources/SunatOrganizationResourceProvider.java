package org.openfact.pe.services.resources;

import org.jboss.logging.Logger;
import org.openfact.pe.services.resources.admin.SunatAdminOrganizationResourceProvider;
import org.openfact.services.resource.OrganizationResourceProvider;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Stateless
@Consumes(MediaType.APPLICATION_JSON)
public class SunatOrganizationResourceProvider implements OrganizationResourceProvider {

    private static final String PATH = "sunat";
    private static final Logger logger = Logger.getLogger(SunatOrganizationResourceProvider.class);

    @Inject
    private SunatAdminOrganizationResourceProvider adminResource;

    @Inject
    private GenericTypesResource genericTypesResource;

    @Override
    public String getPath() {
        return PATH;
    }

    public Object getResource() {
        return this;
    }

    @Path("admin")
    public SunatAdminOrganizationResourceProvider getAdminResource() {
        return adminResource;
    }

    @Path("ubl21-extensions/generic-types")
    public GenericTypesResource getGenericTypesResource() {
        return genericTypesResource;
    }

}