package org.openfact.pe.models.utils;

import org.openfact.common.converts.StringUtils;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;

public class DocumentIdProvider_PE {

    public static String generatePerceptionDocumentId(OpenfactSession session,
            OrganizationModel organization) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getLastPerceptionByPattern(organization,
                CodigoTipoDocumento.PERCEPCION.getLenght(), CodigoTipoDocumento.PERCEPCION.getMask());

        int series = 0;
        int number = 0;
        if (perception != null) {
            String[] splits = perception.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = getNextNumber(number, 99_999_999);
        int nextSeries = getNextSerie(series, number, 999, 99_999_999);
        StringBuilder ID = new StringBuilder();
        ID.append(CodigoTipoDocumento.PERCEPCION.getMask().substring(0, 1));
        ID.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        ID.append("-");
        ID.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return ID.toString();
    }

    public static String generateRetentionDocumentId(OpenfactSession session,
            OrganizationModel organization) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getLastPerceptionByPattern(organization,
                CodigoTipoDocumento.RETENCION.getLenght(), CodigoTipoDocumento.RETENCION.getMask());

        int series = 0;
        int number = 0;
        if (perception != null) {
            String[] splits = perception.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = getNextNumber(number, 99_999_999);
        int nextSeries = getNextSerie(series, number, 999, 99_999_999);
        StringBuilder ID = new StringBuilder();
        ID.append(CodigoTipoDocumento.RETENCION.getMask().substring(0, 1));
        ID.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        ID.append("-");
        ID.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return ID.toString();
    }

    public static String generateSummaryDocumentDocumentId(OpenfactSession session,
            OrganizationModel organization) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getLastPerceptionByPattern(organization,
                CodigoTipoDocumento.RESUMEN_DIARIO.getLenght(), CodigoTipoDocumento.RESUMEN_DIARIO.getMask());

        int series = 0;
        int number = 0;
        if (perception != null) {
            String[] splits = perception.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = getNextNumber(number, 99_999_999);
        int nextSeries = getNextSerie(series, number, 999, 99_999_999);
        StringBuilder ID = new StringBuilder();
        ID.append(CodigoTipoDocumento.RESUMEN_DIARIO.getMask().substring(0, 1));
        ID.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        ID.append("-");
        ID.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return ID.toString();
    }

    public static String generateVoidedDocumentId(OpenfactSession session, OrganizationModel organization) {
        PerceptionProvider perceptionProvider = session.getProvider(PerceptionProvider.class);
        PerceptionModel perception = perceptionProvider.getLastPerceptionByPattern(organization,
                CodigoTipoDocumento.BAJA.getLenght(), CodigoTipoDocumento.BAJA.getMask());

        int series = 0;
        int number = 0;
        if (perception != null) {
            String[] splits = perception.getDocumentId().split("-");
            series = Integer.parseInt(splits[0].substring(1));
            number = Integer.parseInt(splits[1]);
        }

        int nextNumber = getNextNumber(number, 99_999_999);
        int nextSeries = getNextSerie(series, number, 999, 99_999_999);
        StringBuilder ID = new StringBuilder();
        ID.append(CodigoTipoDocumento.BAJA.getMask().substring(0, 1));
        ID.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
        ID.append("-");
        ID.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

        return ID.toString();
    }

    private static int getNextSerie(int currentSerie, int currenctNumber, int maxSerie, int maxNumber) {
        if (getNextNumber(currenctNumber, maxNumber) == 1) {
            return ++currentSerie;
        } else if (currentSerie < maxSerie) {
            return currentSerie;
        } else {
            throw new ModelException("El numero de numero y series se agoto");
        }
    }

    private static int getNextNumber(int currenctNumber, int maxNumber) {
        if (currenctNumber < maxNumber) {
            return ++currenctNumber;
        } else {
            return 1;
        }
    }

}
