package org.openfact.pe.models.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.openfact.models.ContactModel;
import org.openfact.models.OrganizationModel;
import org.openfact.models.PartyModel;
import org.openfact.pe.models.PerceptionDocumentReferenceModel;
import org.openfact.pe.models.PerceptionInformationModel;
import org.openfact.pe.models.PerceptionModel;
import org.openfact.pe.models.RetentionDocumentReferenceModel;
import org.openfact.pe.models.RetentionModel;
import org.openfact.pe.models.SummaryDocumentModel;
import org.openfact.pe.models.VoidedDocumentModel;
import org.openfact.pe.representations.idm.DocumentReferenceRepresentation;
import org.openfact.pe.representations.idm.RetentionRepresentation;
import org.openfact.pe.representations.idm.SummaryRepresentation;
import org.openfact.pe.representations.idm.VoidedRepresentation;

public class SunatModelToRepresentation {

	public static RetentionRepresentation toRepresentation(OrganizationModel organization, PerceptionModel model) {
		RetentionRepresentation rep = new RetentionRepresentation();

		if (model.getDocumentId() != null) {
			rep.setSerie(model.getDocumentId().substring(0, 3));
			rep.setNumero(model.getDocumentId().substring(5, model.getDocumentId().length()));
		}
		if (organization.getAssignedIdentificationId() != null) {
			rep.setEntidadNumeroDeDocumento(organization.getAssignedIdentificationId());
		}
		if (organization.getAdditionalAccountId() != null) {
			rep.setEntidadTipoDeDocumento(organization.getAdditionalAccountId());
		}
		if (organization.getRegistrationName() != null) {
			rep.setEntidadDenominacion(organization.getRegistrationName());
		}
		if (organization.getStreetName() != null) {
			rep.setEntidadDireccion(organization.getStreetName());
		}
		if (model.getAgentParty() != null) {
			toRepresentation(rep, model.getAgentParty());
		}
		if (model.getIssueDate() != null) {
			rep.setFechaDeEmision(model.getIssueDate().atStartOfDay());
		}
		if (model.getDocumentCurrencyCode() != null) {
			rep.setMoneda(model.getDocumentCurrencyCode());
		}
		if (model.getSUNATPerceptionSystemCode() != null) {
			rep.setCodigoDocumento(model.getSUNATPerceptionSystemCode());
		}
		if (model.getSUNATPerceptionPercent() != null) {
			rep.setTasaDocumento(model.getSUNATPerceptionPercent());
		}
		if (model.getNotes() != null) {
			rep.setObservaciones(String.join(", ", model.getNotes()));
		}
		if (model.getId() != null) {
			rep.setCodigoUnico(model.getId());
		}
		if (model.getSunatPerceptionDocumentReference() != null) {
			for (PerceptionDocumentReferenceModel item : model.getSunatPerceptionDocumentReference()) {
				rep.addDocumentReference(toRepresentation(item));
			}
		}
		return rep;
	}

	private static DocumentReferenceRepresentation toRepresentation(PerceptionDocumentReferenceModel model) {
		DocumentReferenceRepresentation rep = new DocumentReferenceRepresentation();

		if( model.getIssueDate()!=null){
			rep.setFechaDeDocumentoRelacionado(model.getIssueDate().atStartOfDay());
		}
		if(model.getDocumentId()!=null){
			rep.setNumeroDocumentRelacionado(model.getDocumentId());
		}
		if(model.getTotalInvoiceAmount()!=null){
			rep.setTotalDocumentoRelacionado(model.getTotalInvoiceAmount());
		}
		return rep;
	}

	private static void toRepresentation(RetentionRepresentation rep, PartyModel model) {
		if (model.getContact() != null) {
			toRepresentation(rep, model.getContact());
		}
	}

	private static void toRepresentation(RetentionRepresentation rep, ContactModel model) {
		if (model.getElectronicMail() != null) {
			rep.setEntidadEmail(model.getElectronicMail());
		}
	}

	public static RetentionRepresentation toRepresentation(OrganizationModel organization ,RetentionModel model) {
		RetentionRepresentation rep = new RetentionRepresentation();

		if (model.getDocumentId() != null) {
			rep.setSerie(model.getDocumentId().substring(0, 3));
			rep.setNumero(model.getDocumentId().substring(5, model.getDocumentId().length()));
		}
		if (organization.getAssignedIdentificationId() != null) {
			rep.setEntidadNumeroDeDocumento(organization.getAssignedIdentificationId());
		}
		if (organization.getAdditionalAccountId() != null) {
			rep.setEntidadTipoDeDocumento(organization.getAdditionalAccountId());
		}
		if (organization.getRegistrationName() != null) {
			rep.setEntidadDenominacion(organization.getRegistrationName());
		}
		if (organization.getStreetName() != null) {
			rep.setEntidadDireccion(organization.getStreetName());
		}
		if (model.getAgentParty() != null) {
			toRepresentation(rep, model.getAgentParty());
		}
		if (model.getIssueDate() != null) {
			rep.setFechaDeEmision(model.getIssueDate().atStartOfDay());
		}
		if (model.getDocumentCurrencyCode() != null) {
			rep.setMoneda(model.getDocumentCurrencyCode());
		}
		if (model.getSunatRetentionSystemCode() != null) {
			rep.setCodigoDocumento(model.getSunatRetentionSystemCode());
		}
		if (model.getSunatRetentionPercent() != null) {
			rep.setTasaDocumento(model.getSunatRetentionPercent());
		}
		if (model.getNotes() != null) {
			rep.setObservaciones(String.join(", ", model.getNotes()));
		}
		if (model.getId() != null) {
			rep.setCodigoUnico(model.getId());
		}
		if (model.getSunatRetentionDocumentReference() != null) {
			for (RetentionDocumentReferenceModel item : model.getSunatRetentionDocumentReference()) {
				rep.addDocumentReference(toRepresentation(item));
			}
		}
		return rep;
	}

	private static DocumentReferenceRepresentation toRepresentation(RetentionDocumentReferenceModel model) {
		DocumentReferenceRepresentation rep = new DocumentReferenceRepresentation();

		if( model.getIssueDate()!=null){
			rep.setFechaDeDocumentoRelacionado(model.getIssueDate().atStartOfDay());
		}
		if(model.getDocumentId()!=null){
			rep.setNumeroDocumentRelacionado(model.getDocumentId());
		}
		if(model.getTotalInvoiceAmount()!=null){
			rep.setTotalDocumentoRelacionado(model.getTotalInvoiceAmount());
		}
		return rep;
	}

	public static SummaryRepresentation toRepresentation(SummaryDocumentModel model) {
		SummaryRepresentation rep = new SummaryRepresentation();
		return rep;
	}

	public static VoidedRepresentation toRepresentation(VoidedDocumentModel model) {
		VoidedRepresentation rep = new VoidedRepresentation();
		return rep;
	}

}
