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
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.PerceptionSendEventModel;
import org.openfact.pe.models.jpa.entities.PerceptionSendEventEntity;

public class PerceptionSendEventAdapter implements PerceptionSendEventModel, JpaModel<PerceptionSendEventEntity> {
	protected static final Logger logger = Logger.getLogger(PerceptionSendEventAdapter.class);

	protected OrganizationModel organization;
	protected PerceptionSendEventEntity sendEvent;
	protected EntityManager em;
	protected OpenfactSession session;

	public PerceptionSendEventAdapter(OpenfactSession session, OrganizationModel organization, EntityManager em,
			PerceptionSendEventEntity sendEvent) {
		this.organization = organization;
		this.session = session;
		this.em = em;
		this.sendEvent = sendEvent;
	}

	@Override
	public PerceptionModel getPerception() {
		return null;
	}

	@Override
	public void setPerception(PerceptionModel perception) {

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
	public PerceptionSendEventEntity getEntity() {
		return null;
	}

//	@Override
//	public PerceptionSendEventEntity getEntity() {
//		return sendEvent;
//	}
//
//	@Override
//	public String getId() {
//		return sendEvent.getId();
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
//	public String getDescription() {
//		return sendEvent.getDescription();
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
//	public PerceptionModel getPerception() {
//		if (sendEvent.getPerception() == null) {
//			return null;
//		}
//		return new PerceptionAdapter(session,organization, em, sendEvent.getPerception());
//	}
//
//	@Override
//	public void setPerception(PerceptionModel perception) {
//		sendEvent.setPerception(PerceptionAdapter.toEntity(perception, em));
//	}
//
//	public static PerceptionSendEventEntity toEntity(PerceptionSendEventModel model, EntityManager em) {
//		if (model instanceof PerceptionSendEventAdapter) {
//			return ((PerceptionSendEventAdapter) model).getEntity();
//		}
//		return em.getReference(PerceptionSendEventEntity.class, model.getId());
//	}
//
//	@Override
//	public String getType() {
//		return sendEvent.getType();
//	}
//
//	@Override
//	public void setType(String type) {
//		sendEvent.setType(type);
//	}
//
//	@Override
//	public Map<String, String> getDestity() {
//		return sendEvent.getDestiny();
//	}
//
//	@Override
//	public void setDestiny(Map<String, String> destiny) {
//		sendEvent.setDestiny(destiny);
//	}
//
//	@Override
//	public LocalDateTime getCreatedTimestamp() {
//		return sendEvent.getCreatedTimestamp();
//	}
//
//

}
