package org.openfact.pe.services.util;

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.pe.services.send.ServiceWrapper;
import org.openfact.pe.services.sunat.BillService;

import com.sun.xml.ws.util.ByteArrayDataSource;

public class SunatSenderUtils {
	private Map<String, String> config;

	public SunatSenderUtils(OrganizationModel organization) {
		config = new HashMap<>();
		config.put("username", organization.getAssignedIdentificationId() + "MODDATOS");
		config.put("password", "MODDATOS");
		config.put("url", "https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService");
	}

	public byte[] sendBill(byte[] document, String fileName, InternetMediaType mediaType) {
		ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<BillService>(config);
		BillService client = (BillService) serviceWrapper.initWebService(BillService.class);
		DataSource dataSource = new ByteArrayDataSource(document, mediaType.getMimeType());
		DataHandler contentFile = new DataHandler(dataSource);
		// Send
		byte[] result = client.sendBill(fileName + mediaType.getExtension(), contentFile);
		return result;
	}

	public String sendSummary(byte[] document, String fileName, InternetMediaType mediaType) {
		ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<BillService>(config);
		BillService client = (BillService) serviceWrapper.initWebService(BillService.class);
		DataSource dataSource = new ByteArrayDataSource(document, mediaType.getMimeType());
		DataHandler contentFile = new DataHandler(dataSource);
		// Send
		String result = client.sendSummary(fileName + mediaType.getExtension(), contentFile);
		return result;
	}

	public String sendPack(byte[] document, String fileName, InternetMediaType mediaType) {
		ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<BillService>(config);
		BillService client = (BillService) serviceWrapper.initWebService(BillService.class);
		DataSource dataSource = new ByteArrayDataSource(document, mediaType.getMimeType());
		DataHandler contentFile = new DataHandler(dataSource);
		// Send
		String result = client.sendPack(fileName + mediaType.getExtension(), contentFile);
		return result;
	}

	public byte[] getStatus(String ticket) {
		// Map<String, Object> result = new HashMap<>();
		// ServiceWrapper<BillService> serviceWrapper = new
		// ServiceWrapper<BillService>(config);
		// BillService client = (BillService)
		// serviceWrapper.initWebService(BillService.class);
		// // send
		// StatusResponse statusResponse = client.getStatus(ticket);
		// Integer inCode = new Integer(statusResponse.getStatusCode());
		// if (inCode.equals(Integer.valueOf(98))) {
		// result.put("warning",
		// "La transaccion " + ticket + ", aun se esta procesando con codigo " +
		// inCode.intValue());
		// } else {
		// byte[] send = statusResponse.getContent();
		// result.put("success", send);
		// }
		// return result;
		return null;
	}
}
