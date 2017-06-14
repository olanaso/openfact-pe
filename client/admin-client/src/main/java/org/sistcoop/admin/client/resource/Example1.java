package org.sistcoop.admin.client.resource;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.openfact.representations.idm.DocumentRepresentation;
import org.openfact.representations.idm.OrganizationRepresentation;
import org.openfact.representations.idm.search.SearchResultsRepresentation;
import org.sistcoop.admin.client.OpenfactClient;

import java.util.List;

public class Example1 {

    public static void main(String args[]) {
        // Openfact Server Url
        String serverUrl = "http://openfact-openfact.192.168.42.244.nip.io";

        // Openfact Authentication
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://keycloak-openfact.192.168.42.244.nip.io/auth")
                .realm("ahren")
                .username("carlos")
                .password("carlos")
                .clientId("openfact-client")
                .clientSecret("9df24011-daef-4c8f-8282-f82d101c90ff")
                .build();

        // Openfact Client
        OpenfactClient openfact = OpenfactClient.buildTarget(serverUrl, keycloak);

        // Listing all allowed organizations
        List<OrganizationRepresentation> organizations = openfact.organizations().findAll();
        for (OrganizationRepresentation organization : organizations) {
            System.out.println("Organization " + organization.getOrganization());
            System.out.println("---------------------------------------------");

            // Actualizando datos de una organizacion
            OrganizationResource organizationResource = openfact.organizations()
                    .organization(organization.getOrganization());
            OrganizationRepresentation patch = new OrganizationRepresentation();
            patch.setDescription("My Description");
            organizationResource.update(patch);

            // Listando los documentos asociados a la organizacion
            DocumentsResource documentsResource = organizationResource.documents();
            SearchResultsRepresentation<DocumentRepresentation> documents = documentsResource.search("");
            System.out.println("Se encontraron " + documents.getTotalSize() + " documentos: ");
            for (DocumentRepresentation document : documents.getItems()) {
                System.out.println("ID:" + document.getDocumentId());
            }

            System.out.println("---------------------------------------------");
        }
    }

}