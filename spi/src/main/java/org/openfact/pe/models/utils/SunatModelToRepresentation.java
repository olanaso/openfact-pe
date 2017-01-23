package org.openfact.pe.models.utils;

import org.openfact.common.converts.DateUtils;
import org.openfact.pe.models.*;
import org.openfact.pe.representations.idm.*;

import java.util.HashSet;

public class SunatModelToRepresentation {

    public static DocumentoSunatRepresentation toRepresentation(PerceptionModel model) {
        DocumentoSunatRepresentation rep = new DocumentoSunatRepresentation();
        if (model.getRequiredActions() != null) {
            rep.setRequiredActions(new HashSet<>());
            rep.getRequiredActions().addAll(model.getRequiredActions());
        }
        if (model.getEntityDocumentType() != null) {
            rep.setEntidadTipoDeDocumento(model.getEntityDocumentType());
        }
        if (model.getEntityDocumentNuber() != null) {
            rep.setEntidadNumeroDeDocumento(model.getEntityDocumentNuber());
        }
        if (model.getEntityName() != null) {
            rep.setEntidadDenominacion(model.getEntityName());
        }
        if (model.getEntityAddress() != null) {
            rep.setEntidadDireccion(model.getEntityAddress());
        }
        if (model.getEntityEmail() != null) {
            rep.setEntidadEmail(model.getEntityEmail());
        }
        if (model.getDocumentId() != null) {
            String[] splits = model.getDocumentId().split("-");
            rep.setSerieDocumento(splits[0]);
            rep.setNumeroDocumento(splits[1]);
        }
        if (model.getDocumentCurrencyCode() != null) {
            rep.setMonedaDocumento(model.getDocumentCurrencyCode());
        }
        if (model.getSunatPerceptionPercent() != null) {
            rep.setTasaDocumento(model.getSunatPerceptionPercent());
        }
        if (model.getNote() != null) {
            rep.setObservaciones(model.getNote());
        }
        if (model.getTotalPerceptionAmount() != null) {
            rep.setTotalDocumentoSunat(model.getTotalPerceptionAmount());
        }
        if (model.getTotalCashed() != null) {
            rep.setTotalPago(model.getTotalCashed());
        }
        if (model.getIssueDateTime() != null) {
            rep.setFechaDeEmision(DateUtils.asDate(model.getIssueDateTime()));
        }
        if (model.getId() != null) {
            rep.setCodigoUnico(model.getId());
        }

        return rep;
    }

    public static DocumentoSunatRepresentation toRepresentation(RetentionModel model) {
        DocumentoSunatRepresentation rep = new DocumentoSunatRepresentation();
        if (model.getRequiredActions() != null) {
            rep.setRequiredActions(new HashSet<>());
            rep.getRequiredActions().addAll(model.getRequiredActions());
        }
        if (model.getEntityDocumentType() != null) {
            rep.setEntidadTipoDeDocumento(model.getEntityDocumentType());
        }
        if (model.getEntityDocumentNuber() != null) {
            rep.setEntidadNumeroDeDocumento(model.getEntityDocumentNuber());
        }
        if (model.getEntityName() != null) {
            rep.setEntidadDenominacion(model.getEntityName());
        }
        if (model.getEntityAddress() != null) {
            rep.setEntidadDireccion(model.getEntityAddress());
        }
        if (model.getEntityEmail() != null) {
            rep.setEntidadEmail(model.getEntityEmail());
        }
        if (model.getDocumentId() != null) {
            String[] splits = model.getDocumentId().split("-");
            rep.setSerieDocumento(splits[0]);
            rep.setNumeroDocumento(splits[1]);
        }
        if (model.getDocumentCurrencyCode() != null) {
            rep.setMonedaDocumento(model.getDocumentCurrencyCode());
        }
        if (model.getSunatRetentionPercent() != null) {
            rep.setTasaDocumento(model.getSunatRetentionPercent());
        }
        if (model.getNote() != null) {
            rep.setObservaciones(model.getNote());
        }
        if (model.getTotalRetentionAmount() != null) {
            rep.setTotalDocumentoSunat(model.getTotalRetentionAmount());
        }
        if (model.getTotalCashed() != null) {
            rep.setTotalPago(model.getTotalCashed());
        }
        if (model.getIssueDateTime() != null) {
            rep.setFechaDeEmision(DateUtils.asDate(model.getIssueDateTime()));
        }
        if (model.getId() != null) {
            rep.setCodigoUnico(model.getId());
        }
        return rep;
    }

    public static SummaryRepresentation toRepresentation(SummaryDocumentModel model) {
        SummaryRepresentation rep = new SummaryRepresentation();
        rep.setCodigoUnico(model.getId());
        if (model.getDocumentId() != null) {
            String[] splits = model.getDocumentId().split("-");
            rep.setSerie(splits[1]);
            rep.setNumero(splits[2]);
        }
        if (model.getIssueDate() != null) {
            rep.setFechaDeEmision(model.getIssueDate().atStartOfDay());
        }
        if (model.getReferenceDate() != null) {
            rep.setFechaDeReferencia(model.getReferenceDate().atStartOfDay());
        }
        return rep;
    }

    public static VoidedRepresentation toRepresentation(VoidedDocumentModel model) {
        VoidedRepresentation rep = new VoidedRepresentation();
        rep.setCodigoUnico(model.getId());
        if (model.getRequiredActions() != null) {
            rep.setRequiredActions(new HashSet<>());
            rep.getRequiredActions().addAll(model.getRequiredActions());
        }
        if (model.getDocumentId() != null) {
            String[] splits = model.getDocumentId().split("-");
            rep.setSerieDocumento(splits[0] + "-" + splits[1]);
            rep.setNumeroDocumento(splits[2]);
        }
        if (model.getIssueDateTime() != null) {
            rep.setFechaDeEmision(DateUtils.asDate(model.getIssueDateTime()));
        }
        if (model.getVoidedDocumentLines() != null) {
            for (VoidedDocumentLineModel line : model.getVoidedDocumentLines()) {
                rep.addDetalle(toRepresentation(line));
            }
        }
        return rep;
    }

    private static VoidedLineRepresentation toRepresentation(VoidedDocumentLineModel model) {
        VoidedLineRepresentation rep = new VoidedLineRepresentation();
        if (model.getLineId() != null) {
            rep.setId(model.getLineId());
        }
        if (model.getDocumentNumberId() != null && model.getDocumentSerialId() != null) {
            rep.setNumeroDocumentoRelacionado(model.getDocumentSerialId() + "-" + model.getDocumentNumberId());
        }
        if (model.getDocumentTypeCode() != null) {
            rep.setTipoDocumentoRelacionado(model.getDocumentTypeCode());
        }
        if (model.getVoidReasonDescription() != null) {
            rep.setDescripcionDocumentoRelacionado(model.getVoidReasonDescription());
        }
        return rep;
    }

}
