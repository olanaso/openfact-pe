package org.openfact.pe.models.jpa.ubl;

import org.jboss.logging.Logger;
import org.openfact.models.OpenfactSession;
import org.openfact.models.jpa.JpaModel;
import org.openfact.pe.models.VoidedDocumentLineModel;
import org.openfact.pe.models.jpa.entities.VoidedDocumentsLineEntity;

import javax.persistence.EntityManager;

/**
 * Created by lxpary on 04/01/17.
 */
public class VoidedDocumentLineAdapter implements VoidedDocumentLineModel, JpaModel<VoidedDocumentsLineEntity> {
    protected static final Logger logger = Logger.getLogger(VoidedDocumentLineAdapter.class);


    protected VoidedDocumentsLineEntity voidedDocumentsLine;
    protected EntityManager em;
    protected OpenfactSession session;

    public VoidedDocumentLineAdapter(OpenfactSession session, EntityManager em,
                                     VoidedDocumentsLineEntity voidedDocumentsLine) {
        this.session = session;
        this.em = em;
        this.voidedDocumentsLine = voidedDocumentsLine;
    }

    @Override
    public VoidedDocumentsLineEntity getEntity() {
        return voidedDocumentsLine;
    }

    @Override
    public String getId() {
        return voidedDocumentsLine.getId();
    }

    @Override
    public String getLineId() {
        return voidedDocumentsLine.getLineId();
    }

    @Override
    public void setLineId(String value) {
        voidedDocumentsLine.setLineId(value);
    }

    @Override
    public String getDocumentTypeCode() {
        return voidedDocumentsLine.getDocumentTypeCode();
    }

    @Override
    public void setDocumentTypeCode(String value) {
        voidedDocumentsLine.setDocumentTypeCode(value);
    }

    @Override
    public String getDocumentSerialId() {
        return voidedDocumentsLine.getDocumentSerialID();
    }

    @Override
    public void setDocumentSerialId(String value) {
        voidedDocumentsLine.setDocumentSerialID(value);
    }

    @Override
    public String getDocumentNumberId() {
        return voidedDocumentsLine.getDocumentNumberID();
    }

    @Override
    public void setDocumentNumberId(String value) {
        voidedDocumentsLine.setDocumentNumberID(value);
    }

    @Override
    public String getVoidReasonDescription() {
        return voidedDocumentsLine.getVoidReasonDescription();
    }

    @Override
    public void setVoidReasonDescription(String value) {
        voidedDocumentsLine.setVoidReasonDescription(value);
    }
}
