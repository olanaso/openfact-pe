/*******************************************************************************
 * Copyright 2016 Sistcoop, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openfact.pe.models.jpa.ubl;

import org.jboss.logging.Logger;
import org.openfact.file.FileModel;
import org.openfact.file.FileProvider;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.SendEventModel;
import org.openfact.models.enums.DestinyType;
import org.openfact.models.enums.SendResultType;
import org.openfact.models.jpa.JpaModel;
import org.openfact.models.jpa.SendEventAdapter;
import org.openfact.models.jpa.entities.SendEventEntity;
import org.openfact.pe.models.jpa.entities.SunatSendEventEntity;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SunatSendEventAdapter implements SendEventModel, JpaModel<SunatSendEventEntity> {

    protected static final Logger logger = Logger.getLogger(SendEventAdapter.class);
    protected OrganizationModel organization;
    protected SunatSendEventEntity sendEvent;
    protected EntityManager em;
    protected OpenfactSession session;

    public SunatSendEventAdapter(OpenfactSession session, EntityManager em, OrganizationModel organization, SunatSendEventEntity sendEvent) {
        this.session = session;
        this.em = em;
        this.organization = organization;
        this.sendEvent = sendEvent;
    }

    public static SendEventEntity toEntity(SendEventModel model, EntityManager em) {
        if (model instanceof SendEventAdapter) {
            return ((SendEventAdapter) model).getEntity();
        }
        return em.getReference(SendEventEntity.class, model.getId());
    }

    @Override
    public SunatSendEventEntity getEntity() {
        return sendEvent;
    }

    @Override
    public String getId() {
        return sendEvent.getId();
    }

    @Override
    public SendResultType getResult() {
        return sendEvent.getResult();
    }

    @Override
    public void setResult(SendResultType result) {
        sendEvent.setResult(result);
    }

    @Override
    public String getDescription() {
        return sendEvent.getDescription();
    }

    @Override
    public void setDescription(String description) {
        sendEvent.setDescription(description);
    }

    @Override
    public List<FileModel> getFileAttatchments() {
        FileProvider fileProvider = session.getProvider(FileProvider.class);
        List<FileModel> files = new ArrayList<>();
        for (String fileId : sendEvent.getFileAttatchmentIds()) {
            files.add(fileProvider.getFileById(organization, fileId));
        }
        return files;
    }

    @Override
    public void attachFile(FileModel file) {
        sendEvent.getFileAttatchmentIds().add(file.getId());
    }

    @Override
    public void unattachFile(FileModel file) {
        sendEvent.getFileAttatchmentIds().remove(file.getId());
    }

    @Override
    public String getType() {
        return sendEvent.getType();
    }

    @Override
    public void setType(String type) {
        sendEvent.setType(type);
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
        return Collections.emptyList();
    }

    @Override
    public Map<String, List<String>> getDestinyAttributes() {
        return Collections.emptyMap();
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
        return Collections.emptyList();
    }

    @Override
    public Map<String, List<String>> getResponseAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public LocalDateTime getCreatedTimestamp() {
        return sendEvent.getCreatedTimestamp();
    }

    @Override
    public DestinyType getDestityType() {
        return sendEvent.getDestinyType();
    }

    @Override
    public List<FileModel> getResponseFileAttatchments() {
        FileProvider fileProvider = session.getProvider(FileProvider.class);
        List<FileModel> files = new ArrayList<>();
        for (String fileId : sendEvent.getFileResponseAttatchmentIds()) {
            files.add(fileProvider.getFileById(organization, fileId));
        }
        return files;
    }

    @Override
    public void attachResponseFile(FileModel file) {
        sendEvent.getFileResponseAttatchmentIds().add(file.getId());
    }

    @Override
    public void unattachResponseFile(FileModel file) {
        sendEvent.getFileResponseAttatchmentIds().remove(file.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SendEventModel)) return false;

        SendEventModel that = (SendEventModel) o;
        return that.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }


}
