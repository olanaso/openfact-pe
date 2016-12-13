package org.openfact.pe.services.scheduled;

import java.util.Iterator;
import java.util.List;

import org.openfact.models.CreditNoteModel;
import org.openfact.models.DebitNoteModel;
import org.openfact.models.InvoiceModel;
import org.openfact.models.JobException;
import org.openfact.models.JobReportModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.OrganizationScheduleTaskProvider;
import org.openfact.models.ScrollModel;
import org.openfact.models.enums.RequiredAction;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionProvider;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.RetentionProvider;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentProvider;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.models.VoidedDocumentProvider;
import org.openfact.pe.services.managers.PerceptionManager;
import org.openfact.pe.services.managers.RetentionManager;
import org.openfact.pe.services.managers.SummaryDocumentManager;
import org.openfact.pe.services.managers.VoidedDocumentManager;
import org.openfact.services.managers.CreditNoteManager;
import org.openfact.services.managers.DebitNoteManager;
import org.openfact.services.managers.InvoiceManager;
import org.openfact.ubl.SendEventModel;
import org.openfact.ubl.SendException;

public class SendToThridPartySunatScheduleTaskProvider implements OrganizationScheduleTaskProvider {

	public static final String JOB_NAME = "SENT_TO_THRID_PARTY_SUNAT";
	public static final int RETRY = 20;

	protected boolean isActive;

	public SendToThridPartySunatScheduleTaskProvider(boolean isActive) {
		this.isActive = isActive;
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

		readCount += sendInvoices(session, organization);
		readCount += sendCreditNotes(session, organization);
		readCount += sendDebitNotes(session, organization);
		readCount += sendPerceptions(session, organization);
		readCount += sendRetentions(session, organization);
		readCount += sendSummary(session, organization);
		readCount += sendVoided(session, organization);

		long endTime = System.currentTimeMillis();

		// Save Report
		JobReportModel jobReportModel = session.jobReports().createJobReport(organization, JOB_NAME);

		// Metrics
		jobReportModel.setStartTime(startTime);
		jobReportModel.setEndTime(endTime);
		jobReportModel.setDuration(endTime - startTime);
		jobReportModel.setReadCount(readCount);
	}

	private long sendInvoices(OpenfactSession session, OrganizationModel organization) {
		long readCount = 0;

		ScrollModel<List<InvoiceModel>> scroll = session.invoices().getInvoicesScroll(organization, 100,
				RequiredAction.SEND_TO_TRIRD_PARTY.toString());
		Iterator<List<InvoiceModel>> iterator = scroll.iterator();

		while (iterator.hasNext()) {
			List<InvoiceModel> invoices = iterator.next();

			readCount += invoices.size();

			invoices.stream()
					.filter(p -> p.getRequiredActions().contains(RequiredAction.SEND_TO_TRIRD_PARTY.toString()))
					.filter(p -> p.getSendEvents().size() < RETRY).forEach(c -> {
						InvoiceManager manager = new InvoiceManager(session);
						try {
							SendEventModel sendEvent = manager.sendToTrirdParty(organization, c);
							if (sendEvent.getResult()) {
								c.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
							}
						} catch (SendException e) {
							throw new JobException("error on execute job", e);
						}
					});
		}

		return readCount;
	}

	private long sendCreditNotes(OpenfactSession session, OrganizationModel organization) {
		long readCount = 0;

		ScrollModel<List<CreditNoteModel>> scroll = session.creditNotes().getCreditNotesScroll(organization, 100,
				RequiredAction.SEND_TO_TRIRD_PARTY.toString());
		Iterator<List<CreditNoteModel>> iterator = scroll.iterator();

		while (iterator.hasNext()) {
			List<CreditNoteModel> creditNotes = iterator.next();

			readCount += creditNotes.size();

			creditNotes.stream().filter(p -> p.getRequiredActions().contains(RequiredAction.SEND_TO_TRIRD_PARTY))
					.filter(p -> p.getSendEvents().size() < RETRY).forEach(c -> {
						CreditNoteManager manager = new CreditNoteManager(session);
						try {
							SendEventModel sendEvent = manager.sendToTrirdParty(organization, c);
							if (sendEvent.getResult()) {
								c.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
							}
						} catch (SendException e) {
							throw new JobException("error on execute job", e);
						}
					});
		}

		return readCount;
	}

	private long sendDebitNotes(OpenfactSession session, OrganizationModel organization) {
		long readCount = 0;

		ScrollModel<List<DebitNoteModel>> scroll = session.debitNotes().getDebitNotesScroll(organization, 100,
				RequiredAction.SEND_TO_TRIRD_PARTY.toString());
		Iterator<List<DebitNoteModel>> iterator = scroll.iterator();

		while (iterator.hasNext()) {
			List<DebitNoteModel> debitNotes = iterator.next();

			readCount += debitNotes.size();

			debitNotes.stream().filter(p -> p.getRequiredActions().contains(RequiredAction.SEND_TO_TRIRD_PARTY))
					.filter(p -> p.getSendEvents().size() < RETRY).forEach(c -> {
						DebitNoteManager manager = new DebitNoteManager(session);
						try {
							SendEventModel sendEvent = manager.sendToTrirdParty(organization, c);
							if (sendEvent.getResult()) {
								c.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
							}
						} catch (SendException e) {
							throw new JobException("error on execute job", e);
						}
					});
		}

		return readCount;
	}

