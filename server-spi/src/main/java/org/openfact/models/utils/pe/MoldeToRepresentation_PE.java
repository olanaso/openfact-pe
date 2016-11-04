package org.openfact.models.utils.pe;

import org.apache.commons.lang.ArrayUtils;
import org.openfact.models.ModelException;
import org.openfact.models.ubl.common.AllowanceChargeModel;
import org.openfact.models.ubl.common.ContractModel;
import org.openfact.models.ubl.common.DocumentReferenceModel;
import org.openfact.models.ubl.common.ExchangeRateModel;
import org.openfact.models.ubl.common.MeasureModel;
import org.openfact.models.ubl.common.PaymentModel;
import org.openfact.models.ubl.common.PeriodModel;
import org.openfact.models.ubl.common.SignatureModel;
import org.openfact.models.ubl.common.StatusModel;
import org.openfact.models.ubl.common.SummaryDocumentsLineModel;
import org.openfact.models.ubl.common.TaxTotalModel;
import org.openfact.models.ubl.common.VoidedDocumentsLineModel;
import org.openfact.models.ubl.common.pe.PerceptionDocumentReferenceModel;
import org.openfact.models.ubl.common.pe.PerceptionInformationModel;
import org.openfact.models.ubl.common.pe.RetentionDocumentReferenceModel;
import org.openfact.models.ubl.common.pe.RetentionInformationModel;
import org.openfact.models.ubl.pe.PerceptionModel;
import org.openfact.models.ubl.pe.RetentionModel;
import org.openfact.models.ubl.pe.SummaryDocumentsModel;
import org.openfact.models.ubl.pe.VoidedDocumentsModel;
import org.openfact.models.utils.ModelToRepresentation;
import org.openfact.representations.idm.ubl.common.ContractRepresentation;
import org.openfact.representations.idm.ubl.common.ExchangeRateRepresentation;
import org.openfact.representations.idm.ubl.common.MeasureRepresentation;
import org.openfact.representations.idm.ubl.common.PaymentRepresentation;
import org.openfact.representations.idm.ubl.common.PeriodRepresentation;
import org.openfact.representations.idm.ubl.common.StatusRepresentation;
import org.openfact.representations.idm.ubl.common.SummaryDocumentsLineRepresentation;
import org.openfact.representations.idm.ubl.common.VoidedDocumentsLineRepresentation;
import org.openfact.representations.idm.ubl.common.pe.PerceptionDocumentReferenceRepresentation;
import org.openfact.representations.idm.ubl.common.pe.PerceptionInformationRepresentation;
import org.openfact.representations.idm.ubl.common.pe.RetentionDocumentReferenceRepresentation;
import org.openfact.representations.idm.ubl.common.pe.RetentionInformationRepresentation;
import org.openfact.representations.idm.ubl.pe.PerceptionRepresentation;
import org.openfact.representations.idm.ubl.pe.RetentionRepresentation;
import org.openfact.representations.idm.ubl.pe.SummaryDocumentsRepresentation;
import org.openfact.representations.idm.ubl.pe.VoidedDocumentsRepresentation;

public class MoldeToRepresentation_PE {

	public static PerceptionRepresentation toRepresentation(PerceptionModel model) {
		PerceptionRepresentation rep = new PerceptionRepresentation();
		rep.setId(model.getId());
		rep.setUblVersionID(model.getUblVersionID());
		rep.setCustomizationID(model.getCustomizationID());
		if (model.getUblExtensions() != null) {
			rep.setUblExtensions(ModelToRepresentation.toRepresentation(model.getUblExtensions()));
		}
		for (SignatureModel item : model.getSignature()) {
			rep.addSignature(ModelToRepresentation.toRepresentation(item));
		}
		rep.setDocumentCurrencyCode(model.getDocumentCurrencyCode());
		rep.setIdUbl(model.getID());
		rep.setIssueDateTime(model.getIssueDateTime());
		rep.setNote(model.getNote());
		rep.setSUNATPerceptionSystemCode(model.getSUNATPerceptionSystemCode());
		rep.setSUNATPerceptionPercent(model.getSUNATPerceptionPercent());
		if (model.getAgentParty() != null) {
			rep.setAgentParty(ModelToRepresentation.toRepresentation(model.getAgentParty()));
		}
		if (model.getReceiverParty() != null) {
			rep.setAgentParty(ModelToRepresentation.toRepresentation(model.getReceiverParty()));
		}
		rep.setTotalInvoiceAmount(model.getTotalInvoiceAmount());
		rep.setSUNATTotalCashed(model.getSUNATTotalCashed());
		for (PerceptionDocumentReferenceModel item : model.getSUNATPerceptionDocumentReference()) {
			rep.addPerceptionDocumentReference(toRepresentation(item));
		}
		if (model.getXmlDocument() != null) {
			try {
				rep.setXmlDocument(ArrayUtils.toPrimitive(model.getXmlDocument()));
			} catch (Exception e) {
				throw new ModelException(e.getMessage());
			}
		}
		return rep;
	}

