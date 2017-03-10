package org.openfact.pe.ubl.ubl21.factories;

import com.helger.ubl21.UBL21NamespaceContext;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import org.openfact.models.ModelException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SunatMarshallerUtils {

    public static int getNextSerie(int currentSerie, int currenctNumber, int maxSerie, int maxNumber) {
        if (getNextNumber(currenctNumber, maxNumber) == 1) {
            return ++currentSerie;
        } else if (currentSerie < maxSerie) {
            return currentSerie;
        } else {
            throw new ModelException("Max series and number");
        }
    }

    public static int getNextNumber(int currenctNumber, int maxNumber) {
        if (currenctNumber < maxNumber) {
            return ++currenctNumber;
        } else {
            return 1;
        }
    }

    public static int getDateToNumber() {
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String formatted = sdf.format(localCalendar.getTime());
        return Integer.parseInt(formatted);
    }

    public static MapBasedNamespaceContext getBasedNamespaceContext(String defaultNamespace) {
        UBL21NamespaceContext namespace = UBL21NamespaceContext.getInstance();
        namespace.setMapping("ccts", "urn:un:unece:uncefact:documentation:2");
        namespace.setMapping("ds", "http://www.w3.org/2000/09/xmldsig#");
        namespace.setMapping("ext", "urn:oasis:names:specification:ubl21:schema:xsd:CommonExtensionComponents-2");
        namespace.setMapping("qdt", "urn:oasis:names:specification:ubl21:schema:xsd:QualifiedDatatypes-2");
        namespace.setMapping("sac", "urn:sunat:names:specification:ubl21:peru:schema:xsd:SunatAggregateComponents-1");
        namespace.setMapping("udt", "urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2");
        MapBasedNamespaceContext mapBasedNamespace = new MapBasedNamespaceContext();
        mapBasedNamespace.addMappings(namespace);
        mapBasedNamespace.setDefaultNamespaceURI(defaultNamespace);
        return mapBasedNamespace;
    }

    /**
     * Unmarshal a String with XML to a class
     *
     * @param xmlData a String with XML
     * @param clazz   The class to unmarshal to
     * @return The class
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(final String xmlData, final Class<T> clazz) {
        Object object = null;
        try {
            final JAXBContext context = JAXBContext.newInstance(clazz);

            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final StringReader reader = new StringReader(xmlData);
            object = unmarshaller.unmarshal(reader);
        } catch (final JAXBException e) {
            throw new ModelException(e);
        }
        return (T) object;
    }
}
