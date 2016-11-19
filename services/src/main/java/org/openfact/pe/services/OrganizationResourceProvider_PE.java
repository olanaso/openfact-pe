package org.openfact.pe.services;

import org.openfact.models.OpenfactSession;
import org.openfact.pe.services.resources.RootResource_PE;
import org.openfact.services.resource.OrganizationResourceProvider;

public class OrganizationResourceProvider_PE implements OrganizationResourceProvider {

    private OpenfactSession session;

    public OrganizationResourceProvider_PE(OpenfactSession session) {
        this.session = session;
    }

    public Object getResource() {
        return new RootResource_PE(session);
    }

    public void close() {
    }

}