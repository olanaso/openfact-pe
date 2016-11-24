package org.openfact.models.jpa.pe.ubl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.jpa.AbstractHibernateStorage;
import org.openfact.models.jpa.entities.ubl.CreditNoteSendEventEntity;
import org.openfact.models.jpa.entities.ubl.DebitNoteSendEventEntity;
import org.openfact.models.jpa.entities.ubl.InvoiceSendEventEntity;
import org.openfact.models.jpa.pe.entities.PerceptionSendEventEntity;
import org.openfact.models.jpa.pe.entities.RetentionSendEventEntity;
import org.openfact.models.jpa.pe.entities.SummaryDocumentsSendEventEntity;
import org.openfact.models.jpa.pe.entities.VoidedDocumentsSendEventEntity;
import org.openfact.models.jpa.ubl.CreditNoteAdapter;
import org.openfact.models.jpa.ubl.CreditNoteSendEventAdapter;
import org.openfact.models.jpa.ubl.DebitNoteAdapter;
import org.openfact.models.jpa.ubl.DebitNoteSendEventAdapter;
import org.openfact.models.jpa.ubl.InvoiceAdapter;
import org.openfact.models.jpa.ubl.InvoiceSendEventAdapter;
import org.openfact.models.ubl.CreditNoteModel;
import org.openfact.models.ubl.DebitNoteModel;
import org.openfact.models.ubl.InvoiceModel;
import org.openfact.models.ubl.SendEventModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.provider.UblSunatSendEventProvider;
import org.openfact.ubl.UblSenderException;

public class JpaSunatSendEventProvider extends AbstractHibernateStorage implements UblSunatSendEventProvider {
	protected static final Logger logger = Logger.getLogger(JpaSunatSendEventProvider.class);

	private final OpenfactSession session;
	protected EntityManager em;

	public JpaSunatSendEventProvider(OpenfactSession session, EntityManager em) {
		this.session = session;
		this.em = em;
	}

	@Override
	public void close() {
	}

	@Override
	public SendEventModel addInvoiceSendEvent(OrganizationModel organization, InvoiceModel invoice, byte[] xmlSubmitted,
			byte[] response, boolean isAccepted) throws UblSenderException {
		InvoiceSendEventEntity sendEventEntity = new InvoiceSendEventEntity();
		sendEventEntity.setInvoices(Arrays.asList(InvoiceAdapter.toEntity(invoice, em)));
		sendEventEntity.setXmlDoument(xmlSubmitted);
		if (response != null) {
			sendEventEntity.setDocumentResponse(response);
		}
		sendEventEntity.setID(invoice.getID());
		sendEventEntity.setAccepted(isAccepted);
		sendEventEntity.setCreatedTimestamp(LocalDateTime.now());
		em.persist(sendEventEntity);
		em.flush();
		final SendEventModel adapter = new InvoiceSendEventAdapter(session, organization, em, sendEventEntity);
		return adapter;
	}

	@Override
	public SendEventModel addCreditNoteSendEvent(OrganizationModel organization, CreditNoteModel creditNote,
			byte[] xmlSubmitted, byte[] response, boolean isAccepted) throws UblSenderException {
		CreditNoteSendEventEntity sendEventEntity = new CreditNoteSendEventEntity();
		sendEventEntity.setCreditNotes(Arrays.asList(CreditNoteAdapter.toEntity(creditNote, em)));
		sendEventEntity.setXmlDoument(xmlSubmitted);
		if (response != null) {
			sendEventEntity.setDocumentResponse(response);
		}
		sendEventEntity.setID(creditNote.getID());
		sendEventEntity.setAccepted(isAccepted);
		sendEventEntity.setCreatedTimestamp(LocalDateTime.now());
		em.persist(sendEventEntity);
		em.flush();
		final SendEventModel adapter = new CreditNoteSendEventAdapter(session, organization, em, sendEventEntity);
		return adapter;
	}

	@Override
	public SendEventModel addDebitNoteSendEvent(OrganizationModel organization, DebitNoteModel debitNote,
			byte[] xmlSubmitted, byte[] response, boolean isAccepted) throws UblSenderException {
		DebitNoteSendEventEntity sendEventEntity = new DebitNoteSendEventEntity();
		sendEventEntity.setDebitNotes(Arrays.asList(DebitNoteAdapter.toEntity(debitNote, em)));
		sendEventEntity.setXmlDoument(xmlSubmitted);
		if (response != null) {
			sendEventEntity.setDocumentResponse(response);
		}
		sendEventEntity.setID(debitNote.getID());
		sendEventEntity.setAccepted(isAccepted);
		sendEventEntity.setCreatedTimestamp(LocalDateTime.now());
		em.persist(sendEventEntity);
		em.flush();
		final SendEventModel adapter = new DebitNoteSendEventAdapter(session, organization, em, sendEventEntity);
		return adapter;
	}

