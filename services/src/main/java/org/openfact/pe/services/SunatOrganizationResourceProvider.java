package org.openfact.pe.services;

import org.openfact.models.OpenfactSession;
import org.openfact.pe.services.resources.SunatResources;
import org.openfact.services.resource.OrganizationResourceProvider;

public class SunatOrganizationResourceProvider implements OrganizationResourceProvider {

    private OpenfactSession session;

    public SunatOrganizationResourceProvider(OpenfactSession session) {
        this.session = session;
    }

    public Object getResource() {
        return new SunatResources(session);
    }

    public void close() {
    }

}