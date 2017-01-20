package org.openfact.pe.models;

import org.openfact.models.SendEventModel;

public interface PerceptionSendEventModel extends SendEventModel {
	PerceptionModel getPerception();

	void setPerception(PerceptionModel perception);
}
