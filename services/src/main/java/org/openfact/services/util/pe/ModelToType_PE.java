package org.openfact.services.util.pe;

import javax.xml.datatype.DatatypeConfigurationException;

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
import org.openfact.models.utils.ModelToType;
import org.openfact.ubl.pe.sunat.PerceptionType;
import org.openfact.ubl.pe.sunat.RetentionType;
import org.openfact.ubl.pe.sunat.SUNATPerceptionDocumentReferenceType;
import org.openfact.ubl.pe.sunat.SUNATPerceptionInformationType;
import org.openfact.ubl.pe.sunat.SUNATRetentionDocumentReferenceType;
import org.openfact.ubl.pe.sunat.SUNATRetentionInformationType;
import org.openfact.ubl.pe.sunat.SummaryDocumentsLineType;
import org.openfact.ubl.pe.sunat.SummaryDocumentsType;
import org.openfact.ubl.pe.sunat.VoidedDocumentsLineType;
import org.openfact.ubl.pe.sunat.VoidedDocumentsType;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ContractType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ExchangeRateType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PaymentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PeriodType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.StatusType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DescriptionCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DurationMeasureType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.StatusReasonType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TextType;

public class ModelToType_PE {
	public static PerceptionType toType(PerceptionModel model) throws DatatypeConfigurationException {
		PerceptionType type = new PerceptionType();
		if (model.getUblVersionID() != null) {
			type.setUblVersionID(model.getUblVersionID());
		}
		if (model.getCustomizationID() != null) {
			type.setCustomizationID(model.getCustomizationID());
		}
		if (model.getSignature() != null) {
			for (SignatureModel item : model.getSignature()) {
				type.addSignature(ModelToType.toType(item));
			}
		}
		if (model.getID() != null) {
			type.setId(model.getID());
		}
		if (model.getIssueDateTime() != null) {
			type.setIssueDate(ModelToType.toType(model.getIssueDateTime().toLocalDate()));
		}
		if (model.getNote() != null) {
			for (String value : model.getNote()) {
				type.addNote(value);
			}
		}
		if (model.getSUNATPerceptionSystemCode() != null) {
			type.setSunatPerceptionSystemCode(model.getSUNATPerceptionSystemCode());
		}
		if (model.getSUNATPerceptionPercent() != null) {
			type.setSunatPerceptionPercent(model.getSUNATPerceptionPercent());
		}
		if (model.getAgentParty() != null) {
			type.setAgentParty(ModelToType.toType(model.getAgentParty()));
		}
		if (model.getReceiverParty() != null) {
			type.setAgentParty(ModelToType.toType(model.getReceiverParty()));
		}
		if (model.getTotalInvoiceAmount() != null) {
			type.setTotalInvoiceAmount(model.getTotalInvoiceAmount(), model.getDocumentCurrencyCode());
		}
		if (model.getSUNATTotalCashed() != null) {
			type.setSunatTotalCashed(model.getSUNATTotalCashed(), model.getDocumentCurrencyCode());
		}
		for (PerceptionDocumentReferenceModel item : model.getSUNATPerceptionDocumentReference()) {
			type.addPerceptionDocumentReference(toType(item, model.getDocumentCurrencyCode()));
		}
		type.setDocumentCurrencyCode(model.getDocumentCurrencyCode());
		return type;
	}