	public static RetentionRepresentation toRepresentation(RetentionModel model) {
		RetentionRepresentation rep = new RetentionRepresentation();
		rep.setId(model.getId());
		rep.setUblVersionID(model.getUblVersionID());
		rep.setCustomizationID(model.getCustomizationID());
		rep.setIdUbl(model.getID());
		rep.setIssueDateTime(model.getIssueDateTime());
		rep.setNote(model.getNote());
		rep.setDocumentCurrencyCode(model.getDocumentCurrencyCode());
		if (model.getUblExtensions() != null) {
			rep.setUblExtensions(ModelToRepresentation.toRepresentation(model.getUblExtensions()));
		}
		for (SignatureModel item : model.getSignature()) {
			rep.addSignature(ModelToRepresentation.toRepresentation(item));
		}
		rep.setSUNATRetentionSystemCode(model.getSUNATRetentionSystemCode());
		rep.setSUNATRetentionPercent(model.getSUNATRetentionPercent());
		if (model.getAgentParty() != null) {
			rep.setAgentParty(ModelToRepresentation.toRepresentation(model.getAgentParty()));
		}
		if (model.getReceiverParty() != null) {
			rep.setAgentParty(ModelToRepresentation.toRepresentation(model.getReceiverParty()));
		}
		rep.setTotalInvoiceAmount(model.getTotalInvoiceAmount());
		rep.setSUNATTotalPaid(model.getSUNATTotalPaid());
		for (RetentionDocumentReferenceModel item : model.getSUNATRetentionDocumentReference()) {
			rep.addRetentionDocumentReference(toRepresentation(item));
		}
		if (model.getXmlDocument() != null) {
			try {
				rep.setXmlDocument(ArrayUtils.toPrimitive(model.getXmlDocument()));
			} catch (Exception e) {
				throw new ModelException(e.getMessage());
			}
		}
		return rep;
	}

	public static SummaryDocumentsRepresentation toRepresentation(SummaryDocumentsModel model) {
		SummaryDocumentsRepresentation rep = new SummaryDocumentsRepresentation();
		rep.setId(model.getId());
		rep.setDocumentCurrencyCode(model.getDocumentCurrencyCode());
		rep.setUblVersionID(model.getUblVersionID());
		rep.setCustomizationID(model.getCustomizationID());
		rep.setIdUbl(model.getID());
		rep.setReferenceDateTime(model.getReferenceDateTime());
		rep.setIssueDateTime(model.getIssueDateTime());
		if (model.getUblExtensions() != null) {
			rep.setUblExtensions(ModelToRepresentation.toRepresentation(model.getUblExtensions()));
		}
		for (SignatureModel item : model.getSignature()) {
			rep.addSignature(ModelToRepresentation.toRepresentation(item));
		}
		if (model.getAccountingSupplierParty() != null) {
			rep.setAccountingSupplierParty(ModelToRepresentation.toRepresentation(model.getAccountingSupplierParty()));
		}
		for (SummaryDocumentsLineModel item : model.getSummaryDocumentsLines()) {
			rep.addSummaryDocumentsLine(toRepresentation(item));
		}
		if (model.getXmlDocument() != null) {
			try {
				rep.setXmlDocument(ArrayUtils.toPrimitive(model.getXmlDocument()));
			} catch (Exception e) {
				throw new ModelException(e.getMessage());
			}
		}
		return rep;
	}
	
	public static VoidedDocumentsRepresentation toRepresentation(VoidedDocumentsModel model) {
		VoidedDocumentsRepresentation rep = new VoidedDocumentsRepresentation();
		rep.setId(model.getId());
		rep.setIdUbl(model.getID());
		rep.setUblVersionID(model.getUblVersionID());
		rep.setCustomizationID(model.getCustomizationID());
		rep.setReferenceDate(model.getReferenceDate());
		rep.setIssueDateTime(model.getIssueDateTime());
		rep.setNote(model.getNote());
		rep.setDocumentCurrencyCode(model.getDocumentCurrencyCode());
		if (model.getUblExtensions() != null) {
			rep.setUblExtensions(ModelToRepresentation.toRepresentation(model.getUblExtensions()));
		}
		for (SignatureModel item : model.getSignature()) {
			rep.addSignature(ModelToRepresentation.toRepresentation(item));
		}
		if (model.getAccountingSupplierParty() != null) {
			rep.setAccountingSupplierParty(ModelToRepresentation.toRepresentation(model.getAccountingSupplierParty()));
		}

		for (VoidedDocumentsLineModel item : model.getVoidedDocumentsLine()) {
			rep.addVoidedDocumentsLine(toRepresentation(item));
		}
		if (model.getXmlDocument() != null) {
			try {
				rep.setXmlDocument(ArrayUtils.toPrimitive(model.getXmlDocument()));
			} catch (Exception e) {
				throw new ModelException(e.getMessage());
			}
		}
		return rep;
	}

