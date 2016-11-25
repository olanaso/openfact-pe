package org.openfact.pe.spi;

import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import org.openfact.Config;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.*;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.ubl.DebitNoteIDGeneratorProvider;
import org.openfact.ubl.DebitNoteIDGeneratorProviderFactory;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SunatDebitNoteIDGeneratorProviderFactory implements DebitNoteIDGeneratorProviderFactory {

    @Override
    public DebitNoteIDGeneratorProvider create(OpenfactSession session) {
        return new SunatDebitNoteIDGeneratorProvider(session);
    }

    @Override
    public void init(Config.Scope config) {
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