	@Override
	public List<SendEventModel> getInvoiceSendEvents(OrganizationModel organization, InvoiceModel invoice)
			throws UblSenderException {
		String queryName = "getAllSendEventsByInvoice";
		TypedQuery<InvoiceSendEventEntity> query = em.createNamedQuery(queryName, InvoiceSendEventEntity.class);
		query.setParameter("invoiceId", invoice.getId());

		List<InvoiceSendEventEntity> results = query.getResultList();
		List<SendEventModel> sendEvents = results.stream()
				.map(f -> new InvoiceSendEventAdapter(session, organization, em, f)).collect(Collectors.toList());
		return sendEvents;
	}

	@Override
	public List<SendEventModel> getCreditNoteSendEvents(OrganizationModel organization, CreditNoteModel creditNote)
			throws UblSenderException {
		String queryName = "getAllSendEventsByCreditNote";
		TypedQuery<CreditNoteSendEventEntity> query = em.createNamedQuery(queryName, CreditNoteSendEventEntity.class);
		query.setParameter("creditNoteId", creditNote.getId());

		List<CreditNoteSendEventEntity> results = query.getResultList();
		List<SendEventModel> sendEvents = results.stream()
				.map(f -> new CreditNoteSendEventAdapter(session, organization, em, f)).collect(Collectors.toList());
		return sendEvents;
	}

	@Override
	public List<SendEventModel> getDebitNoteSendEvents(OrganizationModel organization, DebitNoteModel debitNote)
			throws UblSenderException {
		String queryName = "getAllSendEventsByDebitNote";
		TypedQuery<DebitNoteSendEventEntity> query = em.createNamedQuery(queryName, DebitNoteSendEventEntity.class);
		query.setParameter("debitNoteId", debitNote.getId());

		List<DebitNoteSendEventEntity> results = query.getResultList();
		List<SendEventModel> sendEvents = results.stream()
				.map(f -> new DebitNoteSendEventAdapter(session, organization, em, f)).collect(Collectors.toList());
		return sendEvents;
	}

	@Override
	public SendEventModel addPerceptionSendEvent(OrganizationModel organization, PerceptionModel perception,
			byte[] xmlSubmitted, byte[] response, boolean isAccepted) throws UblSenderException {
		PerceptionSendEventEntity sendEventEntity = new PerceptionSendEventEntity();
		sendEventEntity.setPerceptions(Arrays.asList(PerceptionAdapter.toEntity(perception, em)));
		sendEventEntity.setXmlDoument(xmlSubmitted);
		if (response != null) {
			sendEventEntity.setDocumentResponse(response);
		}
		sendEventEntity.setID(perception.getDocumentId());
		sendEventEntity.setAccepted(isAccepted);
		sendEventEntity.setCreatedTimestamp(LocalDateTime.now());
		em.persist(sendEventEntity);
		em.flush();
		final SendEventModel adapter = new PerceptionSendEventAdapter(session, organization, em, sendEventEntity);
		return adapter;
	}

	@Override
	public SendEventModel addRetentionSendEvent(OrganizationModel organization, RetentionModel retention,
			byte[] xmlSubmitted, byte[] response, boolean isAccepted) throws UblSenderException {
		RetentionSendEventEntity sendEventEntity = new RetentionSendEventEntity();
		sendEventEntity.setRetentions(Arrays.asList(RetentionAdapter.toEntity(retention, em)));
		sendEventEntity.setXmlDoument(xmlSubmitted);
		if (response != null) {
			sendEventEntity.setDocumentResponse(response);
		}
		sendEventEntity.setID(retention.getDocumentId());
		sendEventEntity.setAccepted(isAccepted);
		sendEventEntity.setCreatedTimestamp(LocalDateTime.now());
		em.persist(sendEventEntity);
		em.flush();
		final SendEventModel adapter = new RetentionSendEventAdapter(session, organization, em, sendEventEntity);
		return adapter;
	}

