package org.openfact.pe.services;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.services.resource.OrganizationResourceProvider;
import org.openfact.services.resource.OrganizationResourceProviderFactory;

public class OrganizationResourceProviderFactory_PE implements OrganizationResourceProviderFactory {

    public static final String ID = "pe";

    public String getId() {
        return ID;
    }

    public OrganizationResourceProvider create(OpenfactSession session) {
        return new OrganizationResourceProvider_PE(session);
    }

    public void init(Scope config) {
    }

    public void postInit(OpenfactSessionFactory factory) {
    }

    public void close() {
    }

}