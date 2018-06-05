package org.openfact.pe.ws.sunat;

import jodd.io.ZipBuilder;
import org.jboss.logging.Logger;
import org.openfact.models.*;
import org.openfact.models.jpa.JpaOrganizationProvider;
import org.openfact.models.types.DestinyType;
import org.openfact.models.types.DocumentType;
import org.openfact.models.types.InternetMediaType;
import org.openfact.models.types.SendEventStatus;
import org.openfact.models.utils.TypeToModel;
import org.openfact.pe.models.SunatSendEventException;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.ubl.types.SunatCodigoErrores;
import org.openfact.pe.ubl.types.SunatRequiredAction;
import org.openfact.pe.ubl.types.TipoComprobante;
import org.openfact.pe.ubl.types.TipoDocumentoRelacionadoBaja;
import org.openfact.pe.ws.ServiceConfigurationException;
import org.openfact.services.ErrorResponse;
import org.openfact.services.ModelErrorResponseException;
import pe.gob.sunat.UsernameTokenCallbackHandler;
import service.sunat.gob.pe.billconsultservice.StatusResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.*;

@Stateless
public class SunatSenderManager {

    private static final Logger logger = Logger.getLogger(SunatSenderManager.class);

    @Inject
    private FileProvider fileProvider;

    public SendEventModel sendBillCPE(OrganizationModel organization, DocumentModel document, String fileName) throws ModelInsuficientData, SendEventException {
        return sendBill(organization, document, fileName, organization.getAttribute(SunatConfig.SUNAT_ADDRESS_1));
    }

    public SendEventModel sendBillAddressOCPE(OrganizationModel organization, DocumentModel document, String fileName) throws ModelInsuficientData, SendEventException {
        return sendBill(organization, document, fileName, organization.getAttribute(SunatConfig.SUNAT_ADDRESS_2));
    }

    public SendEventModel sendSummaryCPE(OrganizationModel organization, DocumentModel document, String fileName) throws ModelInsuficientData, SendEventException {
        String sunatAddress = organization.getAttribute(SunatConfig.SUNAT_ADDRESS_1);
        return sendSummary(organization, document, fileName, sunatAddress);
    }

    public SendEventModel sendSummaryOCPE(OrganizationModel organization, DocumentModel document, String fileName) throws ModelInsuficientData, SendEventException {
        String sunatAddress = organization.getAttribute(SunatConfig.SUNAT_ADDRESS_2);
        return sendSummary(organization, document, fileName, sunatAddress);
    }

    public service.sunat.gob.pe.billservice.StatusResponse checkTicketCPE(OrganizationModel organization, String ticket) throws SunatSendEventException {
        String sunatAddress = organization.getAttribute(SunatConfig.SUNAT_ADDRESS_1);
        return checkTicket(organization, ticket, sunatAddress);
    }

    public service.sunat.gob.pe.billservice.StatusResponse checkTicketOCPE(OrganizationModel organization, String ticket) throws SunatSendEventException {
        String sunatAddress = organization.getAttribute(SunatConfig.SUNAT_ADDRESS_2);
        return checkTicket(organization, ticket, sunatAddress);
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
                sendEvent.setAttribute(entry.getKey(), entry.getValue().substring(0, Math.min(entry.getValue().length(), 240)));
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
                            sendEvent.setAttribute(entry.getKey(), entry.getValue().substring(0, Math.min(entry.getValue().length(), 240)));
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

    private SendEventModel sendSummary(OrganizationModel organization, DocumentModel document, String fileName, String sunatAddress) throws ModelInsuficientData, SendEventException {
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

    public ConsultaTicketResult checkTicket(OrganizationModel organization, DocumentModel document) {
        Map<String, String> params = new HashMap<>();
        params.put(DocumentModel.SEND_EVENT_DESTINY, DestinyType.THIRD_PARTY.toString());
        params.put(DocumentModel.SEND_EVENT_STATUS, SendEventStatus.SUCCESS.toString());

        try {
            List<SendEventModel> sendEvents = document.searchForSendEvent(params);
            if (sendEvents.isEmpty()) {
                return ConsultaTicketResult.ERROR;
            } else {
                SendEventModel sendEvent = sendEvents.get(0); // obtener el primero

                String ticket = sendEvent.getAttribute(SunatTypeToModel.NUMERO_TICKET);
                if (ticket == null) {
                    return ConsultaTicketResult.ERROR;
                }

                service.sunat.gob.pe.billservice.StatusResponse result;
                if (document.getDocumentId().toUpperCase().startsWith("RR") || document.getDocumentId().toUpperCase().startsWith("RP")) {
                    result = checkTicketOCPE(organization, ticket);
                } else {
                    result = checkTicketCPE(organization, ticket);
                }

                sendEvent.setAttribute("StatusCode:", result.getStatusCode());

                if (result.getStatusCode().equalsIgnoreCase("0") || result.getStatusCode().equalsIgnoreCase("00")) {
                    sendEvent.setAttribute("StatusResponse:", ConsultaTicketResult.PROCESO_CORRECTAMENTE.getMensaje());

                    FileModel file = fileProvider.createFile(organization, "R" + ticket + ".zip", result.getContent());
                    sendEvent.attachFile(file);

                    // Remove required action
                    document.removeRequiredAction(SunatRequiredAction.CONSULTAR_TICKET.toString());
                    document.removeRequiredAction("VERIFICAR");

                    return ConsultaTicketResult.PROCESO_CORRECTAMENTE;
                } else if (result.getStatusCode().equalsIgnoreCase("98") || result.getStatusCode().equalsIgnoreCase("0098")) {
                    sendEvent.setAttribute("StatusResponse:", ConsultaTicketResult.EN_PROCESO.getMensaje());

                    return ConsultaTicketResult.EN_PROCESO;
                } else if (result.getStatusCode().equalsIgnoreCase("99") || result.getStatusCode().equalsIgnoreCase("0099")) {
                    FileModel file = fileProvider.createFile(organization, "R" + ticket + ".zip", result.getContent());
                    sendEvent.attachFile(file);

                    sendEvent.setAttribute("StatusResponse:", ConsultaTicketResult.PROCESO_CON_ERRORES.getMensaje());

                    document.addRequiredAction("VERIFICAR");
                    return ConsultaTicketResult.PROCESO_CON_ERRORES;
                } else {
                    return ConsultaTicketResult.ERROR;
                }
            }
        } catch (SunatSendEventException e) {
            logger.error("Error en la consulta de la sunat. Los servicios de la SUNAT no estan disponibles");
            return ConsultaTicketResult.ERROR;
        } catch (ModelException e) {
            logger.error("Error en guardar el archivo que se recuperó en en la consulta del Ticket");
            return ConsultaTicketResult.ERROR;
        }
    }

    private service.sunat.gob.pe.billservice.StatusResponse checkTicket(OrganizationModel organization, String ticket, String sunatAddress) throws SunatSendEventException {
        String sunatUsername = organization.getAttribute(SunatConfig.SUNAT_USERNAME);
        String sunatPassword = organization.getAttribute(SunatConfig.SUNAT_PASSWORD);

        Map<String, String> config = SunatSender.buildConfig()
                .address(sunatAddress)
                .username(sunatUsername)
                .password(sunatPassword).build();

        try {
            service.sunat.gob.pe.billservice.StatusResponse result = new SunatSender().getStatus(config, ticket);
            return result;
        } catch (ServiceConfigurationException e) {
            throw new SunatSendEventException("Ocurrio un error al consultar el numero de ticket");
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
