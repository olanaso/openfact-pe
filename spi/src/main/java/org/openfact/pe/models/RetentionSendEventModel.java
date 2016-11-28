package org.openfact.pe.models;

import org.openfact.ubl.SendEventModel;

public interface RetentionSendEventModel  extends SendEventModel {
	RetentionModel getRetention();

	void setRetention(RetentionModel retention);
}