	private long sendPerceptions(OpenfactSession session, OrganizationModel organization) {
		long readCount = 0;

		ScrollModel<List<PerceptionModel>> scroll = session.getProvider(PerceptionProvider.class)
				.getPerceptionsScroll(organization, 100, RequiredAction.SEND_TO_TRIRD_PARTY.toString());
		Iterator<List<PerceptionModel>> iterator = scroll.iterator();

		while (iterator.hasNext()) {
			List<PerceptionModel> perceptions = iterator.next();

			readCount += perceptions.size();

			perceptions.stream().filter(p -> p.getRequiredActions().contains(RequiredAction.SEND_TO_TRIRD_PARTY))
					.filter(p -> p.getSendEvents().size() < RETRY).forEach(c -> {
						PerceptionManager manager = new PerceptionManager(session);
						try {
							SendEventModel sendEvent = manager.sendToTrirdParty(organization, c);
							if (sendEvent.getResult()) {
								c.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
							}
						} catch (SendException e) {
							throw new JobException("error on execute job", e);
						}
					});
		}

		return readCount;
	}

	private long sendRetentions(OpenfactSession session, OrganizationModel organization) {
		long readCount = 0;

		ScrollModel<List<RetentionModel>> scroll = session.getProvider(RetentionProvider.class)
				.getRetentionsScroll(organization, 100, RequiredAction.SEND_TO_TRIRD_PARTY.toString());
		Iterator<List<RetentionModel>> iterator = scroll.iterator();

		while (iterator.hasNext()) {
			List<RetentionModel> retentions = iterator.next();

			readCount += retentions.size();

			retentions.stream().filter(p -> p.getRequiredActions().contains(RequiredAction.SEND_TO_TRIRD_PARTY))
					.filter(p -> p.getSendEvents().size() < RETRY).forEach(c -> {
						RetentionManager manager = new RetentionManager(session);
						try {
							SendEventModel sendEvent = manager.sendToTrirdParty(organization, c);
							if (sendEvent.getResult()) {
								c.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
							}
						} catch (SendException e) {
							throw new JobException("error on execute job", e);
						}
					});
		}

		return readCount;
	}

	private long sendSummary(OpenfactSession session, OrganizationModel organization) {
		long readCount = 0;

		ScrollModel<List<SummaryDocumentModel>> scroll = session.getProvider(SummaryDocumentProvider.class)
				.getSummaryDocumentsScroll(organization, 100, RequiredAction.SEND_TO_TRIRD_PARTY.toString());
		Iterator<List<SummaryDocumentModel>> iterator = scroll.iterator();

		while (iterator.hasNext()) {
			List<SummaryDocumentModel> summaryDocuments = iterator.next();

			readCount += summaryDocuments.size();

			summaryDocuments.stream().filter(p -> p.getRequiredActions().contains(RequiredAction.SEND_TO_TRIRD_PARTY))
					.filter(p -> p.getSendEvents().size() < RETRY).forEach(c -> {
						SummaryDocumentManager manager = new SummaryDocumentManager(session);
						try {
							SendEventModel sendEvent = manager.sendToTrirdParty(organization, c);
							if (sendEvent.getResult()) {
								c.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
							}
						} catch (SendException e) {
							throw new JobException("error on execute job", e);
						}
					});
		}

		return readCount;
	}

	private long sendVoided(OpenfactSession session, OrganizationModel organization) {
		long readCount = 0;

		ScrollModel<List<VoidedDocumentModel>> scroll = session.getProvider(VoidedDocumentProvider.class)
				.getVoidedDocumentsScroll(organization, 100, RequiredAction.SEND_TO_TRIRD_PARTY.toString());
		Iterator<List<VoidedDocumentModel>> iterator = scroll.iterator();

		while (iterator.hasNext()) {
			List<VoidedDocumentModel> debitNotes = iterator.next();

			readCount += debitNotes.size();

			debitNotes.stream().filter(p -> p.getRequiredActions().contains(RequiredAction.SEND_TO_TRIRD_PARTY))
					.filter(p -> p.getSendEvents().size() < RETRY).forEach(c -> {
						VoidedDocumentManager manager = new VoidedDocumentManager(session);
						try {
							SendEventModel sendEvent = manager.sendToTrirdParty(organization, c);
							if (sendEvent.getResult()) {
								c.removeRequiredAction(RequiredAction.SEND_TO_TRIRD_PARTY);
							}
						} catch (SendException e) {
							throw new JobException("error on execute job", e);
						}
					});
		}

		return readCount;
	}
}
