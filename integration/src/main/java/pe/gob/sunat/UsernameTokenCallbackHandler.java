package pe.gob.sunat;

import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.rt.security.SecurityConstants;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.message.token.UsernameToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Map;
import java.util.Set;

public class UsernameTokenCallbackHandler implements SOAPHandler<SOAPMessageContext> {

    private String usernameText;
    private String passwordText;

    public UsernameTokenCallbackHandler(String usernameText, String passwordText) {
        this.usernameText = usernameText;
        this.passwordText = passwordText;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext soapMessageContext) {
        Boolean outboundProperty = (Boolean) soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
            try {
                SOAPEnvelope soapEnvelope = soapMessageContext.getMessage().getSOAPPart().getEnvelope();

                SOAPFactory soapFactory = SOAPFactory.newInstance();
                SOAPElement securityElement = soapFactory.createElement(getUsernameTokenElement(usernameText, passwordText));

                SOAPHeader soapHeader = soapEnvelope.getHeader();
                if (soapHeader == null) {
                    soapHeader = soapEnvelope.addHeader();
                }
                soapHeader.addChildElement(securityElement);
            } catch (Exception e) {
                throw new RuntimeException("Error on wsSecurityHandler: " + e.getMessage());
            }
        }
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    /**
     * Provide UsernameToken as a string.
     *
     * @param ctx
     * @return
     */
    public String getUsernameTokenString(Map<String, Object> ctx) {
        Document doc = DOMUtils.createDocument();
        String result = null;
        String username = (String) ctx.get(SecurityConstants.USERNAME);
        String password = (String) ctx.get(SecurityConstants.PASSWORD);
        if (username != null) {
            UsernameToken usernameToken = createWSSEUsernameToken(username, password, doc);
            result = toString(usernameToken.getElement().getFirstChild().getParentNode());
        }
        return result;
    }

    /**
     * @param username
     * @param password
     * @return
     */
    public String getUsernameTokenString(String username, String password) {
        Document doc = DOMUtils.createDocument();
        String result = null;
        if (username != null) {
            UsernameToken usernameToken = createWSSEUsernameToken(username, password, doc);
            result = toString(usernameToken.getElement().getFirstChild().getParentNode());
        }
        return result;
    }

    /**
     * Provide UsernameToken as a DOM Element.
     *
     * @param ctx
     * @return
     */
    public Element getUsernameTokenElement(Map<String, Object> ctx) {
        Document doc = DOMUtils.createDocument();
        Element result = null;
        UsernameToken usernameToken = null;
        String username = (String) ctx.get(SecurityConstants.USERNAME);
        String password = (String) ctx.get(SecurityConstants.PASSWORD);
        if (username != null) {
            usernameToken = createWSSEUsernameToken(username, password, doc);
            result = usernameToken.getElement();
        }
        return result;
    }

    /**
     * @param username
     * @param password
     * @return
     */
    public Element getUsernameTokenElement(String username, String password) {
        Document doc = DOMUtils.createDocument();
        Element result = null;
        UsernameToken usernameToken = null;
        if (username != null) {
            usernameToken = createWSSEUsernameToken(username, password, doc);
            result = usernameToken.getElement();
        }
        return result;
    }

    private UsernameToken createWSSEUsernameToken(String username, String password, Document doc) {
        UsernameToken usernameToken = new UsernameToken(true, doc, (password == null) ? null : WSConstants.PASSWORD_TEXT);
        usernameToken.setName(username);
        usernameToken.addWSUNamespace();
        usernameToken.addWSSENamespace();
        usernameToken.setID("id-" + username);

        if (password != null) {
            usernameToken.setPassword(password);
        }

        return usernameToken;
    }


    private String toString(Node node) {
        String str = null;

        if (node != null) {
            DOMImplementationLS lsImpl = (DOMImplementationLS) node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
            LSSerializer serializer = lsImpl.createLSSerializer();
            serializer.getDomConfig().setParameter("xml-declaration", false); //by default its true, so set it to false to get String without xml-declaration
            str = serializer.writeToString(node);
        }
        return str;
    }
}
