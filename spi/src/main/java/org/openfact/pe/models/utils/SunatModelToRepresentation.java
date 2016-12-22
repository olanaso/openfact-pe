package org.openfact.pe.models.utils;

import org.openfact.common.converts.DateUtils;
import org.openfact.models.ContactModel;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyModel;
import org.openfact.pe.models.PerceptionLineModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionLineModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.representations.idm.DocumentoSunatLineRepresentation;
import org.openfact.pe.representations.idm.DocumentoSunatRepresentation;
import org.openfact.pe.representations.idm.SummaryRepresentation;
import org.openfact.pe.representations.idm.VoidedRepresentation;

public class SunatModelToRepresentation {

    public static DocumentoSunatRepresentation toRepresentation(PerceptionModel model) {
        DocumentoSunatRepresentation rep = new DocumentoSunatRepresentation();

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
            rep.setSerieDocumento(splits[0].substring(1, 4));
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
            rep.setFechaDeEmision(DateUtils.asDate( model.getIssueDateTime()));
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
            rep.setFechaCambio(DateUtils.asDate(model.getChangeIssueDateTime()) );
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
            rep.setSerieDocumento(splits[0].substring(1, 4));
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
        if (model.getDocumentId() != null) {
            String[] splits = model.getDocumentId().split("-");
            rep.setSerie(splits[1]);
            rep.setNumero(splits[2]);
        }
        if (model.getIssueDate() != null) {
            rep.setFechaDeDocumento(model.getIssueDate().atStartOfDay());
        }
        if (model.getReferenceDate() != null) {
            rep.setFechaReferencia(model.getReferenceDate().atStartOfDay());
        }
        return rep;
    }

}
