package org.openfact.pe.ws.sunat;

import org.openfact.models.types.InternetMediaType;
import org.openfact.pe.ws.ServiceConfigurationException;
import org.openfact.pe.ws.ServiceWrapper;
import service.sunat.gob.pe.billconsultservice.StatusResponse;
import service.sunat.gob.pe.billservice.BillService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.util.HashMap;
import java.util.Map;

public class SunatSender {

    public byte[] sendBill(Map<String, String> config, byte[] file, String fileName, InternetMediaType mediaType) throws ServiceConfigurationException {
        ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<>(config);
        BillService client = serviceWrapper.initWebService(BillService.class);
        DataSource dataSource = new ByteArrayDataSource(file, mediaType.getMimeType());
        DataHandler dataHandler = new DataHandler(dataSource);

        return client.sendBill(fileName, dataHandler, null);
    }

    public String sendSummary(Map<String, String> config, byte[] document, String fileName, InternetMediaType mediaType) throws ServiceConfigurationException {
        ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<>(config);
        BillService client = serviceWrapper.initWebService(BillService.class);
        DataSource dataSource = new ByteArrayDataSource(document, mediaType.getMimeType());
        DataHandler dataHandler = new DataHandler(dataSource);

        return client.sendSummary(fileName, dataHandler, null);
    }

    public String sendPack(Map<String, String> config, byte[] document, String fileName, InternetMediaType mediaType) throws ServiceConfigurationException {
        ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<>(config);
        BillService client = serviceWrapper.initWebService(BillService.class);
        DataSource dataSource = new ByteArrayDataSource(document, mediaType.getMimeType());
        DataHandler dataHandler = new DataHandler(dataSource);

        return client.sendPack(fileName, dataHandler, null);
    }

    public service.sunat.gob.pe.billservice.StatusResponse getStatus(Map<String, String> config, String ticket) throws ServiceConfigurationException {
        ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<>(config);
        BillService client = serviceWrapper.initWebService(BillService.class);

        return client.getStatus(ticket);
    }

    public StatusResponse getStatusCdr(Map<String, String> config, String ruc, String tipoComprobante, String serieComprobante, Integer numeroComprobante) throws ServiceConfigurationException {
        ServiceWrapper<service.sunat.gob.pe.billconsultservice.BillService> serviceWrapper = new ServiceWrapper<>(config);
        service.sunat.gob.pe.billconsultservice.BillService client = serviceWrapper.initWebService(service.sunat.gob.pe.billconsultservice.BillService.class);

        return client.getStatusCdr(ruc, tipoComprobante, serieComprobante, numeroComprobante);
    }

    public static SunatSenderConfig buildConfig() {
        return new SunatSenderConfig();
    }

    public static class SunatSenderConfig {

        private Map<String, String> config = new HashMap<>();

        public SunatSenderConfig address(String address) {
            this.config.put("address", address);
            return this;
        }

        public SunatSenderConfig username(String username) {
            this.config.put("username", username);
            return this;
        }

        public SunatSenderConfig password(String password) {
            this.config.put("password", password);
            return this;
        }

        public Map<String, String> build() {
            return config;
        }
    }
}
