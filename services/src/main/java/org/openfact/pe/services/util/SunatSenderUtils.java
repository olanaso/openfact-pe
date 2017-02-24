package org.openfact.pe.services.util;

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.openfact.file.FileModel;
import org.openfact.file.InternetMediaType;
import org.openfact.models.*;
import org.openfact.pe.models.enums.EmissionType;
import org.openfact.pe.models.enums.SunatRequiredAction;
import org.openfact.pe.models.utils.SunatTypeToModel;
import org.openfact.pe.services.send.ServiceConfigurationException;
import org.openfact.pe.services.send.ServiceWrapper;
import org.openfact.pe.services.sunat.BillService;

import com.sun.xml.ws.util.ByteArrayDataSource;
import org.openfact.pe.services.ubl.SunatConfig;

public class SunatSenderUtils {

    private Map<String, String> config = new HashMap<>();

    public SunatSenderUtils(String address, String username, String password) {
        this.config.put("address", address);
        this.config.put("username", username);
        this.config.put("password", password);
    }

    public Map<String, String> getDestiny() {
        return config;
    }

    public byte[] sendBill(byte[] file, String fileName, InternetMediaType mediaType) throws ServiceConfigurationException {
        ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<>(config);
        BillService client = serviceWrapper.initWebService(BillService.class);
        DataSource dataSource = new ByteArrayDataSource(file, mediaType.getMimeType());
        DataHandler dataHandler = new DataHandler(dataSource);

        return client.sendBill(fileName, dataHandler);
    }

    public String sendSummary(byte[] document, String fileName, InternetMediaType mediaType) throws ServiceConfigurationException {
        ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<>(config);
        BillService client = serviceWrapper.initWebService(BillService.class);
        DataSource dataSource = new ByteArrayDataSource(document, mediaType.getMimeType());
        DataHandler dataHandler = new DataHandler(dataSource);

        return client.sendSummary(fileName, dataHandler);
    }

    public String sendPack(byte[] document, String fileName, InternetMediaType mediaType) throws ServiceConfigurationException {
        ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<>(config);
        BillService client = serviceWrapper.initWebService(BillService.class);
        DataSource dataSource = new ByteArrayDataSource(document, mediaType.getMimeType());
        DataHandler dataHandler = new DataHandler(dataSource);

        return client.sendPack(fileName, dataHandler);
    }

    public byte[] getStatus(String ticket) throws ServiceConfigurationException {
        ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<>(config);
        BillService client = serviceWrapper.initWebService(BillService.class);

        return client.getStatus(ticket).getContent();
    }

    public static FileModel checkTicket(OpenfactSession session, OrganizationModel organization, DocumentModel document, SendEventModel sendEvent) throws SendException, ModelInsuficientData {
        String ticket = sendEvent.getAttribute(SunatTypeToModel.NUMERO_TICKET);

        if (ticket == null) {
            throw new ModelInsuficientData("ticket not found on send event");
        }

        String sunatAddress = organization.getAttribute(SunatConfig.SUNAT_ADDRESS_1);
        String sunatUsername = organization.getAttribute(SunatConfig.SUNAT_USERNAME);
        String sunatPassword = organization.getAttribute(SunatConfig.SUNAT_PASSWORD);

        try {
            byte[] result = new SunatSenderUtils(sunatAddress, sunatUsername, sunatPassword).getStatus(ticket);

            FileModel responseFileModel = session.files().createFile(organization, "R" + ticket + ".zip", result);
            sendEvent.attachFile(responseFileModel);

            // Remove required action
            document.removeRequiredAction(SunatRequiredAction.CONSULTAR_TICKET.toString());

            return responseFileModel;
        } catch (ServiceConfigurationException e) {
            throw new SendException("Could not generate send to third party", e);
        }
    }
}
