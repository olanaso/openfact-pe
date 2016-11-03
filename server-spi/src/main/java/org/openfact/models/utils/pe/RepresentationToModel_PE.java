package org.openfact.models.utils.pe;

import org.apache.commons.lang.ArrayUtils;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ubl.common.ContractModel;
import org.openfact.models.ubl.common.ExchangeRateModel;
import org.openfact.models.ubl.common.MeasureModel;
import org.openfact.models.ubl.common.PaymentModel;
import org.openfact.models.ubl.common.PeriodModel;
import org.openfact.models.ubl.common.StatusModel;
import org.openfact.models.ubl.common.SummaryDocumentsLineModel;
import org.openfact.models.ubl.common.VoidedDocumentsLineModel;
import org.openfact.models.ubl.common.pe.PerceptionDocumentReferenceModel;
import org.openfact.models.ubl.common.pe.PerceptionInformationModel;
import org.openfact.models.ubl.common.pe.RetentionDocumentReferenceModel;
import org.openfact.models.ubl.common.pe.RetentionInformationModel;
import org.openfact.models.ubl.pe.PerceptionModel;
import org.openfact.models.ubl.pe.RetentionModel;
import org.openfact.models.ubl.pe.SummaryDocumentsModel;
import org.openfact.models.ubl.pe.VoidedDocumentsModel;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.representations.idm.ubl.common.AllowanceChargeRepresentation;
import org.openfact.representations.idm.ubl.common.ContractRepresentation;
import org.openfact.representations.idm.ubl.common.DocumentReferenceRepresentation;
import org.openfact.representations.idm.ubl.common.ExchangeRateRepresentation;
import org.openfact.representations.idm.ubl.common.MeasureRepresentation;
import org.openfact.representations.idm.ubl.common.PaymentRepresentation;
import org.openfact.representations.idm.ubl.common.PeriodRepresentation;
import org.openfact.representations.idm.ubl.common.SignatureRepresentation;
import org.openfact.representations.idm.ubl.common.StatusRepresentation;
import org.openfact.representations.idm.ubl.common.SummaryDocumentsLineRepresentation;
import org.openfact.representations.idm.ubl.common.TaxTotalRepresentation;
import org.openfact.representations.idm.ubl.common.VoidedDocumentsLineRepresentation;
import org.openfact.representations.idm.ubl.common.pe.PerceptionDocumentReferenceRepresentation;
import org.openfact.representations.idm.ubl.common.pe.PerceptionInformationRepresentation;
import org.openfact.representations.idm.ubl.common.pe.RetentionDocumentReferenceRepresentation;
import org.openfact.representations.idm.ubl.common.pe.RetentionInformationRepresentation;
import org.openfact.representations.idm.ubl.pe.PerceptionRepresentation;
import org.openfact.representations.idm.ubl.pe.RetentionRepresentation;
import org.openfact.representations.idm.ubl.pe.SummaryDocumentsRepresentation;
import org.openfact.representations.idm.ubl.pe.VoidedDocumentsRepresentation;

public class RepresentationToModel_PE {
	public static PerceptionModel importPerception(OpenfactSession session, OrganizationModel organization,
			PerceptionModel model, PerceptionRepresentation rep) {

		if (rep.getUblVersionID() != null) {
			model.setUblVersionID(rep.getUblVersionID());
		}
		if (rep.getCustomizationID() != null) {
			model.setCustomizationID(rep.getCustomizationID());
		}
		if (rep.getUblExtensions() != null) {
			RepresentationToModel.updateModel(model.getUblExtensions(), rep.getUblExtensions());
		}
		if (rep.getSignature() != null) {
			for (SignatureRepresentation item : rep.getSignature()) {
				RepresentationToModel.updateModel(model.addSignature(), item);
			}
		}
		if (rep.getIdUbl() != null) {
			model.setID(rep.getIdUbl());
		}
		if (rep.getIssueDateTime() != null) {
			model.setIssueDateTime(rep.getIssueDateTime());
		}
		if (rep.getNote() != null) {
			model.setNote(rep.getNote());
		}
		if (rep.getSUNATPerceptionSystemCode() != null) {
			model.setSUNATPerceptionSystemCode(rep.getSUNATPerceptionSystemCode());
		}
		if (rep.getSUNATPerceptionPercent() != null) {
			model.setSUNATPerceptionPercent(rep.getSUNATPerceptionPercent());
		}
		if (rep.getAgentParty() != null) {
			RepresentationToModel.updateModel(model.getAgentParty(), rep.getAgentParty());
		}
		if (rep.getReceiverParty() != null) {
			RepresentationToModel.updateModel(model.getReceiverParty(), rep.getReceiverParty());
		}
		if (rep.getTotalInvoiceAmount() != null) {
			model.setTotalInvoiceAmount(rep.getTotalInvoiceAmount());
		}
		if (rep.getSUNATTotalCashed() != null) {
			model.setSUNATTotalCashed(rep.getSUNATTotalCashed());
		}
		if (rep.getSUNATPerceptionDocumentReference() != null) {
			for (PerceptionDocumentReferenceRepresentation item : rep.getSUNATPerceptionDocumentReference()) {
				updateModel(model.addPerceptionDocumentReference(), item);
			}
		}
		if (rep.getXmlDocument() != null) {
			try {
				model.setXmlDocument(ArrayUtils.toObject(rep.getXmlDocument()));
			} catch (Exception e) {
				throw new ModelException(e.getMessage());
			}
		}
		return model;
	}