	public static VoidedDocumentsLineRepresentation toRepresentation(VoidedDocumentsLineModel model) {
		VoidedDocumentsLineRepresentation rep = new VoidedDocumentsLineRepresentation();
		rep.setId(model.getId());		
		rep.setLineID(model.getLineID());
		rep.setDocumentTypeCode(model.getDocumentTypeCode());
		rep.setDocumentSerialID(model.getDocumentSerialID());
		rep.setDocumentNumberID(model.getDocumentNumberID());
		rep.setVoidReasonDescription(model.getVoidReasonDescription());
		return rep;
	}

	public static SummaryDocumentsLineRepresentation toRepresentation(SummaryDocumentsLineModel model) {
		SummaryDocumentsLineRepresentation rep = new SummaryDocumentsLineRepresentation();
		rep.setId(model.getId());
		rep.setIdUbl(model.getID());
		rep.setLineID(model.getLineID());
		rep.setDocumentTypeCode(model.getDocumentTypeCode());
		rep.setDocumentSerialID(model.getDocumentSerialID());
		rep.setStartDocumentNumberID(model.getStartDocumentNumberID());
		rep.setEndDocumentNumberID(model.getEndDocumentNumberID());
		rep.setTotalAmount(model.getTotalAmount());
		for (PaymentModel item : model.getBillingPayment()) {
			rep.addPayment(toRepresentation(item));
		}
		if (model.getAccountingCustomerParty() != null) {
			rep.setAccountingCustomerParty(ModelToRepresentation.toRepresentation(model.getAccountingCustomerParty()));
		}
		if (model.getBillingReference() != null) {
			rep.setBillingReference(ModelToRepresentation.toRepresentation(model.getBillingReference()));
		}
		if (model.getStatus() != null) {
			rep.setStatus(toRepresentation(model.getStatus()));
		}
		for (AllowanceChargeModel item : model.getAllowanceCharge()) {
			rep.addAllowanceCharge(ModelToRepresentation.toRepresentation(item));
		}
		for (TaxTotalModel item : model.getTaxTotal()) {
			rep.addTaxTotal(ModelToRepresentation.toRepresentation(item));
		}
		return rep;
	}

	public static StatusRepresentation toRepresentation(StatusModel model) {
		StatusRepresentation rep = new StatusRepresentation();
		rep.setId(model.getId());
		rep.setConditionCode(model.getConditionCode());
		rep.setReferenceDate(model.getReferenceDate());
		rep.setReferenceTime(model.getReferenceTime());
		rep.setDescription(model.getDescription());
		rep.setStatusReason(model.getStatusReason());
		rep.setStatusReasonCode(model.getStatusReasonCode());
		rep.setSequenceID(model.getSequenceID());
		rep.setText(model.getText());
		rep.setIndicationIndicator(model.getIndicationIndicator());
		rep.setPercent(model.getPercent());
		return rep;
	}

	public static RetentionDocumentReferenceRepresentation toRepresentation(RetentionDocumentReferenceModel model) {
		RetentionDocumentReferenceRepresentation rep = new RetentionDocumentReferenceRepresentation();
		rep.setId(model.getId());
		rep.setIdUbl(model.getID());
		rep.setIssueDateTime(model.getIssueDateTime());
		rep.setTotalInvoiceAmount(model.getTotalInvoiceAmount());
		if (model.getPayment() != null) {
			rep.setPayment(toRepresentation(model.getPayment()));
		}
		if (model.getSunatRetentionInformation() != null) {
			rep.setSunatRetentionInformation(toRepresentation(model.getSunatRetentionInformation()));
		}
		return rep;
	}

	public static RetentionInformationRepresentation toRepresentation(RetentionInformationModel model) {
		RetentionInformationRepresentation rep = new RetentionInformationRepresentation();
		rep.setId(model.getId());
		rep.setSunatRetentionAmount(model.getSunatRetentionAmount());
		rep.setSunatRetentionDate(model.getSunatRetentionDate());
		rep.setSunatNetTotalPaid(model.getSunatNetTotalPaid());
		if (model.getExchangeRate() != null) {
			rep.setExchangeRate(toRepresentation(model.getExchangeRate()));
		}
		return rep;
	}

