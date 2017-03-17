package org.openfact.pe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;
import org.openfact.OpenfactConfigResolver;

import javax.ejb.Stateless;
import java.io.IOException;
import java.net.URL;

@Stateless
public class SunatConfigResolver implements OpenfactConfigResolver {

    protected static final Logger logger = Logger.getLogger(SunatConfigResolver.class);

    @Override
    public JsonNode getNode() {
        JsonNode node = null;
        try {
            URL resource = Thread.currentThread().getContextClassLoader().getResource("META-INF/openfact-server-sunat.json");
            if (resource != null) {
                logger.info("Loading openfact-server.json config from " + resource);
                node = new ObjectMapper().readTree(resource);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
        return node;
    }

}
