package org.openfact.pe.models;

import java.util.List;

import org.openfact.models.ubl.SendEventModel;

public interface RetentionSendEventModel  extends SendEventModel {
	List<RetentionModel> getRetentions();

	void setRetentions(List<RetentionModel> retentions);
}
