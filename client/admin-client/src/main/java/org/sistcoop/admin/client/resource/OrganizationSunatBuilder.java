package org.sistcoop.admin.client.resource;

import org.sistcoop.admin.client.Openfact;

public class OrganizationSunatBuilder {

    private Openfact openfact;
    private String organizationName;

    public OrganizationSunatBuilder(Openfact openfact, String organizationName) {
        this.openfact = openfact;
        this.organizationName = organizationName;
    }

    public OrganizationSunatResource sunat() {
        return openfact.target("admin").proxy(OrganizationSunatResource.class);
    }

}
