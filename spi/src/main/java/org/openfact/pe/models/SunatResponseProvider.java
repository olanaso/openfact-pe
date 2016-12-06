package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.SendResultType;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.provider.Provider;
import org.openfact.ubl.SendEventModel;

public interface SunatResponseProvider extends Provider {

	SunatResponseModel addSunatResponse(OrganizationModel organization, SendResultType type,
			SendEventModel sendEvent);	

	boolean removeSunatResponse(OrganizationModel organization, String id);

	boolean removeSunatResponse(OrganizationModel organization, SunatResponseModel sunatResponse);

	SunatResponseModel getSunatResponseById(OrganizationModel organization, String id);

	SunatResponseModel getSunatResponseByDocument(OrganizationModel organization, CodigoTipoDocumento documentType,
												  String id);

	List<SunatResponseModel> getSunatResponsesByDocument(OrganizationModel organization,
														 CodigoTipoDocumento documentType, String id);
}