	public static RetentionModel importRetention(OpenfactSession session, OrganizationModel organization,
			RetentionModel model, RetentionRepresentation rep) {
		if (model.getUblVersionID() != null) {
			model.setUblVersionID(model.getUblVersionID());
		}
		if (rep.getCustomizationID() != null) {
			model.setCustomizationID(rep.getCustomizationID());
		}
		if (rep.getIdUbl() != null) {
			model.setID(rep.getIdUbl());
		}
		if (rep.getIssueDateTime() != null) {
			model.setIssueDateTime(rep.getIssueDateTime());
		}
		if (rep.getNote() != null) {
			model.setNote(rep.getNote());
		}
		if (rep.getUblExtensions() != null) {
			RepresentationToModel.updateModel(model.getUblExtensions(), rep.getUblExtensions());
		}
		if (rep.getSignature() != null) {
			for (SignatureRepresentation item : rep.getSignature()) {
				RepresentationToModel.updateModel(model.addSignature(), item);
			}
		}
		if (rep.getSUNATRetentionSystemCode() != null) {
			model.setSUNATRetentionSystemCode(rep.getSUNATRetentionSystemCode());
		}
		if (rep.getSUNATRetentionPercent() != null) {
			model.setSUNATRetentionPercent(rep.getSUNATRetentionPercent());
		}
		if (rep.getAgentParty() != null) {
			RepresentationToModel.updateModel(model.getAgentParty(), rep.getAgentParty());
		}
		if (rep.getReceiverParty() != null) {
			RepresentationToModel.updateModel(model.getReceiverParty(), rep.getReceiverParty());
		}
		if (rep.getTotalInvoiceAmount() != null) {
			model.setTotalInvoiceAmount(rep.getTotalInvoiceAmount());
		}
		if (rep.getSUNATTotalPaid() != null) {
			model.setSUNATTotalPaid(rep.getSUNATTotalPaid());
		}
		if (rep.getSUNATRetentionDocumentReference() != null) {
			for (RetentionDocumentReferenceRepresentation item : rep.getSUNATRetentionDocumentReference()) {
				updateModel(model.addRetentionDocumentReference(), item);
			}
		}
		if (rep.getXmlDocument() != null) {
			try {
				model.setXmlDocument(ArrayUtils.toObject(rep.getXmlDocument()));
			} catch (Exception e) {
				throw new ModelException(e.getMessage());
			}
		}
		return model;
	}

	public static SummaryDocumentsModel importSummaryDocuments(OpenfactSession session, OrganizationModel organization,
			SummaryDocumentsModel model, SummaryDocumentsRepresentation rep) {
		if (rep.getUblVersionID() != null) {
			model.setUblVersionID(rep.getUblVersionID());
		}
		if (rep.getCustomizationID() != null) {
			model.setCustomizationID(rep.getCustomizationID());
		}
		if (rep.getIdUbl() != null) {
			model.setID(rep.getIdUbl());
		}
		if (rep.getReferenceDateTime() != null) {
			model.setReferenceDateTime(rep.getReferenceDateTime());
		}
		if (rep.getIssueDateTime() != null) {
			model.setIssueDateTime(rep.getIssueDateTime());
		}
		if (rep.getUblExtensions() != null) {
			RepresentationToModel.updateModel(model.getUblExtensions(), rep.getUblExtensions());
		}
		if (rep.getSignature() != null) {
			for (SignatureRepresentation item : rep.getSignature()) {
				RepresentationToModel.updateModel(model.addSignature(), item);
			}
		}
		if (rep.getAccountingSupplierParty() != null) {
			RepresentationToModel.updateModel(model.getAccountingSupplierParty(), rep.getAccountingSupplierParty());
		}
		for (SummaryDocumentsLineRepresentation item : rep.getSummaryDocumentsLines()) {
			updateModel(model.addSummaryDocumentsLine(), item);
		}
		if (rep.getXmlDocument() != null) {
			try {
				model.setXmlDocument(ArrayUtils.toObject(rep.getXmlDocument()));
			} catch (Exception e) {
				throw new ModelException(e.getMessage());
			}
		}

		return model;
	}

