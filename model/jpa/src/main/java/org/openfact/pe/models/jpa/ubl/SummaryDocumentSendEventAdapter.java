package org.openfact.pe.models.jpa.ubl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.openfact.file.FileModel;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.OrganizationAdapter;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.SummaryDocumentsSendEventModel;
import org.openfact.pe.models.jpa.entities.SummaryDocumentsSendEventEntity;

public class SummaryDocumentSendEventAdapter implements SummaryDocumentsSendEventModel, JpaModel<SummaryDocumentsSendEventEntity> {
	protected static final Logger logger = Logger.getLogger(SummaryDocumentSendEventAdapter.class);

	protected OrganizationModel organization;
	protected SummaryDocumentsSendEventEntity sendEvent;
	protected EntityManager em;
	protected OpenfactSession session;

	public SummaryDocumentSendEventAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			SummaryDocumentsSendEventEntity sendEvent) {
		this.organization = organization;
		this.session = session;
		this.em = em;
		this.sendEvent = sendEvent;
	}

	@Override
	public SummaryDocumentModel getSummaryDocument() {
		return null;
	}

	@Override
	public void setSummaryDocument(SummaryDocumentModel summaryDocument) {

	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public LocalDateTime getCreatedTimestamp() {
		return null;
	}

	@Override
	public DestinyType getDestityType() {
		return null;
	}

	@Override
	public SendResultType getResult() {
		return null;
	}

	@Override
	public void setResult(SendResultType result) {

	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void setDescription(String description) {

	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public void setType(String type) {

	}

	@Override
	public void setSingleDestinyAttribute(String name, String value) {

	}

	@Override
	public void setDestinyAttribute(String name, List<String> values) {

	}

	@Override
	public void removeDestinyAttribute(String name) {

	}

	@Override
	public String getFirstDestinyAttribute(String name) {
		return null;
	}

	@Override
	public List<String> getDestinyAttribute(String name) {
		return null;
	}

	@Override
	public Map<String, List<String>> getDestinyAttributes() {
		return null;
	}

	@Override
	public void setSingleResponseAttribute(String name, String value) {

	}

	@Override
	public void setResponseAttribute(String name, List<String> values) {

	}

	@Override
	public void removeResponseAttribute(String name) {

	}

	@Override
	public String getFirstResponseAttribute(String name) {
		return null;
	}

	@Override
	public List<String> getResponseAttribute(String name) {
		return null;
	}

	@Override
	public Map<String, List<String>> getResponseAttributes() {
		return null;
	}

	@Override
	public List<FileModel> getFileAttatchments() {
		return null;
	}

	@Override
	public void attachFile(FileModel file) {

	}

	@Override
	public void unattachFile(FileModel file) {

	}

	@Override
	public List<FileModel> getResponseFileAttatchments() {
		return null;
	}

	@Override
	public void attachResponseFile(FileModel file) {

	}

	@Override
	public void unattachResponseFile(FileModel file) {

	}

	@Override
	public SummaryDocumentsSendEventEntity getEntity() {
		return null;
	}

//	@Override
//	public String getId() {
//		return sendEvent.getId();
//	}
//
//	@Override
//	public String getDescription() {
//		return sendEvent.getDescription();
//	}
//
//	@Override
//	public SummaryDocumentsSendEventEntity getEntity() {
//		return sendEvent;
//	}
//
//	@Override
//	public boolean getResult() {
//		return sendEvent.isResult();
//	}
//
//	@Override
//	public void setResult(boolean result) {
//		sendEvent.setResult(result);
//	}
//
//	@Override
//	public void setDescription(String description) {
//		sendEvent.setDescription(description);
//	}
//
//	@Override
//	public OrganizationModel getOrganization() {
//		return organization;
//	}
//
//	@Override
//	public SummaryDocumentModel getSummaryDocument() {
//		if (sendEvent.getSummaryDocuments() == null) {
//			return null;
//		}
//		return new SummaryDocumentAdapter(session, organization, em, sendEvent.getSummaryDocuments());
//	}
//
//	@Override
//	public void setSummaryDocument(SummaryDocumentModel summaryDocument) {
//		sendEvent.setSummaryDocuments(SummaryDocumentAdapter.toEntity(summaryDocument, em));
//
//	}
//
//	public static SummaryDocumentsSendEventEntity toEntity(SummaryDocumentsSendEventModel model, EntityManager em) {
//		if (model instanceof SummaryDocumentSendEventAdapter) {
//			return ((SummaryDocumentSendEventAdapter) model).getEntity();
//		}
//		return em.getReference(SummaryDocumentsSendEventEntity.class, model.getId());
//	}
//
//
//	@Override
//	public String getType() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void setType(String type) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public Map<String, String> getDestity() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void setDestiny(Map<String, String> destiny) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public LocalDateTime getCreatedTimestamp() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public StorageFileModel addFileAttatchments(FileModel file) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<StorageFileModel> getFileAttatchments() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<StorageFileModel> getFileResponseAttatchments() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public StorageFileModel addFileResponseAttatchments(FileModel file) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Map<String, String> getResponse() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void setResponse(Map<String, String> response) {
//		// TODO Auto-generated method stub
//
//	}
}
