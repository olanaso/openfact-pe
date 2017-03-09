package org.openfact.pe.ubl.ubl21;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.KeyManager;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.ubl.UBLSignerProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.ParserConfigurationException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class SunatSignerUtils {

    @Inject
    private UBLSignerProvider signerProvider;

    @Inject
    private KeyManager keystore;

    public Element getSignToElement(OrganizationModel organization) {
        Document signedDocument = sign(organization);
        return signedDocument.getDocumentElement();
    }

    public Document getSignToDocument(OpenfactSession session, OrganizationModel organization, Document document) {
        Document signedDocument = signerProvider.sign(document, organization);
        return signedDocument;
    }

    public void removeSignature(Document document) {
        NodeList nodeList = document.getDocumentElement().getElementsByTagName("ext:UBLExtension");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element chilElement = (Element) nodeList.item(i);
            chilElement.getParentNode().removeChild(chilElement);
        }
    }


    private static final String FACTORY = "DOM";
    private static final String PREFIX = "ds";

    public Document sign(OrganizationModel organization) {
        XMLSignatureFactory xmlSigFactory = XMLSignatureFactory.getInstance(FACTORY);

        // Certificate
        try {
            Document document = DocumentUtils.getEmptyDocument();
            DOMSignContext domSignCtx = new DOMSignContext(keystore.getActiveRsaKey(organization).getPrivateKey(), document);

            domSignCtx.setDefaultNamespacePrefix(PREFIX);
            Reference ref = xmlSigFactory.newReference("", xmlSigFactory.newDigestMethod(DigestMethod.SHA1, null),
                    Collections.singletonList(xmlSigFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)), null, null);
            SignedInfo signedInfo = xmlSigFactory.newSignedInfo(
                    xmlSigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                    xmlSigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

            KeyInfoFactory keyInfoFactory = xmlSigFactory.getKeyInfoFactory();
            List<X509Certificate> x509Content = new ArrayList<>();
            x509Content.add(keystore.getActiveRsaKey(organization).getCertificate());
            X509Data xdata = keyInfoFactory.newX509Data(x509Content);
            KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(xdata));

            // Create a new XML Signature
            XMLSignature xmlSignature = xmlSigFactory.newXMLSignature(signedInfo, keyInfo, null, "Signature" + organization.getName(), null);
            xmlSignature.sign(domSignCtx);
            return document;
        } catch (NoSuchAlgorithmException e) {
            throw new ModelException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new ModelException(e);
        } catch (MarshalException e) {
            throw new ModelException(e);
        } catch (XMLSignatureException e) {
            throw new ModelException(e);
        } catch (ParserConfigurationException e) {
            throw new ModelException(e);
        }
    }
}
