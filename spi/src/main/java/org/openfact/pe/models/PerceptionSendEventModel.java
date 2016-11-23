package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.ubl.SendEventModel;

public interface PerceptionSendEventModel extends SendEventModel {
	List<PerceptionModel> getPerceptions();

	void setPerceptions(List<PerceptionModel> perceptions);
}
