package org.openfact.pe.spi;

import org.openfact.Config.Scope;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OpenfactSessionFactory;
import org.openfact.ubl.SignerProvider;
import org.openfact.ubl.SignerProviderFactory;

public class SunatSignerProviderFactory implements SignerProviderFactory {

    @Override
    public SignerProvider create(OpenfactSession session) {
        return new SunatSignerProvider(session);
    }

    @Override
    public void init(Scope config) {
    }

    @Override
    public void postInit(OpenfactSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return "sunat";
    }

}
