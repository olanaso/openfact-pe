package org.openfact.pe.ws.sunat;

import jodd.io.ZipBuilder;
import org.openfact.models.*;
import org.openfact.models.types.DestinyType;
import org.openfact.models.types.InternetMediaType;
import org.openfact.models.types.SendEventStatus;
import org.openfact.pe.models.SunatSendEventException;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.ubl.types.SunatCodigoErrores;
import org.openfact.pe.ws.ServiceConfigurationException;
import pe.gob.sunat.UsernameTokenCallbackHandler;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class SunatSenderManager {

    @Inject
    private FileProvider fileProvider;

    public SendEventModel sendBillAddress1(OrganizationModel organization, DocumentModel document, String fileName) throws ModelInsuficientData, SendEventException {
        return sendBill(organization, document, fileName, organization.getAttribute(SunatConfig.SUNAT_ADDRESS_1));
    }

    public SendEventModel sendBillAddress2(OrganizationModel organization, DocumentModel document, String fileName) throws ModelInsuficientData, SendEventException {
        return sendBill(organization, document, fileName, organization.getAttribute(SunatConfig.SUNAT_ADDRESS_2));
    }

    public SendEventModel sendSummary(OrganizationModel organization, DocumentModel document, String fileName) throws ModelInsuficientData, SendEventException {
        String sunatAddress = organization.getAttribute(SunatConfig.SUNAT_ADDRESS_1);
        String sunatUsername = organization.getAttribute(SunatConfig.SUNAT_USERNAME);
        String sunatPassword = organization.getAttribute(SunatConfig.SUNAT_PASSWORD);

        if (sunatAddress == null) {
            throw new ModelInsuficientData("No se pudo encontrar una url de envio valida");
        }
        if (sunatUsername == null || sunatPassword == null) {
            throw new ModelInsuficientData("No se pudo encontrar un usuario y/o password valido en la organizacion");
        }

        try {
            String xmlFilename = fileName + ".xml";
            String zipFileName = fileName + ".zip";
            byte[] zipFile = ZipBuilder.createZipInMemory()
                    .add(document.getXmlAsFile().getFile())
                    .path(xmlFilename)
                    .save()
                    .toBytes();

            Map<String, String> config = SunatSender.buildConfig()
                    .address(sunatAddress)
                    .username(sunatUsername)
                    .password(sunatPassword).build();

            String ticket = new SunatSender().sendSummary(config, zipFile, zipFileName, InternetMediaType.ZIP);

            SendEventModel sendEvent = document.addSendEvent(DestinyType.THIRD_PARTY);
            sendEvent.setResult(SendEventStatus.SUCCESS);
            sendEvent.setDescription("Document submitted successfully to SUNAT");
            sendEvent.setAttribute("address", sunatAddress);
            sendEvent.setAttribute("ticket", ticket);

            document.setSingleAttribute(SunatTypeToModel.NUMERO_TICKET, ticket);

            return sendEvent;
        } catch (SOAPFaultException e) {
            String faultCode = e.getFault().getFaultCode();
            throw new SunatSendEventException(SunatCodigoErrores.codigoErrores.getOrDefault(faultCode, e.getMessage()), e);
        } catch (Exception e) {
            throw new SendEventException("Could not generate send to third party", e);
        }
    }

    public byte[] checkTicket(OrganizationModel organization, String ticket) throws SunatSendEventException {
        String sunatAddress = organization.getAttribute(SunatConfig.SUNAT_ADDRESS_1);
        String sunatUsername = organization.getAttribute(SunatConfig.SUNAT_USERNAME);
        String sunatPassword = organization.getAttribute(SunatConfig.SUNAT_PASSWORD);

        Map<String, String> config = SunatSender.buildConfig()
                .address(sunatAddress)
                .username(sunatUsername)
                .password(sunatPassword).build();

        try {
            byte[] result = new SunatSender().getStatus(config, ticket);
            return result;
        } catch (ServiceConfigurationException e) {
            throw new SunatSendEventException("Ocurrio un error al consultar el numero de ticket");
        }
    }

    private SendEventModel sendBill(OrganizationModel organization, DocumentModel document, String fileName, String sunatAddress) throws ModelInsuficientData, SendEventException {
        String sunatUsername = organization.getAttribute(SunatConfig.SUNAT_USERNAME);
        String sunatPassword = organization.getAttribute(SunatConfig.SUNAT_PASSWORD);

        if (sunatAddress == null) {
            throw new ModelInsuficientData("No se pudo encontrar una url de envio valida");
        }
        if (sunatUsername == null || sunatPassword == null) {
            throw new ModelInsuficientData("No se pudo encontrar un usuario y/o password valido en la organizacion");
        }

        try {
            String xmlFilename = fileName + ".xml";
            String zipFileName = fileName + ".zip";
            byte[] zipFile = ZipBuilder.createZipInMemory()
                    .add(document.getXmlAsFile().getFile())
                    .path(xmlFilename)
                    .save()
                    .toBytes();


            Map<String, String> config = SunatSender.buildConfig()
                    .address(sunatAddress)
                    .username(sunatUsername)
                    .password(sunatPassword).build();
            byte[] response = new SunatSender().sendBill(config, zipFile, zipFileName, InternetMediaType.ZIP);

            SendEventModel sendEvent = document.addSendEvent(DestinyType.THIRD_PARTY);
            sendEvent.setResult(SendEventStatus.SUCCESS);
            sendEvent.setDescription("Document submitted successfully to SUNAT");
            sendEvent.setAttribute("address", sunatAddress);

            FileModel cdrFile = fileProvider.createFile(organization, "R" + zipFileName, response);
            sendEvent.attachFile(cdrFile);

            for (Map.Entry<String, String> entry : SunatResponseUtils.byteResponseToMap(response).entrySet()) {
                sendEvent.setAttribute(entry.getKey(), entry.getValue());
            }

            return sendEvent;
        } catch (SOAPFaultException e) {
            String faultCode = e.getFault().getFaultCode();
            throw new SunatSendEventException(SunatCodigoErrores.codigoErrores.getOrDefault(faultCode, e.getMessage()), e);
        } catch (Exception e) {
            throw new SendEventException("Could not generate send to third party", e);
        }
    }

    private void configureTimeout(pe.gob.sunat.service.BillService port) throws Exception {
        //Set timeout until a connection is established
        ((BindingProvider) port).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "6000");

        //Set timeout until the response is received
        ((BindingProvider) port).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "1000");
    }

    private void configureAddress(BindingProvider bindingProvider, String address) {
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void configureUser(BindingProvider bp, String username, String password) {
        List<Handler> handlers = new ArrayList<>();
        handlers.add(new UsernameTokenCallbackHandler(username, password));
        bp.getBinding().setHandlerChain(handlers);
    }

}