	public static RetentionType toType(RetentionModel model) throws DatatypeConfigurationException {
		RetentionType type = new RetentionType();
		if (model.getUblVersionID() != null) {
			type.setUblVersionID(model.getUblVersionID());
		}
		if (model.getCustomizationID() != null) {
			type.setCustomizationID(model.getCustomizationID());
		}
		if (model.getSignature() != null) {
			for (SignatureModel item : model.getSignature()) {
				type.addSignature(ModelToType.toType(item));
			}
		}
		if (model.getID() != null) {
			type.setId(model.getID());
		}
		if (model.getIssueDateTime() != null) {
			type.setIssueDate(ModelToType.toType(model.getIssueDateTime().toLocalDate()));
		}
		if (model.getNote() != null) {
			for (String value : model.getNote()) {
				type.addNote(value);
			}
		}

		if (model.getSUNATRetentionSystemCode() != null) {
			type.setSunatRetentionSystemCode(model.getSUNATRetentionSystemCode());
		}
		if (model.getSUNATRetentionPercent() != null) {
			type.setSunatRetentionPercent(model.getSUNATRetentionPercent());
		}
		if (model.getAgentParty() != null) {
			type.setAgentParty(ModelToType.toType(model.getAgentParty()));
		}
		if (model.getReceiverParty() != null) {
			type.setAgentParty(ModelToType.toType(model.getReceiverParty()));
		}
		if (model.getTotalInvoiceAmount() != null) {
			type.setTotalInvoiceAmount(model.getTotalInvoiceAmount(), model.getDocumentCurrencyCode());
		}
		if (model.getSUNATTotalPaid() != null) {
			type.setSunatTotalPaid(model.getSUNATTotalPaid(), model.getDocumentCurrencyCode());
		}
		for (RetentionDocumentReferenceModel item : model.getSUNATRetentionDocumentReference()) {
			type.addRetentionDocumentReference(toType(item, model.getDocumentCurrencyCode()));
		}
		type.setDocumentCurrencyCode(model.getDocumentCurrencyCode());
		return type;
	}

	public static SummaryDocumentsType toType(SummaryDocumentsModel model) throws DatatypeConfigurationException {
		SummaryDocumentsType type = new SummaryDocumentsType();
		if (model.getUblVersionID() != null) {
			type.setUblVersionID(model.getUblVersionID());
		}
		if (model.getCustomizationID() != null) {
			type.setCustomizationID(model.getCustomizationID());
		}
		if (model.getID() != null) {
			type.setId(model.getID());
		}
		if (model.getReferenceDateTime() != null) {
			type.setReferenceDateTime(ModelToType.toType(model.getReferenceDateTime().toLocalDate()));
		}
		if (model.getIssueDateTime() != null) {
			type.setIssueDate(ModelToType.toType(model.getIssueDateTime().toLocalDate()));
		}

		if (model.getSignature() != null) {
			for (SignatureModel item : model.getSignature()) {
				type.addSignature(ModelToType.toType(item));
			}
		}

		if (model.getAccountingSupplierParty() != null) {
			type.setAccountingSupplierParty(ModelToType.toType(model.getAccountingSupplierParty()));
		}
		if (model.getSummaryDocumentsLines() != null) {
			for (SummaryDocumentsLineModel item : model.getSummaryDocumentsLines()) {
				type.addSummaryDocumentsLine(toType(item, model.getDocumentCurrencyCode()));
			}
		}
		type.setDocumentCurrencyCode(model.getDocumentCurrencyCode());
		return type;
	}

	public static VoidedDocumentsType toType(VoidedDocumentsModel model) throws DatatypeConfigurationException {
		VoidedDocumentsType type = new VoidedDocumentsType();
		if (model.getID() != null) {
			type.setID(model.getID());
		}
		if (model.getUblVersionID() != null) {
			type.setUBLVersionID(model.getUblVersionID());
		}
		if (model.getCustomizationID() != null) {
			type.setCustomizationID(model.getCustomizationID());
		}
		if (model.getReferenceDate() != null) {
			type.setReferenceDate(ModelToType.toType(model.getReferenceDate().toLocalDate()));
		}
		if (model.getIssueDateTime() != null) {
			type.setIssueDate(ModelToType.toType(model.getIssueDateTime().toLocalDate()));
		}
		if (model.getNote() != null) {
			for (String value : model.getNote()) {
				type.addNote(value);
			}
		}
		if (model.getSignature() != null) {
			for (SignatureModel item : model.getSignature()) {
				type.addSignature(ModelToType.toType(item));
			}
		}
		if (model.getAccountingSupplierParty() != null) {
			type.setAccountingSupplierParty(ModelToType.toType(model.getAccountingSupplierParty()));
		}
		if (model.getVoidedDocumentsLine() != null) {
			for (VoidedDocumentsLineModel item : model.getVoidedDocumentsLine()) {
				type.addVoidedDocumentsLine(toType(item));
			}
		}
		type.setDocumentCurrencyCode(model.getDocumentCurrencyCode());
		return type;
	}

