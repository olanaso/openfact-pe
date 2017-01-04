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

        if (model.getPerceptionLines() != null) {
            for (PerceptionLineModel item : model.getPerceptionLines()) {
                rep.addDetalle(toRepresentation(item));
            }
        }
        return rep;
    }

    private static DocumentoSunatLineRepresentation toRepresentation(PerceptionLineModel model) {
        DocumentoSunatLineRepresentation rep = new DocumentoSunatLineRepresentation();
        if (model.getRelatedDocumentType() != null) {
            rep.setTipoDocumentoRelacionado(model.getRelatedDocumentType());
        }
        if (model.getRelatedDocumentNumber() != null) {
            rep.setNumeroDocumentoRelacionado(model.getRelatedDocumentNumber());
        }
        if (model.getRelatedIssueDateTime() != null) {
            rep.setFechaDocumentoRelacionado(DateUtils.asDate(model.getRelatedIssueDateTime()));
        }
        if (model.getRelatedDocumentCurrency() != null) {
            rep.setMonedaDocumentoRelacionado(model.getRelatedDocumentCurrency());
        }
        if (model.getTotalDocumentRelated() != null) {
            rep.setTotalDocumentoRelacionado(model.getTotalDocumentRelated());
        }
        if (model.getTypeChange() != null) {
            rep.setTipoCambio(model.getTypeChange());
        }
        if (model.getChangeIssueDateTime() != null) {
            rep.setFechaCambio(DateUtils.asDate(model.getChangeIssueDateTime()));
        }
        if (model.getTotalPerceptionPayment() != null) {
            rep.setPagoDocumentoSunat(model.getTotalPerceptionPayment());
        }
        if (model.getPerceptionPaymentNumber() != null) {
            rep.setNumeroPago(model.getPerceptionPaymentNumber());
        }
        if (model.getPerceptionIssueDateTime() != null) {
            rep.setFechaDocumentoSunat(DateUtils.asDate(model.getPerceptionIssueDateTime()));
        }
        if (model.getSunatNetPerceptionAmount() != null) {
            rep.setImporteDocumentoSunat(model.getSunatNetPerceptionAmount());
        }
        if (model.getSunatNetCashed() != null) {
            rep.setImportePago(model.getSunatNetCashed());
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
        if (model.getRetentionLines() != null) {
            for (RetentionLineModel item : model.getRetentionLines()) {
                rep.addDetalle(toRepresentation(item));
            }
        }
        return rep;
    }

    private static DocumentoSunatLineRepresentation toRepresentation(RetentionLineModel model) {
        DocumentoSunatLineRepresentation rep = new DocumentoSunatLineRepresentation();
        if (model.getRelatedDocumentType() != null) {
            rep.setTipoDocumentoRelacionado(model.getRelatedDocumentType());
        }
        if (model.getRelatedDocumentNumber() != null) {
            rep.setNumeroDocumentoRelacionado(model.getRelatedDocumentNumber());
        }
        if (model.getRelatedIssueDateTime() != null) {
            rep.setFechaDocumentoRelacionado(DateUtils.asDate(model.getRelatedIssueDateTime()));
        }
        if (model.getRelatedDocumentCurrency() != null) {
            rep.setMonedaDocumentoRelacionado(model.getRelatedDocumentCurrency());
        }
        if (model.getTotalDocumentRelated() != null) {
            rep.setTotalDocumentoRelacionado(model.getTotalDocumentRelated());
        }
        if (model.getTypeChange() != null) {
            rep.setTipoCambio(model.getTypeChange());
        }
        if (model.getChangeIssueDateTime() != null) {
            rep.setFechaCambio(DateUtils.asDate(model.getChangeIssueDateTime()));
        }
        if (model.getTotalRetentionPayment() != null) {
            rep.setPagoDocumentoSunat(model.getTotalRetentionPayment());
        }
        if (model.getRetentionPaymentNumber() != null) {
            rep.setNumeroPago(model.getRetentionPaymentNumber());
        }
        if (model.getRetentionIssueDateTime() != null) {
            rep.setFechaDocumentoSunat(DateUtils.asDate(model.getRetentionIssueDateTime()));
        }
        if (model.getSunatNetRetentionAmount() != null) {
            rep.setImporteDocumentoSunat(model.getSunatNetRetentionAmount());
        }
        if (model.getSunatNetCashed() != null) {
            rep.setImportePago(model.getSunatNetCashed());
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
            rep.setSerieDocumento(splits[0]+"-"+splits[1]);
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
