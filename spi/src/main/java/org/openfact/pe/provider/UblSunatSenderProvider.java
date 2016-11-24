package org.openfact.pe.provider;

import java.util.Map;

import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.InternetMediaType;
import org.openfact.ubl.UblSenderException;
import org.openfact.ubl.UblSenderProvider;

public interface UblSunatSenderProvider extends UblSenderProvider {

	public String sendSummary(OrganizationModel organization, byte[] document, String fileName,
			InternetMediaType mediaType) throws UblSenderException;

	public String sendPack(OrganizationModel organization, byte[] document, String fileName,
			InternetMediaType mediaType) throws UblSenderException;

	public Map<String, Object> getStatus(OrganizationModel organization, String ticket) throws UblSenderException;
}
