package org.openfact.pe.services.sender;

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.pe.provider.UblSunatSenderProvider;
import org.openfact.pe.services.send.ServiceWrapper;
import org.openfact.pe.services.sunat.BillService;
import org.openfact.pe.services.sunat.StatusResponse;
import org.openfact.ubl.UblSenderException;

import com.sun.xml.ws.util.ByteArrayDataSource;

/**
 * Created by lxpary on 21/11/16.
 */
public class SunatSenderProvider implements UblSunatSenderProvider {

	private OpenfactSession session;

	public SunatSenderProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {

	}

	private byte[] sendBill(OrganizationModel organization, byte[] document, String fileName,
			InternetMediaType mediaType, String wsUrl) throws UblSenderException {
		ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<BillService>(organization.getUblSenderConfig(),
				wsUrl);
		BillService client = (BillService) serviceWrapper.initWebService(BillService.class);
		DataSource dataSource = new ByteArrayDataSource(document, mediaType.getMimeType());
		DataHandler contentFile = new DataHandler(dataSource);
		// Send
		byte[] result = client.sendBill(fileName + mediaType.getExtension(), contentFile);
		return result;
	}

	@Override
	public Map<String, Object> sendDocument(OrganizationModel organization, byte[] document, String fileName,
			InternetMediaType mediaType) throws UblSenderException {
		Map<String, Object> result = new HashMap<>();
		String wsUrl = organization.getUblSenderConfig().get("wsUrl");
		byte[] send = sendBill(organization, document, fileName, mediaType, wsUrl);
		result.put("success", send);
		return result;
	}

	@Override
	public String sendSummary(OrganizationModel organization, byte[] document, String fileName,
			InternetMediaType mediaType) throws UblSenderException {
		String wsUrl = organization.getUblSenderConfig().get("wsUrl");
		ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<BillService>(organization.getUblSenderConfig(),
				wsUrl);
		BillService client = (BillService) serviceWrapper.initWebService(BillService.class);
		DataSource dataSource = new ByteArrayDataSource(document, mediaType.getMimeType());
		DataHandler contentFile = new DataHandler(dataSource);
		// Send
		String result = client.sendSummary(fileName + mediaType.getExtension(), contentFile);
		return result;
	}

	@Override
	public String sendPack(OrganizationModel organization, byte[] document, String fileName,
			InternetMediaType mediaType) throws UblSenderException {
		String wsUrl = organization.getUblSenderConfig().get("wsUrl");
		ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<BillService>(organization.getUblSenderConfig(),
				wsUrl);
		BillService client = (BillService) serviceWrapper.initWebService(BillService.class);
		DataSource dataSource = new ByteArrayDataSource(document, mediaType.getMimeType());
		DataHandler contentFile = new DataHandler(dataSource);
		// Send
		String result = client.sendPack(fileName + mediaType.getExtension(), contentFile);
		return result;
	}

	@Override
	public Map<String, Object> getStatus(OrganizationModel organization, String ticket) throws UblSenderException {
		String wsUrl = organization.getUblSenderConfig().get("wsUrl");
		Map<String, Object> result = new HashMap<>();
		ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<BillService>(organization.getUblSenderConfig(),
				wsUrl);
		BillService client = (BillService) serviceWrapper.initWebService(BillService.class);
		// send
		StatusResponse statusResponse = client.getStatus(ticket);
		Integer inCode = new Integer(statusResponse.getStatusCode());
		if (inCode.equals(Integer.valueOf(98))) {
			result.put("warning",
					"La transaccion " + ticket + ", aun se esta procesando con codigo " + inCode.intValue());
		} else {
			byte[] send = statusResponse.getContent();
			result.put("success", send);
		}
		return result;
	}

}
