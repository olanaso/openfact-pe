package org.openfact.pe.services.sender;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.pe.services.constants.CodigoTipoDocumento;
import org.openfact.pe.services.send.ServiceWrapper;
import org.openfact.pe.services.sunat.BillService;
import org.openfact.pe.services.sunat.StatusResponse;
import org.openfact.ubl.UblSenderException;
import org.openfact.ubl.UblSenderProvider;

import com.sun.xml.ws.util.ByteArrayDataSource;

/**
 * Created by lxpary on 21/11/16.
 */
public class SunatSenderProvider implements UblSenderProvider {

	private OpenfactSession session;

	public SunatSenderProvider(OpenfactSession session) {
		this.session = session;
	}

	@Override
	public void close() {

	}

	@Override
	public byte[] send(OrganizationModel organization, byte[] document, String fileName, String invoiceType,
			InternetMediaType mediaType, String wsUrl) throws UblSenderException {
		ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<BillService>(organization.getUblSenderConfig(),
				wsUrl);
		BillService client = (BillService) serviceWrapper.initWebService(BillService.class);
		DataSource dataSource = new ByteArrayDataSource(document, mediaType.getMimeType());
		DataHandler contentFile = new DataHandler(dataSource);
		// Send
		byte[] result = null;
		if (CodigoTipoDocumento.BOLETA.getCodigo().equals(invoiceType)) {
			client.sendSummary(fileName + mediaType.getExtension(), contentFile);
		} else {
			result = client.sendBill(fileName + mediaType.getExtension(), contentFile);
		}
		return result;
	}

	@Override
	public byte[] getStatus(OrganizationModel organization, String ticket, String wsUrl) throws UblSenderException {
		ServiceWrapper<BillService> serviceWrapper = new ServiceWrapper<BillService>(organization.getUblSenderConfig(),
				wsUrl);
		BillService client = (BillService) serviceWrapper.initWebService(BillService.class);
		StatusResponse statusResponse = client.getStatus(ticket);
		Integer inCode = new Integer(statusResponse.getStatusCode());
		if (inCode.equals(Integer.valueOf(98))) {
			throw new ModelException(
					"La transaccion " + ticket + ", aun se esta procesando con codigo " + inCode.intValue());
		} else {
			byte[] result = statusResponse.getContent();
			return result;
		}
	}

	@Override
	public byte[] voided(OrganizationModel organization, byte[] document, String fileName, String invoiceType,
			InternetMediaType mediaType, String wsUrl) throws UblSenderException {
		// TODO Auto-generated method stub
		return null;
	}
}
