package org.openfact.pe.spi;

import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import org.openfact.Config;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.*;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.ubl.CreditNoteIDGeneratorProvider;
import org.openfact.ubl.CreditNoteIDGeneratorProviderFactory;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SunatCreditNoteIDGeneratorProviderFactory implements CreditNoteIDGeneratorProviderFactory {


    @Override
    public CreditNoteIDGeneratorProvider create(OpenfactSession session) {
        return new SunatCreditNoteIDGeneratorProvider(session);
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