	public static VoidedDocumentsModel importVoidedDocuments(OpenfactSession session, OrganizationModel organization,
			VoidedDocumentsModel model, VoidedDocumentsRepresentation rep) {
		if (rep.getIdUbl() != null) {
			model.setID(rep.getIdUbl());
		}
		if (rep.getUblVersionID() != null) {
			model.setUblVersionID(rep.getUblVersionID());
		}
		if (rep.getCustomizationID() != null) {
			model.setCustomizationID(rep.getCustomizationID());
		}
		if (rep.getReferenceDate() != null) {
			model.setReferenceDate(rep.getReferenceDate());
		}
		if (rep.getIssueDateTime() != null) {
			model.setIssueDateTime(rep.getIssueDateTime());
		}
		if (rep.getNote() != null) {
			model.setNote(rep.getNote());
		}
		if (rep.getUblExtensions() != null) {
			RepresentationToModel.updateModel(model.getUblExtensions(), rep.getUblExtensions());
		}
		if (rep.getSignature() != null) {
			for (SignatureRepresentation item : rep.getSignature()) {
				RepresentationToModel.updateModel(model.addSignature(), item);
			}
		}
		if (rep.getAccountingSupplierParty() != null) {
			RepresentationToModel.updateModel(model.getAccountingSupplierParty(), rep.getAccountingSupplierParty());
		}
		if (rep.getVoidedDocumentsLine() != null) {
			for (VoidedDocumentsLineRepresentation item : rep.getVoidedDocumentsLine()) {
				updateModel(model.addVoidedDocumentsLine(), item);
			}
		}
		if (rep.getXmlDocument() != null) {
			try {
				model.setXmlDocument(ArrayUtils.toObject(rep.getXmlDocument()));
			} catch (Exception e) {
				throw new ModelException(e.getMessage());
			}
		}
		return model;
	}

	public static void updateModel(VoidedDocumentsLineModel model, VoidedDocumentsLineRepresentation rep) {
		if (rep.getIdUbl() != null) {
			model.setID(rep.getIdUbl());
		}
		if (rep.getLineID() != null) {
			model.setLineID(rep.getLineID());
		}
		if (rep.getDocumentTypeCode() != null) {
			model.setDocumentTypeCode(rep.getDocumentTypeCode());
		}
		if (rep.getDocumentSerialID() != null) {
			model.setDocumentSerialID(rep.getDocumentSerialID());
		}
		if (rep.getDocumentNumberID() != null) {
			model.setDocumentNumberID(rep.getDocumentNumberID());
		}
		if (rep.getVoidReasonDescription() != null) {
			model.setVoidReasonDescription(rep.getVoidReasonDescription());
		}
	}

	public static void updateModel(SummaryDocumentsLineModel model, SummaryDocumentsLineRepresentation rep) {
		if (rep.getIdUbl() != null) {
			model.setID(rep.getIdUbl());
		}
		if (rep.getLineID() != null) {
			model.setLineID(rep.getLineID());
		}
		if (rep.getDocumentTypeCode() != null) {
			model.setDocumentTypeCode(rep.getDocumentTypeCode());
		}
		if (rep.getDocumentSerialID() != null) {
			model.setDocumentSerialID(rep.getDocumentSerialID());
		}
		if (rep.getStartDocumentNumberID() != null) {
			model.setStartDocumentNumberID(rep.getStartDocumentNumberID());
		}
		if (rep.getEndDocumentNumberID() != null) {
			model.setEndDocumentNumberID(rep.getEndDocumentNumberID());
		}
		if (rep.getTotalAmount() != null) {
			model.setTotalAmount(rep.getTotalAmount());
		}
		if (rep.getBillingPayment() != null) {
			for (PaymentRepresentation item : rep.getBillingPayment()) {
				updateModel(model.addPayment(), item);
			}
		}
		if (rep.getAccountingCustomerParty() != null) {
			RepresentationToModel.updateModel(model.getAccountingCustomerParty(), rep.getAccountingCustomerParty());
		}
		if (rep.getBillingReference() != null) {
			RepresentationToModel.updateModel(model.getBillingReference(), rep.getBillingReference());
		}
		if (rep.getStatus() != null) {
			updateModel(model.getStatus(), rep.getStatus());
		}
		if (rep.getAllowanceCharge() != null) {
			for (AllowanceChargeRepresentation item : rep.getAllowanceCharge()) {
				RepresentationToModel.updateModel(model.addAllowanceCharge(), item);
			}
		}
		if (rep.getTaxTotal() != null) {
			for (TaxTotalRepresentation item : rep.getTaxTotal()) {
				RepresentationToModel.updateModel(model.addTaxTotal(), item);
			}
		}
	}

