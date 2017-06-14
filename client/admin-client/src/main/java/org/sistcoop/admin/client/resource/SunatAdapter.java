package org.sistcoop.admin.client.resource;

import org.sistcoop.admin.client.OpenfactClient;

public class SunatAdapter {

    public static OrganizationsSunatResource from(OpenfactClient openfact) {
        return openfact.getOpenfact().proxy(OrganizationsSunatResource.class, "");
    }

}