	@Override
	public SendEventModel addSummaryDocumentSendEvent(OrganizationModel organization,
			SummaryDocumentModel summaryDocument, byte[] xmlSubmitted, byte[] response, boolean isAccepted)
			throws UblSenderException {
		SummaryDocumentsSendEventEntity sendEventEntity = new SummaryDocumentsSendEventEntity();
		sendEventEntity.setSummaryDocuments(Arrays.asList(SummaryDocumentAdapter.toEntity(summaryDocument, em)));
		sendEventEntity.setXmlDoument(xmlSubmitted);
		if (response != null) {
			sendEventEntity.setDocumentResponse(response);
		}
		sendEventEntity.setID(summaryDocument.getDocumentId());
		sendEventEntity.setAccepted(isAccepted);
		sendEventEntity.setCreatedTimestamp(LocalDateTime.now());
		em.persist(sendEventEntity);
		em.flush();
		final SendEventModel adapter = new SummaryDocumentSendEventAdapter(session, organization, em, sendEventEntity);
		return adapter;
	}

	@Override
	public SendEventModel addVoidedDocumentSendEvent(OrganizationModel organization, VoidedDocumentModel voidedDocument,
			byte[] xmlSubmitted, byte[] response, boolean isAccepted) throws UblSenderException {
		VoidedDocumentsSendEventEntity sendEventEntity = new VoidedDocumentsSendEventEntity();
		sendEventEntity.setVoidedDocuments(Arrays.asList(VoidedDocumentAdapter.toEntity(voidedDocument, em)));
		sendEventEntity.setXmlDoument(xmlSubmitted);
		if (response != null) {
			sendEventEntity.setDocumentResponse(response);
		}
		sendEventEntity.setID(voidedDocument.getDocumentId());
		sendEventEntity.setAccepted(isAccepted);
		sendEventEntity.setCreatedTimestamp(LocalDateTime.now());
		em.persist(sendEventEntity);
		em.flush();
		final SendEventModel adapter = new VoidedDocumentSendEventAdapter(session, organization, em, sendEventEntity);
		return adapter;
	}

	@Override
	public List<SendEventModel> getPerceptionSendEvents(OrganizationModel organization, PerceptionModel perception)
			throws UblSenderException {
		String queryName = "getAllSendEventsByPerception";
		TypedQuery<PerceptionSendEventEntity> query = em.createNamedQuery(queryName, PerceptionSendEventEntity.class);
		query.setParameter("perceptionId", perception.getId());

		List<PerceptionSendEventEntity> results = query.getResultList();
		List<SendEventModel> sendEvents = results.stream()
				.map(f -> new PerceptionSendEventAdapter(session, organization, em, f)).collect(Collectors.toList());
		return sendEvents;
	}

	@Override
	public List<SendEventModel> getRetentionSendEvents(OrganizationModel organization, RetentionModel retention)
			throws UblSenderException {
		String queryName = "getAllSendEventsByRetention";
		TypedQuery<RetentionSendEventEntity> query = em.createNamedQuery(queryName, RetentionSendEventEntity.class);
		query.setParameter("retentionId", retention.getId());

		List<RetentionSendEventEntity> results = query.getResultList();
		List<SendEventModel> sendEvents = results.stream()
				.map(f -> new RetentionSendEventAdapter(session, organization, em, f)).collect(Collectors.toList());
		return sendEvents;
	}

	@Override
	public List<SendEventModel> getSummaryDocumentSendEvents(OrganizationModel organization,
			SummaryDocumentModel summaryDocument) throws UblSenderException {
		String queryName = "getAllSendEventsBySummaryDocuments";
		TypedQuery<SummaryDocumentsSendEventEntity> query = em.createNamedQuery(queryName,
				SummaryDocumentsSendEventEntity.class);
		query.setParameter("summaryDocumentsId", summaryDocument.getId());

		List<SummaryDocumentsSendEventEntity> results = query.getResultList();
		List<SendEventModel> sendEvents = results.stream()
				.map(f -> new SummaryDocumentSendEventAdapter(session, organization, em, f))
				.collect(Collectors.toList());
		return sendEvents;
	}

	@Override
	public List<SendEventModel> getVoidedDocumentSendEvents(OrganizationModel organization,
			VoidedDocumentModel voidedDocument) throws UblSenderException {
		String queryName = "getAllSendEventsByVoidedDocuments";
		TypedQuery<VoidedDocumentsSendEventEntity> query = em.createNamedQuery(queryName,
				VoidedDocumentsSendEventEntity.class);
		query.setParameter("voidedDocumentsId", voidedDocument.getId());

		List<VoidedDocumentsSendEventEntity> results = query.getResultList();
		List<SendEventModel> sendEvents = results.stream()
				.map(f -> new VoidedDocumentSendEventAdapter(session, organization, em, f))
				.collect(Collectors.toList());
		return sendEvents;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
