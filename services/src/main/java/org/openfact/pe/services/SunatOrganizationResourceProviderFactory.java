package org.openfact.pe.services;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.services.resource.OrganizationResourceProvider;
import org.openfact.services.resource.OrganizationResourceProviderFactory;

public class SunatOrganizationResourceProviderFactory implements OrganizationResourceProviderFactory {

    public String getId() {
        return "sunat";
    }

    public OrganizationResourceProvider create(OpenfactSession session) {
        return new SunatOrganizationResourceProvider(session);
    }

    public void init(Scope config) {
    }

    public void postInit(OpenfactSessionFactory factory) {
    }

    public void close() {
    }

}