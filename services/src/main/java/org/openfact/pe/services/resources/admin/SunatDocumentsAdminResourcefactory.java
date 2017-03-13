package org.openfact.pe.services.resources.admin;

import org.openfact.services.resource.OrganizationAdminResourceProvider;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

@Stateless
@Consumes(MediaType.APPLICATION_JSON)
public class SunatDocumentsAdminResourcefactory implements OrganizationAdminResourceProvider {

    private static final String PATH = "sunat";

    @Inject
    private SunatDocumentsAdminResource resource;

    @Override
    public String getPath() {
        return PATH;
    }

    public Object getResource() {
        return resource;
    }

}