	private static VoidedDocumentsLineType toType(VoidedDocumentsLineModel model) {
		VoidedDocumentsLineType type = new VoidedDocumentsLineType();
		if (model.getLineID() != null) {
			type.setLineID(model.getLineID());
		}
		if (model.getDocumentTypeCode() != null) {
			type.setDocumentTypeCode(model.getDocumentTypeCode());
		}
		if (model.getDocumentSerialID() != null) {
			type.setDocumentSerialID(model.getDocumentSerialID());
		}
		if (model.getDocumentNumberID() != null) {
			type.setDocumentNumberID(model.getDocumentNumberID());
		}
		if (model.getVoidReasonDescription() != null) {
			type.setVoidReasonDescription(model.getVoidReasonDescription());
		}
		return type;
	}

	private static SummaryDocumentsLineType toType(SummaryDocumentsLineModel model, String currencyID)
			throws DatatypeConfigurationException {
		SummaryDocumentsLineType type = new SummaryDocumentsLineType();
		if (model.getID() != null) {
			type.setID(model.getID());
		}
		if (model.getLineID() != null) {
			type.setLineID(model.getLineID());
		}
		if (model.getDocumentTypeCode() != null) {
			type.setDocumentTypeCode(model.getDocumentTypeCode());
		}
		if (model.getDocumentSerialID() != null) {
			type.setDocumentSerialID(model.getDocumentSerialID());
		}
		if (model.getStartDocumentNumberID() != null) {
			type.setStartDocumentNumberID(model.getStartDocumentNumberID());
		}
		if (model.getEndDocumentNumberID() != null) {
			type.setEndDocumentNumberID(model.getEndDocumentNumberID());
		}
		if (model.getTotalAmount() != null) {
			type.setTotalAmount(model.getTotalAmount(), currencyID);
		}
		if (model.getBillingPayment() != null) {
			for (PaymentModel item : model.getBillingPayment()) {
				type.addPayment(toType(item));
			}
		}
		if (model.getAccountingCustomerParty() != null) {
			type.setAccountingCustomerParty(ModelToType.toType(model.getAccountingCustomerParty()));
		}
		if (model.getBillingReference() != null) {
			type.setBillingReference(ModelToType.toType(model.getBillingReference()));
		}
		if (model.getStatus() != null) {
			type.setStatus(toType(model.getStatus()));
		}
		if (model.getAllowanceCharge() != null) {
			for (AllowanceChargeModel item : model.getAllowanceCharge()) {
				type.addAllowanceCharge(ModelToType.toType(item));
			}
		}
		if (model.getTaxTotal() != null) {
			for (TaxTotalModel item : model.getTaxTotal()) {
				type.addTaxTotal(ModelToType.toType(item, currencyID));
			}
		}
		return type;
	}

	private static StatusType toType(StatusModel model) throws DatatypeConfigurationException {
		StatusType type = new StatusType();
		if (model.getConditionCode() != null) {
			type.setConditionCode(model.getConditionCode());
		}
		if (model.getReferenceDate() != null) {
			type.setReferenceDate(ModelToType.toType(model.getReferenceDate()));
		}
		if (model.getReferenceTime() != null) {
			type.setReferenceTime(ModelToType.toType(model.getReferenceTime()));
		}
		if (model.getDescription() != null) {
			type.addDescription(getDescriptionType(model.getDescription()));
		}
		if (model.getStatusReason() != null) {
			type.addStatusReason(getStatusReasonType(model.getStatusReason()));
		}
		if (model.getStatusReasonCode() != null) {
			type.setStatusReasonCode(model.getStatusReasonCode());
		}
		if (model.getSequenceID() != null) {
			type.setSequenceID(model.getSequenceID());
		}
		if (model.getText() != null) {
			type.addText(getTextType(model.getText()));
		}
		type.setIndicationIndicator(model.getIndicationIndicator());
		if (model.getPercent() != null) {
			type.setPercent(model.getPercent());
		}
		return type;
	}

	private static TextType getTextType(String text) {
		TextType type = new TextType();
		type.setValue(text);
		return type;
	}

	private static StatusReasonType getStatusReasonType(String statusReason) {
		StatusReasonType type = new StatusReasonType();
		type.setValue(statusReason);
		return type;
	}

