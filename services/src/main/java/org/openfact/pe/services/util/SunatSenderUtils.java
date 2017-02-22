package org.openfact.pe.services.util;

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.openfact.file.InternetMediaType;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.models.enums.EmissionType;
import org.openfact.pe.services.send.ServiceConfigurationException;
import org.openfact.pe.services.send.ServiceWrapper;
import org.openfact.pe.services.sunat.BillService;

import com.sun.xml.ws.util.ByteArrayDataSource;

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
}