	public static void updateModel(StatusModel model, StatusRepresentation rep) {
		if (rep.getConditionCode() != null) {
			model.setConditionCode(rep.getConditionCode());
		}
		if (rep.getReferenceDate() != null) {
			model.setReferenceDate(rep.getReferenceDate());
		}
		if (rep.getReferenceTime() != null) {
			model.setReferenceTime(rep.getReferenceTime());
		}
		if (rep.getDescription() != null) {
			model.setDescription(rep.getDescription());
		}
		if (rep.getStatusReason() != null) {
			model.setStatusReason(rep.getStatusReason());
		}
		if (rep.getStatusReasonCode() != null) {
			model.setStatusReasonCode(rep.getStatusReasonCode());
		}
		if (rep.getSequenceID() != null) {
			model.setSequenceID(rep.getSequenceID());
		}
		if (rep.getText() != null) {
			model.setText(rep.getText());
		}
		model.setIndicationIndicator(rep.isIndicationIndicator());
		if (rep.getPercent() != null) {
			model.setPercent(rep.getPercent());
		}
	}

	public static void updateModel(RetentionDocumentReferenceModel model,
			RetentionDocumentReferenceRepresentation rep) {
		if (rep.getIdUbl() != null) {
			model.setID(rep.getIdUbl());
		}
		if (rep.getIssueDateTime() != null) {
			model.setIssueDateTime(rep.getIssueDateTime());
		}
		if (rep.getTotalInvoiceAmount() != null) {
			model.setTotalInvoiceAmount(rep.getTotalInvoiceAmount());
		}
		if (rep.getPayment() != null) {
			updateModel(model.getPayment(), rep.getPayment());
		}
		if (rep.getSunatRetentionInformation() != null) {
			updateModel(model.getSunatRetentionInformation(), rep.getSunatRetentionInformation());
		}
	}

	public static void updateModel(RetentionInformationModel model, RetentionInformationRepresentation rep) {
		if (rep.getSunatRetentionAmount() != null) {
			model.setSunatRetentionAmount(rep.getSunatRetentionAmount());
		}
		if (rep.getSunatRetentionDate() != null) {
			model.setSunatRetentionDate(rep.getSunatRetentionDate());
		}
		if (rep.getSunatNetTotalPaid() != null) {
			model.setSunatNetTotalPaid(rep.getSunatNetTotalPaid());
		}
		if (rep.getExchangeRate() != null) {
			updateModel(model.getExchangeRate(), rep.getExchangeRate());
		}
	}

	public static void updateModel(PerceptionDocumentReferenceModel model,
			PerceptionDocumentReferenceRepresentation rep) {
		if (rep.getIdUbl() != null) {
			model.setID(rep.getIdUbl());
		}
		if (rep.getIssueDateTime() != null) {
			model.setIssueDateTime(rep.getIssueDateTime());
		}
		if (rep.getTotalInvoiceAmount() != null) {
			model.setTotalInvoiceAmount(rep.getTotalInvoiceAmount());
		}
		if (rep.getPayment() != null) {
			updateModel(model.getPayment(), rep.getPayment());
		}
		if (rep.getSunatPerceptionInformation() != null) {
			updateModel(model.getSunatPerceptionInformation(), rep.getSunatPerceptionInformation());
		}
	}