	private static SUNATRetentionDocumentReferenceType toType(RetentionDocumentReferenceModel model, String currencyID)
			throws DatatypeConfigurationException {
		SUNATRetentionDocumentReferenceType type = new SUNATRetentionDocumentReferenceType();
		if (model.getID() != null) {
			type.setID(model.getID());
		}
		if (model.getIssueDateTime() != null) {
			type.setIssueDate(ModelToType.toType(model.getIssueDateTime().toLocalDate()));
		}
		if (model.getTotalInvoiceAmount() != null) {
			type.setTotalInvoiceAmount(model.getTotalInvoiceAmount(), currencyID);
		}
		if (model.getPayment() != null) {
			type.setPayment(toType(model.getPayment()));
		}
		if (model.getSunatRetentionInformation() != null) {
			type.setSUNATRetentionInformation(toType(model.getSunatRetentionInformation(), currencyID));
		}
		return type;
	}

	private static SUNATRetentionInformationType toType(RetentionInformationModel model, String currencyID)
			throws DatatypeConfigurationException {
		SUNATRetentionInformationType type = new SUNATRetentionInformationType();
		if (model.getSunatRetentionAmount() != null) {
			type.setSUNATRetentionAmount(model.getSunatRetentionAmount());
		}
		if (model.getSunatRetentionDate() != null) {
			type.setSUNATRetentionDate(ModelToType.toType(model.getSunatRetentionDate().toLocalDate()));
		}
		if (model.getSunatNetTotalPaid() != null) {
			type.setSUNATNetTotalPaid(model.getSunatNetTotalPaid(), currencyID);
		}
		if (model.getExchangeRate() != null) {
			type.setExchangeRate(toType(model.getExchangeRate()));
		}
		return type;
	}

	public static SUNATPerceptionDocumentReferenceType toType(PerceptionDocumentReferenceModel model, String currencyID)
			throws DatatypeConfigurationException {
		SUNATPerceptionDocumentReferenceType type = new SUNATPerceptionDocumentReferenceType();

		if (model.getID() != null) {
			type.setId(model.getID());
		}
		if (model.getIssueDateTime() != null) {
			type.setIssueDate(ModelToType.toType(model.getIssueDateTime().toLocalDate()));
		}
		if (model.getTotalInvoiceAmount() != null) {
			type.setTotalInvoiceAmount(model.getTotalInvoiceAmount(), currencyID);
		}
		if (model.getPayment() != null) {
			type.setPayment(toType(model.getPayment()));
		}
		if (model.getSunatPerceptionInformation() != null) {
			type.setSunatPerceptionInformation(toType(model.getSunatPerceptionInformation(), currencyID));
		}
		return type;
	}

	private static SUNATPerceptionInformationType toType(PerceptionInformationModel model, String currencyID)
			throws DatatypeConfigurationException {
		SUNATPerceptionInformationType type = new SUNATPerceptionInformationType();
		if (model.getSunatPerceptionAmount() != null) {
			type.setSunatPerceptionAmount(model.getSunatPerceptionAmount());
		}
		if (model.getSunatPerceptionDate() != null) {
			type.setSunatPerceptionDate(ModelToType.toType(model.getSunatPerceptionDate().toLocalDate()));
		}
		if (model.getSunatNetTotalPaid() != null) {
			type.setSunatNetTotalCashed(model.getSunatNetTotalPaid(), currencyID);
		}
		if (model.getExchangeRate() != null) {
			type.setExchangeRate(toType(model.getExchangeRate()));
		}
		return type;
	}

	public static ExchangeRateType toType(ExchangeRateModel model) throws DatatypeConfigurationException {
		ExchangeRateType type = new ExchangeRateType();
		if (model.getSourceCurrencyCode() != null) {
			type.setSourceCurrencyCode(model.getSourceCurrencyCode());
		}
		if (model.getSourceCurrencyBaseRate() != null) {
			type.setSourceCurrencyBaseRate(model.getSourceCurrencyBaseRate());
		}
		if (model.getTargetCurrencyCode() != null) {
			type.setTargetCurrencyCode(model.getTargetCurrencyCode());
		}
		if (model.getTargetCurrencyBaseRate() != null) {
			type.setTargetCurrencyBaseRate(model.getTargetCurrencyBaseRate());
		}
		if (model.getExchangeMarketID() != null) {
			type.setExchangeMarketID(model.getExchangeMarketID());
		}
		if (model.getCalculationRate() != null) {
			type.setCalculationRate(model.getCalculationRate());
		}
		if (model.getMathematicOperatorCode() != null) {
			type.setMathematicOperatorCode(model.getMathematicOperatorCode());
		}
		if (model.getDate() != null) {
			type.setDate(ModelToType.toType(model.getDate()));
		}
		if (model.getForeignExchangeContract() != null) {
			type.setForeignExchangeContract(toType(model.getForeignExchangeContract()));
		}
		return type;
	}

