package org.openfact.pe.models.utils;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.ubl.SignerProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by lxpary on 06/01/17.
 */
public class SunatSignerUtils {

    public static Element getSignToElement(OpenfactSession session, OrganizationModel organization) {
        SignerProvider signerProvider = session.getProvider(SignerProvider.class);

        try {
            Document document = DocumentUtils.getEmptyDocument();
            Document signedDocument = signerProvider.sign(document, organization);
            return signedDocument.getDocumentElement();
        } catch (ParserConfigurationException e) {
            return null;
        }
    }

    public static Document getSignToDocument(OpenfactSession session, OrganizationModel organization, Document document) {
        SignerProvider signerProvider = session.getProvider(SignerProvider.class);
        Document signedDocument = signerProvider.sign(document, organization);
        return signedDocument;
    }

    public static void removeSignature(Document document) {
        NodeList nodeList = document.getDocumentElement().getElementsByTagName("ext:UBLExtensions");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element chilElement = (Element) nodeList.item(i);
            document.removeChild(chilElement);

        }
    }
}
