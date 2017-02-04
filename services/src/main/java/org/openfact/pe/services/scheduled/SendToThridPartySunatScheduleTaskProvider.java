package org.openfact.pe.services.scheduled;

import org.openfact.models.*;
import org.openfact.models.enums.RequiredAction;
import org.openfact.pe.services.managers.SunatDocumentManager;

import java.util.Iterator;
import java.util.List;

public class SendToThridPartySunatScheduleTaskProvider implements OrganizationScheduleTaskProvider {

	public static final String JOB_NAME = "SENT_TO_THRID_PARTY_SUNAT";

	private int RETRY = 20;

	protected boolean isActive;

	public SendToThridPartySunatScheduleTaskProvider(boolean isActive, int retries) {
		this.isActive = isActive;
		this.RETRY = retries;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void run(OpenfactSession session, OrganizationModel organization) throws JobException {
		// Start Job
		long startTime = System.currentTimeMillis();
		long readCount = 0;

		readCount += sendDocuments(session, organization);

		long endTime = System.currentTimeMillis();

		// Save Report
		JobReportModel jobReportModel = session.jobReports().createJobReport(organization, JOB_NAME);

		// Metrics
		jobReportModel.setStartTime(startTime);
		jobReportModel.setEndTime(endTime);
		jobReportModel.setDuration(endTime - startTime);
		jobReportModel.setReadCount(readCount);
	}

	private long sendDocuments(OpenfactSession session, OrganizationModel organization) {
		long readCount = 0;

		ScrollModel<List<DocumentModel>> scroll = session.documents().getDocumentScroll(organization, 1000, RequiredAction.SEND_TO_TRIRD_PARTY.toString());
		Iterator<List<DocumentModel>> iterator = scroll.iterator();

		while (iterator.hasNext()) {
			List<DocumentModel> documents = iterator.next();

			readCount += documents.size();

			documents.stream()
					.filter(p -> p.getRequiredActions().contains(RequiredAction.SEND_TO_TRIRD_PARTY.toString()))
					.filter(p -> p.getSendEvents().size() < RETRY).forEach(c -> {
						SunatDocumentManager manager = new SunatDocumentManager(session);
						try {
							manager.sendToThirdParty(organization, c);
							c.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
						} catch (ModelInsuficientData e) {
							throw new JobException("Insufient data to send", e);
						} catch (SendException e) {
							throw new JobException("error on execute job", e);
						}
					});
		}

		return readCount;
	}

}