	public static PerceptionDocumentReferenceRepresentation toRepresentation(PerceptionDocumentReferenceModel model) {
		PerceptionDocumentReferenceRepresentation rep = new PerceptionDocumentReferenceRepresentation();
		rep.setId(model.getId());
		rep.setIdUbl(model.getID());
		rep.setIssueDateTime(model.getIssueDateTime());
		rep.setTotalInvoiceAmount(model.getTotalInvoiceAmount());
		if (model.getPayment() != null) {
			rep.setPayment(toRepresentation(model.getPayment()));
		}
		if (model.getSunatPerceptionInformation() != null) {
			rep.setSunatPerceptionInformation(toRepresentation(model.getSunatPerceptionInformation()));
		}
		return rep;
	}

	public static PerceptionInformationRepresentation toRepresentation(PerceptionInformationModel model) {
		PerceptionInformationRepresentation rep = new PerceptionInformationRepresentation();
		rep.setId(model.getId());
		rep.setSunatPerceptionAmount(model.getSunatPerceptionAmount());
		rep.setSunatPerceptionDate(model.getSunatPerceptionDate());
		rep.setSunatNetTotalPaid(model.getSunatNetTotalPaid());
		if (model.getExchangeRate() != null) {
			rep.setExchangeRate(toRepresentation(model.getExchangeRate()));
		}
		return rep;
	}

	public static ExchangeRateRepresentation toRepresentation(ExchangeRateModel model) {
		ExchangeRateRepresentation rep = new ExchangeRateRepresentation();
		rep.setId(model.getId());
		rep.setSourceCurrencyCode(model.getSourceCurrencyCode());
		rep.setSourceCurrencyBaseRate(model.getSourceCurrencyBaseRate());
		rep.setTargetCurrencyCode(model.getTargetCurrencyCode());
		rep.setTargetCurrencyBaseRate(model.getTargetCurrencyBaseRate());
		rep.setExchangeMarketID(model.getExchangeMarketID());
		rep.setCalculationRate(model.getCalculationRate());
		rep.setMathematicOperatorCode(model.getMathematicOperatorCode());
		rep.setDate(model.getDate());
		if (model.getForeignExchangeContract() != null) {
			rep.setForeignExchangeContract(toRepresentation(model.getForeignExchangeContract()));
		}
		return rep;
	}

	public static ContractRepresentation toRepresentation(ContractModel model) {
		ContractRepresentation rep = new ContractRepresentation();
		rep.setId(model.getId());
		rep.setIdUbl(model.getID());
		rep.setIssueDate(model.getIssueDate());
		rep.setIssueTime(model.getIssueTime());
		rep.setContractTypeCode(model.getContractTypeCode());
		rep.setContractType(model.getContractType());
		if (model.getValidityPeriod() != null) {
			rep.setValidityPeriod(toRepresentation(model.getValidityPeriod()));
		}
		for (DocumentReferenceModel item : model.getContractDocumentReference()) {
			rep.addDocumentReference(ModelToRepresentation.toRepresentation(item));
		}
		return rep;
	}

	public static PeriodRepresentation toRepresentation(PeriodModel model) {
		PeriodRepresentation rep = new PeriodRepresentation();
		rep.setId(model.getId());
		rep.setStartDate(model.getStartDate());
		rep.setStartTime(model.getStartTime());
		rep.setEndDate(model.getEndDate());
		rep.setEndTime(model.getEndTime());
		if (model.getDurationMeasure() != null) {
			rep.setDurationMeasure(toRepresentation(model.getDurationMeasure()));
		}
		rep.setDescriptionCode(model.getDescriptionCode());
		rep.setDescription(model.getDescription());
		return rep;
	}

	public static MeasureRepresentation toRepresentation(MeasureModel model) {
		MeasureRepresentation rep = new MeasureRepresentation();
		rep.setId(model.getId());
		rep.setValue(model.getValue());
		rep.setUnitCode(model.getUnitCode());
		return rep;
	}

	public static PaymentRepresentation toRepresentation(PaymentModel model) {
		PaymentRepresentation rep = new PaymentRepresentation();
		rep.setId(model.getId());
		rep.setIdUbl(model.getID());
		rep.setPaidAmount(model.getPaidAmount());
		rep.setReceivedDate(model.getReceivedDate());
		rep.setPaidDate(model.getPaidDate());
		rep.setPaidTime(model.getPaidTime());
		rep.setInstructionID(model.getInstructionID());
		return rep;
	}

}
