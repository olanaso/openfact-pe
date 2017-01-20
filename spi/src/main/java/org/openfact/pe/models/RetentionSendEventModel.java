package org.openfact.pe.models;

import org.openfact.models.SendEventModel;

public interface RetentionSendEventModel  extends SendEventModel {
	RetentionModel getRetention();

	void setRetention(RetentionModel retention);
}