	public static void updateModel(PerceptionInformationModel model, PerceptionInformationRepresentation rep) {
		if (rep.getSunatPerceptionAmount() != null) {
			model.setSunatPerceptionAmount(rep.getSunatPerceptionAmount());
		}
		if (rep.getSunatPerceptionDate() != null) {
			model.setSunatPerceptionDate(rep.getSunatPerceptionDate());
		}
		if (rep.getSunatNetTotalPaid() != null) {
			model.setSunatNetTotalPaid(rep.getSunatNetTotalPaid());
		}
		if (rep.getExchangeRate() != null) {
			updateModel(model.getExchangeRate(), rep.getExchangeRate());
		}

	}

	public static void updateModel(ExchangeRateModel model, ExchangeRateRepresentation rep) {
		if (rep.getSourceCurrencyCode() != null) {
			model.setSourceCurrencyCode(rep.getSourceCurrencyCode());
		}
		if (rep.getSourceCurrencyBaseRate() != null) {
			model.setSourceCurrencyBaseRate(rep.getSourceCurrencyBaseRate());
		}
		if (rep.getTargetCurrencyCode() != null) {
			model.setTargetCurrencyCode(rep.getTargetCurrencyCode());
		}
		if (rep.getTargetCurrencyBaseRate() != null) {
			model.setTargetCurrencyBaseRate(rep.getTargetCurrencyBaseRate());
		}
		if (rep.getExchangeMarketID() != null) {
			model.setExchangeMarketID(rep.getExchangeMarketID());
		}
		if (rep.getCalculationRate() != null) {
			model.setCalculationRate(rep.getCalculationRate());
		}
		if (rep.getMathematicOperatorCode() != null) {
			model.setMathematicOperatorCode(rep.getMathematicOperatorCode());
		}
		if (rep.getDate() != null) {
			model.setDate(rep.getDate());
		}
		if (rep.getForeignExchangeContract() != null) {
			updateModel(model.getForeignExchangeContract(), rep.getForeignExchangeContract());
		}
	}

	public static void updateModel(ContractModel model, ContractRepresentation rep) {
		if (rep.getIdUbl() != null) {
			model.setID(rep.getIdUbl());
		}
		if (rep.getIssueDate() != null) {
			model.setIssueDate(rep.getIssueDate());
		}
		if (rep.getIssueTime() != null) {
			model.setIssueTime(rep.getIssueTime());
		}
		if (rep.getContractTypeCode() != null) {
			model.setContractTypeCode(rep.getContractTypeCode());
		}
		if (rep.getContractType() != null) {
			model.setContractType(rep.getContractType());
		}
		if (rep.getValidityPeriod() != null) {
			updateModel(model.getValidityPeriod(), rep.getValidityPeriod());
		}
		for (DocumentReferenceRepresentation item : rep.getContractDocumentReference()) {
			RepresentationToModel.updateModel(model.addContractDocumentReference(), item);
		}
	}

	public static void updateModel(PeriodModel model, PeriodRepresentation rep) {
		if (rep.getStartDate() != null) {
			model.setStartDate(rep.getStartDate());
		}
		if (rep.getStartTime() != null) {
			model.setStartTime(rep.getStartTime());
		}
		if (rep.getEndDate() != null) {
			model.setEndDate(rep.getEndDate());
		}
		if (rep.getEndTime() != null) {
			model.setEndTime(rep.getEndTime());
		}
		if (rep.getDurationMeasure() != null) {
			updateModel(model.getDurationMeasure(), rep.getDurationMeasure());
		}
		if (rep.getDescriptionCode() != null) {
			model.setDescriptionCode(rep.getDescriptionCode());
		}
		if (rep.getDescription() != null) {
			model.setDescription(rep.getDescription());
		}
	}

	public static void updateModel(MeasureModel model, MeasureRepresentation rep) {
		if (rep.getValue() != null) {
			model.setValue(rep.getValue());
		}
		if (rep.getUnitCode() != null) {
			model.setUnitCode(rep.getUnitCode());
		}
	}

	public static void updateModel(PaymentModel model, PaymentRepresentation rep) {
		if (rep.getIdUbl() != null) {
			model.setID(rep.getIdUbl());
		}
		if (rep.getPaidAmount() != null) {
			model.setPaidAmount(rep.getPaidAmount());
		}
		if (rep.getReceivedDate() != null) {
			model.setReceivedDate(rep.getReceivedDate());
		}
		if (rep.getPaidDate() != null) {
			model.setPaidDate(rep.getPaidDate());
		}
		if (rep.getPaidTime() != null) {
			model.setPaidTime(rep.getPaidTime());
		}
		if (rep.getInstructionID() != null) {
			model.setInstructionID(rep.getInstructionID());
		}
	}
}