	public static ContractType toType(ContractModel model) throws DatatypeConfigurationException {
		ContractType type = new ContractType();
		if (model.getID() != null) {
			type.setID(model.getID());
		}
		if (model.getIssueDate() != null) {
			type.setIssueDate(ModelToType.toType(model.getIssueDate()));
		}
		if (model.getIssueTime() != null) {
			type.setIssueTime(ModelToType.toType(model.getIssueTime()));
		}
		if (model.getContractTypeCode() != null) {
			type.setContractTypeCode(model.getContractTypeCode());
		}
		if (model.getContractType() != null) {
			type.setContractType(model.getContractType());
		}
		if (model.getValidityPeriod() != null) {
			type.setValidityPeriod(toType(model.getValidityPeriod()));
		}
		if (model.getContractDocumentReference() != null) {
			for (DocumentReferenceModel item : model.getContractDocumentReference()) {
				type.addContractDocumentReference(ModelToType.toType(item));
			}
		}
		return type;
	}

	private static PeriodType toType(PeriodModel model) throws DatatypeConfigurationException {
		PeriodType type = new PeriodType();
		if (model.getStartDate() != null) {
			type.setStartDate(ModelToType.toType(model.getStartDate()));
		}
		if (model.getStartTime() != null) {
			type.setStartTime(ModelToType.toType(model.getStartTime()));
		}
		if (model.getEndDate() != null) {
			type.setEndDate(ModelToType.toType(model.getEndDate()));
		}
		if (model.getEndTime() != null) {
			type.setEndTime(ModelToType.toType(model.getEndTime()));
		}
		if (model.getDurationMeasure() != null) {
			type.setDurationMeasure(toType(model.getDurationMeasure()));
		}
		if (model.getDescriptionCode() != null) {
			for (String elem : model.getDescriptionCode()) {
				type.addDescriptionCode(getDescriptionCodeType(elem));
			}
		}
		if (model.getDescription() != null) {
			for (String elem : model.getDescription()) {
				type.addDescription(getDescriptionType(elem));
			}
		}
		return type;
	}

	private static DescriptionCodeType getDescriptionCodeType(String elem) {
		DescriptionCodeType type = new DescriptionCodeType();
		type.setValue(elem);
		return type;
	}

	private static DescriptionType getDescriptionType(String elem) {
		DescriptionType type = new DescriptionType();
		type.setValue(elem);
		return type;
	}

	private static DurationMeasureType toType(MeasureModel model) {
		DurationMeasureType type = new DurationMeasureType();
		if (model.getValue() != null) {
			type.setValue(model.getValue());
		}
		if (model.getUnitCode() != null) {
			type.setUnitCode(model.getUnitCode());
		}
		return type;
	}

	private static PaymentType toType(PaymentModel model) throws DatatypeConfigurationException {
		PaymentType type = new PaymentType();
		if (model.getID() != null) {
			type.setID(model.getID());
		}
		if (model.getPaidAmount() != null) {
			type.setPaidAmount(model.getPaidAmount());
		}
		if (model.getReceivedDate() != null) {
			type.setReceivedDate(ModelToType.toType(model.getReceivedDate()));
		}
		if (model.getPaidDate() != null) {
			type.setPaidDate(ModelToType.toType(model.getPaidDate()));
		}
		if (model.getPaidTime() != null) {
			type.setPaidTime(ModelToType.toType(model.getPaidTime()));
		}
		if (model.getInstructionID() != null) {
			type.setInstructionID(model.getInstructionID());
		}
		return type;
	}

}
