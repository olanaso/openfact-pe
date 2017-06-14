package org.sistcoop.admin.client.resource;

import org.sistcoop.admin.client.Openfact;

public class SunatAdapter {

    public static OrganizationsSunatResource from(Openfact openfact) {
        return openfact.proxy(OrganizationsSunatResource.class, "");
    }

}
