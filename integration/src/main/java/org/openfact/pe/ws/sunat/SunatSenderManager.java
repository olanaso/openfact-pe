package org.openfact.pe.ws.sunat;

import jodd.io.ZipBuilder;
import org.openfact.models.*;
import org.openfact.models.types.DestinyType;
import org.openfact.models.types.DocumentType;
import org.openfact.models.types.InternetMediaType;
import org.openfact.models.types.SendEventStatus;
import org.openfact.models.utils.TypeToModel;
import org.openfact.pe.models.SunatSendEventException;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.ubl.types.SunatCodigoErrores;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.pe.ws.ServiceConfigurationException;
import pe.gob.sunat.UsernameTokenCallbackHandler;
import service.sunat.gob.pe.billconsultservice.StatusResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public StatusResponse getStatusCdr(OrganizationModel organization, DocumentModel document) throws SunatSendEventException {
//        String sunatAddress = organization.getAttribute(SunatConfig.SUNAT_ADDRESS_3);
        String sunatAddress = "https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService";
        String sunatUsername = organization.getAttribute(SunatConfig.SUNAT_USERNAME);
        String sunatPassword = organization.getAttribute(SunatConfig.SUNAT_PASSWORD);

        Map<String, String> config = SunatSender.buildConfig()
                .address(sunatAddress)
                .username(sunatUsername)
                .password(sunatPassword).build();

        DocumentType documentType = DocumentType.valueOf(document.getDocumentType());

        String tipoComprobante = null;
        switch (documentType) {
            case INVOICE:
                List<String> attribute = document.getAttribute(TypeToModel.INVOICE_TYPE_CODE);
                if (!attribute.isEmpty()) {
                    tipoComprobante = attribute.get(0);
                }
                break;
            case CREDIT_NOTE:
                tipoComprobante = TipoComprobante.NOTA_CREDITO.getCodigo();
                break;
            case DEBIT_NOTE:
                tipoComprobante = TipoComprobante.NOTA_DEBITO.getCodigo();
                break;
        }

        if (tipoComprobante == null) {
            throw new SunatSendEventException("El codigo del comprobante no es un tipo válido");
        }

        try {
            String[] serieNumero = document.getDocumentId().split("-");
            return new SunatSender().getStatusCdr(config, organization.getAssignedIdentificationId(), tipoComprobante, serieNumero[0], Integer.parseInt(serieNumero[1]));
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

            if (faultCode.equals("soap-env:Client.1033")) {
                StatusResponse statusResponse = getStatusCdr(organization, document);

                Optional<ConsultaCdrResponseType> optional = ConsultaCdrResponseType.searchByCodigo(statusResponse.getStatusCode());
                if (optional.isPresent()) {
                    ConsultaCdrResponseType consultaCdrResponseType = optional.get();

                    SendEventModel sendEvent = document.addSendEvent(DestinyType.THIRD_PARTY);
                    sendEvent.setDescription(statusResponse.getStatusMessage());
                    sendEvent.setAttribute("codigo", statusResponse.getStatusCode());
                    sendEvent.setAttribute("address", sunatAddress);
                    sendEvent.setAttribute("exito", consultaCdrResponseType.isTipo());
                    sendEvent.setResult(statusResponse.getStatusMessage().equalsIgnoreCase("La constancia existe") ? SendEventStatus.SUCCESS : SendEventStatus.ERROR);

                    byte[] bytes = statusResponse.getContent();

                    try {
                        for (Map.Entry<String, String> entry : SunatResponseUtils.byteResponseToMap(bytes).entrySet()) {
                            sendEvent.setAttribute(entry.getKey(), entry.getValue());
                        }
                    } catch (Exception e1) {
                        throw new SunatSendEventException(SunatCodigoErrores.codigoErrores.getOrDefault(faultCode, e.getMessage()), e);
                    }


                    FileModel cdrFile;
                    try {
                        cdrFile = fileProvider.createFile(organization, "R" + fileName + ".zip", bytes);
                    } catch (Exception e1) {
                        throw new SendEventException("Could not generate send to third party", e1);
                    }

                    sendEvent.attachFile(cdrFile);
                    return sendEvent;
                } else {
                    throw new SunatSendEventException(SunatCodigoErrores.codigoErrores.getOrDefault(faultCode, e.getMessage()), e);
                }
            } else {
                throw new SunatSendEventException(SunatCodigoErrores.codigoErrores.getOrDefault(faultCode, e.getMessage()), e);
            }

        } catch (Exception e) {
            throw new SendEventException("Could not generate send to third party", e);
        }
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
