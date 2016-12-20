package org.openfact.pe.models.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.dom.DOMResult;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.*;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.*;
import org.openfact.common.finance.MoneyConverters;
import org.openfact.models.InvoiceModel;
import org.openfact.models.ModelException;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.constants.*;
import org.openfact.pe.model.types.PerceptionType;
import org.openfact.pe.model.types.RetentionType;
import org.openfact.pe.model.types.SUNATPerceptionDocumentReferenceType;
import org.openfact.pe.model.types.SUNATPerceptionInformationType;
import org.openfact.pe.model.types.SUNATRetentionDocumentReferenceType;
import org.openfact.pe.model.types.SUNATRetentionInformationType;
import org.openfact.pe.model.types.SummaryDocumentsLineType;
import org.openfact.pe.model.types.SummaryDocumentsType;
import org.openfact.pe.model.types.VoidedDocumentsLineType;
import org.openfact.pe.model.types.VoidedDocumentsType;
import org.openfact.pe.models.types.common.AdditionalInformationTypeSunatAgg;
import org.openfact.pe.models.types.common.AdditionalMonetaryTotalType;
import org.openfact.pe.models.types.common.AdditionalPropertyType;
import org.openfact.pe.models.types.common.InvoiceFactory;
import org.openfact.pe.representations.idm.BillingPaymentRepresentation;
import org.openfact.pe.representations.idm.DocumentReferenceRepresentation;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.representations.idm.LineRepresentation;
import org.openfact.pe.representations.idm.RetentionRepresentation;
import org.openfact.pe.representations.idm.SummaryLineRepresentation;
import org.openfact.pe.representations.idm.SummaryRepresentation;
import org.openfact.pe.representations.idm.TaxTotalRepresentation;
import org.openfact.pe.representations.idm.VoidedLineRepresentation;
import org.openfact.pe.representations.idm.VoidedRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.UBLExtensionsType;
import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public class SunatRepresentationToType {

	public static final String UBL_VERSION_ID = "2.0";
	public static final String CUSTOMIZATION_ID = "1.0";

	public static final String QUANTITY_UNKNOW = "NIU";

	public static XMLGregorianCalendar toGregorianCalendar(LocalDate date) {
		GregorianCalendar gcal = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		} catch (DatatypeConfigurationException e) {
			throw new ModelException(e);
		}
	}

	public static XMLGregorianCalendar toGregorianCalendarTime(LocalDateTime date) {
		GregorianCalendar gcal = GregorianCalendar.from(date.atZone(ZoneId.systemDefault()));
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		} catch (DatatypeConfigurationException e) {
			throw new ModelException(e);
		}
	}

	public static InvoiceType toInvoiceType(OrganizationModel organization, DocumentRepresentation rep) {
		InvoiceType type = new InvoiceType();

		// General config
		type.setUBLVersionID(SunatRepresentationToType.UBL_VERSION_ID);
		type.setCustomizationID(SunatRepresentationToType.CUSTOMIZATION_ID);

		// ID
		if (rep.getNumero() != null && rep.getSerie() != null) {
			type.setID(rep.getNumero() + "-" + rep.getSerie());
		}

		// Issue Date
		if (rep.getFechaDeEmision() != null) {
			type.setIssueDate(toGregorianCalendar(rep.getFechaDeEmision().toLocalDate()));
			type.setIssueTime(toGregorianCalendarTime(rep.getFechaDeEmision()));
		} else {
			type.setIssueDate(toGregorianCalendar(LocalDate.now()));
			type.setIssueTime(toGregorianCalendarTime(LocalDateTime.now()));
		}
		if (rep.getFechaDeVencimiento() != null) {
			type.setDueDate(toGregorianCalendar(rep.getFechaDeVencimiento().toLocalDate()));
		}

		// Currency
		if (rep.getMoneda() != null) {
			type.setDocumentCurrencyCode(rep.getMoneda());
		}

		// Supplier
		type.setAccountingSupplierParty(toSupplierParty(organization));

		// Customer
		type.setAccountingCustomerParty(toCustomerPartyType(rep));

		// Tax Total
		type.setTaxTotal(Arrays.asList(toTaxTotalIGV(rep)));

		// Legal monetary total
		type.setLegalMonetaryTotal(toLegalMonetaryTotalType(rep));

		// Invoice type code
		if (rep.getTipo() != null) {
			type.setInvoiceTypeCode(rep.getTipo());
		}

		if (rep.getTipoDeCambio() != null) {
		}

		// Notes type
		if (rep.getObservaciones() != null) {
			List<NoteType> noteTypes = new ArrayList<>();
			noteTypes.add(new NoteType(rep.getObservaciones()));
			type.setNote(noteTypes);
		}

		// Signature
		type.setSignature(Arrays.asList(toSignatureType(organization)));

		// Lines
		if (rep.getDetalle() != null) {
			type.setInvoiceLine(toInvoiceLineType(rep));
		}

		// Extensions
		UBLExtensionsType ublExtensionsType = new UBLExtensionsType();
		UBLExtensionType ublExtensionType = new UBLExtensionType();
		ExtensionContentType extensionContentType = new ExtensionContentType();

		AdditionalMonetaryTotalType gravado = new AdditionalMonetaryTotalType();
		if (rep.getTotalGravada() != null) {
			gravado.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRAVADAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalGravada());
			payableAmountType.setCurrencyID(rep.getMoneda());
			gravado.setPayableAmount(payableAmountType);
		}
		AdditionalMonetaryTotalType inafecto = new AdditionalMonetaryTotalType();
		if (rep.getTotalInafecta() != null) {
			inafecto.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_INAFECTAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalInafecta());
			payableAmountType.setCurrencyID(rep.getMoneda());
			inafecto.setPayableAmount(payableAmountType);
		}
		AdditionalMonetaryTotalType exonerado = new AdditionalMonetaryTotalType();
		if (rep.getTotalExonerada() != null) {
			exonerado.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_EXONERADAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalExonerada());
			payableAmountType.setCurrencyID(rep.getMoneda());
			exonerado.setPayableAmount(payableAmountType);
		}
		AdditionalMonetaryTotalType gratuito = new AdditionalMonetaryTotalType();
		if (rep.getTotalGratuita() != null) {
			gratuito.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRATUITAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalGratuita());
			payableAmountType.setCurrencyID(rep.getMoneda());
			gratuito.setPayableAmount(payableAmountType);
		}
		AdditionalPropertyType additionalProperty = generateAdditionalInformationSunatTotal(rep.getTotal());

		AdditionalInformationTypeSunatAgg additionalInformation = new AdditionalInformationTypeSunatAgg();
		additionalInformation.getAdditionalMonetaryTotal().addAll(Arrays.asList(gravado, inafecto, exonerado, gratuito));
		additionalInformation.getAdditionalProperty().add(additionalProperty);

		extensionContentType.setAny(generateElement(additionalInformation));
		ublExtensionType.setExtensionContent(extensionContentType);
		ublExtensionsType.setUBLExtension(new ArrayList<>(Arrays.asList(ublExtensionType)));
		type.setUBLExtensions(ublExtensionsType);

		return type;
	}

	private static List<InvoiceLineType> toInvoiceLineType(DocumentRepresentation rep) {
		List<InvoiceLineType> invoiceLineTypes = new ArrayList<>();
		for (int i = 0; i < rep.getDetalle().size(); i++) {
			LineRepresentation lineRep = rep.getDetalle().get(i);
			InvoiceLineType invoiceLineType = new InvoiceLineType();

			// ID
			invoiceLineType.setID(new IDType(String.valueOf(i + 1)));

			// Quantity
			InvoicedQuantityType quantityType = new InvoicedQuantityType(lineRep.getCantidad());
			if (lineRep.getUnitCode() != null) {
				quantityType.setUnitCode(lineRep.getUnitCode());
			} else {
				quantityType.setUnitCode(SunatRepresentationToType.QUANTITY_UNKNOW);
			}
			invoiceLineType.setInvoicedQuantity(quantityType);

			// Line extension amount
			LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType(lineRep.getSubtotal());
			lineExtensionAmountType.setCurrencyID(rep.getMoneda());
			invoiceLineType.setLineExtensionAmount(lineExtensionAmountType);

			// Pricing reference
			PricingReferenceType pricingReferenceType = new PricingReferenceType();
			PriceType priceType = new PriceType();

			PriceAmountType priceAmountType = new PriceAmountType(lineRep.getPrecioUnitario());
			priceAmountType.setCurrencyID(rep.getMoneda());
			priceType.setPriceAmount(priceAmountType);
			if(!rep.isOperacionGratuita()) {
				priceType.setPriceTypeCode("01");
			} else {
				priceType.setPriceTypeCode("02");
			}
			pricingReferenceType.setAlternativeConditionPrice(Arrays.asList(priceType));
			invoiceLineType.setPricingReference(pricingReferenceType);

			// Item
			ItemType itemType = new ItemType();
			itemType.setDescription(Arrays.asList(new DescriptionType(lineRep.getDescripcion())));
			invoiceLineType.setItem(itemType);

			// Tax Total
			TaxTotalType taxTotalType = new TaxTotalType();
			TaxAmountType taxAmountType1 = new TaxAmountType(lineRep.getTotal());
			taxAmountType1.setCurrencyID(rep.getMoneda());
			taxTotalType.setTaxAmount(taxAmountType1);

			TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
			TaxAmountType taxAmountType2 = new TaxAmountType(lineRep.getTotal());
			taxAmountType2.setCurrencyID(rep.getMoneda());
			taxSubtotalType.setTaxAmount(taxAmountType2);

			TaxCategoryType taxCategoryType = new TaxCategoryType();
			taxCategoryType.setTaxExemptionReasonCode(lineRep.getTipoDeIgv());
			TaxSchemeType taxSchemeType = new TaxSchemeType();
			taxSchemeType.setID(CodigoTipoTributo.IGV.getId());
			taxSchemeType.setName(CodigoTipoTributo.IGV.toString());
			taxSchemeType.setTaxTypeCode(CodigoTipoTributo.IGV.getCodigo());
			taxCategoryType.setTaxScheme(taxSchemeType);
			taxSubtotalType.setTaxCategory(taxCategoryType);

			taxTotalType.setTaxSubtotal(Arrays.asList(taxSubtotalType));
			invoiceLineType.setTaxTotal(Arrays.asList(taxTotalType));

			taxTotalType.setTaxSubtotal(Arrays.asList(taxSubtotalType));
			invoiceLineType.setTaxTotal(Arrays.asList(taxTotalType));

			// Price
			PriceType priceType1 = new PriceType();
			PriceAmountType priceAmountType1 = new PriceAmountType(lineRep.getValorUnitario());
			priceAmountType1.setCurrencyID(rep.getMoneda());
			priceType1.setPriceAmount(priceAmountType1);
			invoiceLineType.setPrice(priceType1);

			invoiceLineTypes.add(invoiceLineType);
		}
		return invoiceLineTypes;
	}

	public static CreditNoteType toCreditNoteType(OrganizationModel organization, DocumentRepresentation rep) {
		CreditNoteType type = new CreditNoteType();

		// General config
		type.setUBLVersionID(SunatRepresentationToType.UBL_VERSION_ID);
		type.setCustomizationID(SunatRepresentationToType.CUSTOMIZATION_ID);

		// ID
		if (rep.getNumero() != null && rep.getSerie() != null) {
			type.setID(rep.getNumero() + "-" + rep.getSerie());
		}

		// Issue Date
		if (rep.getFechaDeEmision() != null) {
			type.setIssueDate(toGregorianCalendar(rep.getFechaDeEmision().toLocalDate()));
			type.setIssueTime(toGregorianCalendarTime(rep.getFechaDeEmision()));
		} else {
			type.setIssueDate(toGregorianCalendar(LocalDate.now()));
			type.setIssueTime(toGregorianCalendarTime(LocalDateTime.now()));
		}

		// Currency
		if (rep.getMoneda() != null) {
			type.setDocumentCurrencyCode(rep.getMoneda());
		}

		// Supplier
		type.setAccountingSupplierParty(toSupplierParty(organization));

		// Customer
		type.setAccountingCustomerParty(toCustomerPartyType(rep));

		// Tax Total
		type.setTaxTotal(Arrays.asList(toTaxTotalIGV(rep)));

		// Legal monetary total
		type.setLegalMonetaryTotal(toLegalMonetaryTotalType(rep));

		// Notes type
		if (rep.getObservaciones() != null) {
			List<NoteType> noteTypes = new ArrayList<>();
			noteTypes.add(new NoteType(rep.getObservaciones()));
			type.setNote(noteTypes);
		}

		// Discrepancy response
		ResponseType responseType = new ResponseType();
		responseType.setReferenceID(rep.getDocumentoQueSeModifica());
		responseType.setResponseCode(rep.getTipo());
		responseType.setDescription(Arrays.asList(new DescriptionType(rep.getObservaciones())));

		type.setDiscrepancyResponse(Arrays.asList());

		// Billing reference
		BillingReferenceType billingReferenceType = new BillingReferenceType();

		DocumentReferenceType documentReferenceType = new DocumentReferenceType();
		documentReferenceType.setID(rep.getTipo());

		billingReferenceType.setInvoiceDocumentReference(documentReferenceType);

		type.setBillingReference(Arrays.asList(billingReferenceType));

		// Signature
		type.setSignature(Arrays.asList(toSignatureType(organization)));

		// Lines
		if (rep.getDetalle() != null) {
			type.setCreditNoteLine(toCreditNoteLineType(rep));
		}

		// Extensions
		UBLExtensionsType ublExtensionsType = new UBLExtensionsType();
		UBLExtensionType ublExtensionType = new UBLExtensionType();
		ExtensionContentType extensionContentType = new ExtensionContentType();

		AdditionalMonetaryTotalType gravado = new AdditionalMonetaryTotalType();
		if (rep.getTotalGravada() != null) {
			gravado.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRAVADAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalGravada());
			payableAmountType.setCurrencyID(rep.getMoneda());
			gravado.setPayableAmount(payableAmountType);
		}
		AdditionalMonetaryTotalType inafecto = new AdditionalMonetaryTotalType();
		if (rep.getTotalInafecta() != null) {
			inafecto.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_INAFECTAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalInafecta());
			payableAmountType.setCurrencyID(rep.getMoneda());
			inafecto.setPayableAmount(payableAmountType);
		}
		AdditionalMonetaryTotalType exonerado = new AdditionalMonetaryTotalType();
		if (rep.getTotalExonerada() != null) {
			exonerado.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_EXONERADAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalExonerada());
			payableAmountType.setCurrencyID(rep.getMoneda());
			exonerado.setPayableAmount(payableAmountType);
		}
		AdditionalMonetaryTotalType gratuito = new AdditionalMonetaryTotalType();
		if (rep.getTotalGratuita() != null) {
			gratuito.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRATUITAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalGratuita());
			payableAmountType.setCurrencyID(rep.getMoneda());
			gratuito.setPayableAmount(payableAmountType);
		}
		AdditionalPropertyType additionalProperty = generateAdditionalInformationSunatTotal(rep.getTotal());

		AdditionalInformationTypeSunatAgg additionalInformation = new AdditionalInformationTypeSunatAgg();
		additionalInformation.getAdditionalMonetaryTotal().addAll(Arrays.asList(gravado, inafecto, exonerado, gratuito));
		additionalInformation.getAdditionalProperty().add(additionalProperty);

		extensionContentType.setAny(generateElement(additionalInformation));
		ublExtensionType.setExtensionContent(extensionContentType);
		ublExtensionsType.setUBLExtension(new ArrayList<>(Arrays.asList(ublExtensionType)));
		type.setUBLExtensions(ublExtensionsType);

		return type;
	}

	private static List<CreditNoteLineType> toCreditNoteLineType(DocumentRepresentation rep) {
		List<CreditNoteLineType> creditNoteLineTypes = new ArrayList<>();
		for (int i = 0; i < rep.getDetalle().size(); i++) {
			LineRepresentation lineRep = rep.getDetalle().get(i);
			CreditNoteLineType creditNoteLineType = new CreditNoteLineType();

			// ID
			creditNoteLineType.setID(new IDType(String.valueOf(i + 1)));

			// Quantity
			CreditedQuantityType quantityType = new CreditedQuantityType(lineRep.getCantidad());
			if (lineRep.getUnitCode() != null) {
				quantityType.setUnitCode(lineRep.getUnitCode());
			} else {
				quantityType.setUnitCode(SunatRepresentationToType.QUANTITY_UNKNOW);
			}
			creditNoteLineType.setCreditedQuantity(quantityType);

			// Line extension amount
			LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType(lineRep.getSubtotal());
			lineExtensionAmountType.setCurrencyID(rep.getMoneda());
			creditNoteLineType.setLineExtensionAmount(lineExtensionAmountType);

			// Pricing reference
			PricingReferenceType pricingReferenceType = new PricingReferenceType();
			PriceType priceType = new PriceType();

			PriceAmountType priceAmountType = new PriceAmountType(lineRep.getPrecioUnitario());
			priceAmountType.setCurrencyID(rep.getMoneda());
			priceType.setPriceAmount(priceAmountType);
			if(!rep.isOperacionGratuita()) {
				priceType.setPriceTypeCode("01");
			} else {
				priceType.setPriceTypeCode("02");
			}
			pricingReferenceType.setAlternativeConditionPrice(Arrays.asList(priceType));
			creditNoteLineType.setPricingReference(pricingReferenceType);

			// Item
			ItemType itemType = new ItemType();
			itemType.setDescription(Arrays.asList(new DescriptionType(lineRep.getDescripcion())));
			creditNoteLineType.setItem(itemType);

			// Tax Total
			TaxTotalType taxTotalType = new TaxTotalType();
			TaxAmountType taxAmountType1 = new TaxAmountType(lineRep.getTotal());
			taxAmountType1.setCurrencyID(rep.getMoneda());
			taxTotalType.setTaxAmount(taxAmountType1);

			TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
			TaxAmountType taxAmountType2 = new TaxAmountType(lineRep.getTotal());
			taxAmountType2.setCurrencyID(rep.getMoneda());
			taxSubtotalType.setTaxAmount(taxAmountType2);

			TaxCategoryType taxCategoryType = new TaxCategoryType();
			taxCategoryType.setTaxExemptionReasonCode(lineRep.getTipoDeIgv());
			TaxSchemeType taxSchemeType = new TaxSchemeType();
			taxSchemeType.setID(CodigoTipoTributo.IGV.getId());
			taxSchemeType.setName(CodigoTipoTributo.IGV.toString());
			taxSchemeType.setTaxTypeCode(CodigoTipoTributo.IGV.getCodigo());
			taxCategoryType.setTaxScheme(taxSchemeType);
			taxSubtotalType.setTaxCategory(taxCategoryType);

			taxTotalType.setTaxSubtotal(Arrays.asList(taxSubtotalType));
			creditNoteLineType.setTaxTotal(Arrays.asList(taxTotalType));

			// Price
			PriceType priceType1 = new PriceType();
			PriceAmountType priceAmountType1 = new PriceAmountType(lineRep.getValorUnitario());
			priceAmountType1.setCurrencyID(rep.getMoneda());
			priceType1.setPriceAmount(priceAmountType1);
			creditNoteLineType.setPrice(priceType1);

			creditNoteLineTypes.add(creditNoteLineType);
		}
		return creditNoteLineTypes;
	}

	public static DebitNoteType toDebitNoteType(OrganizationModel organization, DocumentRepresentation rep) {
		DebitNoteType type = new DebitNoteType();

		// General config
		type.setUBLVersionID(SunatRepresentationToType.UBL_VERSION_ID);
		type.setCustomizationID(SunatRepresentationToType.CUSTOMIZATION_ID);

		// ID
		if (rep.getNumero() != null && rep.getSerie() != null) {
			type.setID(rep.getNumero() + "-" + rep.getSerie());
		}

		// Issue Date
		if (rep.getFechaDeEmision() != null) {
			type.setIssueDate(toGregorianCalendar(rep.getFechaDeEmision().toLocalDate()));
			type.setIssueTime(toGregorianCalendarTime(rep.getFechaDeEmision()));
		} else {
			type.setIssueDate(toGregorianCalendar(LocalDate.now()));
			type.setIssueTime(toGregorianCalendarTime(LocalDateTime.now()));
		}

		// Currency
		if (rep.getMoneda() != null) {
			type.setDocumentCurrencyCode(rep.getMoneda());
		}

		// Supplier
		type.setAccountingSupplierParty(toSupplierParty(organization));

		// Customer
		type.setAccountingCustomerParty(toCustomerPartyType(rep));

		// Tax Total
		type.setTaxTotal(Arrays.asList(toTaxTotalIGV(rep)));

		// Notes type
		if (rep.getObservaciones() != null) {
			List<NoteType> noteTypes = new ArrayList<>();
			noteTypes.add(new NoteType(rep.getObservaciones()));
			type.setNote(noteTypes);
		}

		// Discrepancy response
		ResponseType responseType = new ResponseType();
		responseType.setReferenceID(rep.getDocumentoQueSeModifica());
		responseType.setResponseCode(rep.getTipo());
		responseType.setDescription(Arrays.asList(new DescriptionType(rep.getObservaciones())));

		type.setDiscrepancyResponse(Arrays.asList());

		// Billing reference
		BillingReferenceType billingReferenceType = new BillingReferenceType();

		DocumentReferenceType documentReferenceType = new DocumentReferenceType();
		documentReferenceType.setID(rep.getTipo());

		billingReferenceType.setInvoiceDocumentReference(documentReferenceType);

		type.setBillingReference(Arrays.asList(billingReferenceType));

		// Requested monetary total
		type.setRequestedMonetaryTotal(toLegalMonetaryTotalType(rep));

		// Signature
		type.setSignature(Arrays.asList(toSignatureType(organization)));

		// Lines
		if (rep.getDetalle() != null) {
			type.setDebitNoteLine(toDebitNoteLineType(rep));
		}

		// Extensions
		UBLExtensionsType ublExtensionsType = new UBLExtensionsType();
		UBLExtensionType ublExtensionType = new UBLExtensionType();
		ExtensionContentType extensionContentType = new ExtensionContentType();

		AdditionalMonetaryTotalType gravado = new AdditionalMonetaryTotalType();
		if (rep.getTotalGravada() != null) {
			gravado.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRAVADAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalGravada());
			payableAmountType.setCurrencyID(rep.getMoneda());
			gravado.setPayableAmount(payableAmountType);
		}
		AdditionalMonetaryTotalType inafecto = new AdditionalMonetaryTotalType();
		if (rep.getTotalInafecta() != null) {
			inafecto.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_INAFECTAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalInafecta());
			payableAmountType.setCurrencyID(rep.getMoneda());
			inafecto.setPayableAmount(payableAmountType);
		}
		AdditionalMonetaryTotalType exonerado = new AdditionalMonetaryTotalType();
		if (rep.getTotalExonerada() != null) {
			exonerado.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_EXONERADAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalExonerada());
			payableAmountType.setCurrencyID(rep.getMoneda());
			exonerado.setPayableAmount(payableAmountType);
		}
		AdditionalMonetaryTotalType gratuito = new AdditionalMonetaryTotalType();
		if (rep.getTotalGratuita() != null) {
			gratuito.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRATUITAS.getCodigo()));

			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalGratuita());
			payableAmountType.setCurrencyID(rep.getMoneda());
			gratuito.setPayableAmount(payableAmountType);
		}
		AdditionalPropertyType additionalProperty = generateAdditionalInformationSunatTotal(rep.getTotal());

		AdditionalInformationTypeSunatAgg additionalInformation = new AdditionalInformationTypeSunatAgg();
		additionalInformation.getAdditionalMonetaryTotal().addAll(Arrays.asList(gravado, inafecto, exonerado, gratuito));
		additionalInformation.getAdditionalProperty().add(additionalProperty);

		extensionContentType.setAny(generateElement(additionalInformation));
		ublExtensionType.setExtensionContent(extensionContentType);
		ublExtensionsType.setUBLExtension(new ArrayList<>(Arrays.asList(ublExtensionType)));
		type.setUBLExtensions(ublExtensionsType);

		return type;
	}

	private static List<DebitNoteLineType> toDebitNoteLineType(DocumentRepresentation rep) {
		List<DebitNoteLineType> debitNoteLineTypes = new ArrayList<>();
		for (int i = 0; i < rep.getDetalle().size(); i++) {
			LineRepresentation lineRep = rep.getDetalle().get(i);
			DebitNoteLineType debitNoteLineType = new DebitNoteLineType();

			// ID
			debitNoteLineType.setID(new IDType(String.valueOf(i + 1)));

			// Quantity
			DebitedQuantityType quantityType = new DebitedQuantityType(lineRep.getCantidad());
			if (lineRep.getUnitCode() != null) {
				quantityType.setUnitCode(lineRep.getUnitCode());
			} else {
				quantityType.setUnitCode(SunatRepresentationToType.QUANTITY_UNKNOW);
			}
			debitNoteLineType.setDebitedQuantity(quantityType);

			// Line extension amount
			LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType(lineRep.getSubtotal());
			lineExtensionAmountType.setCurrencyID(rep.getMoneda());
			debitNoteLineType.setLineExtensionAmount(lineExtensionAmountType);

			// Pricing reference
			PricingReferenceType pricingReferenceType = new PricingReferenceType();
			PriceType priceType = new PriceType();

			PriceAmountType priceAmountType = new PriceAmountType(lineRep.getPrecioUnitario());
			priceAmountType.setCurrencyID(rep.getMoneda());
			priceType.setPriceAmount(priceAmountType);
			if(!rep.isOperacionGratuita()) {
				priceType.setPriceTypeCode("01");
			} else {
				priceType.setPriceTypeCode("02");
			}
			pricingReferenceType.setAlternativeConditionPrice(Arrays.asList(priceType));
			debitNoteLineType.setPricingReference(pricingReferenceType);

			// Item
			ItemType itemType = new ItemType();
			itemType.setDescription(Arrays.asList(new DescriptionType(lineRep.getDescripcion())));
			debitNoteLineType.setItem(itemType);

			// Tax Total
			TaxTotalType taxTotalType = new TaxTotalType();
			TaxAmountType taxAmountType1 = new TaxAmountType(lineRep.getTotal());
			taxAmountType1.setCurrencyID(rep.getMoneda());
			taxTotalType.setTaxAmount(taxAmountType1);

			TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
			TaxAmountType taxAmountType2 = new TaxAmountType(lineRep.getTotal());
			taxAmountType2.setCurrencyID(rep.getMoneda());
			taxSubtotalType.setTaxAmount(taxAmountType2);

			TaxCategoryType taxCategoryType = new TaxCategoryType();
			taxCategoryType.setTaxExemptionReasonCode(lineRep.getTipoDeIgv());
			TaxSchemeType taxSchemeType = new TaxSchemeType();
			taxSchemeType.setID(CodigoTipoTributo.IGV.getId());
			taxSchemeType.setName(CodigoTipoTributo.IGV.toString());
			taxSchemeType.setTaxTypeCode(CodigoTipoTributo.IGV.getCodigo());
			taxCategoryType.setTaxScheme(taxSchemeType);
			taxSubtotalType.setTaxCategory(taxCategoryType);

			taxTotalType.setTaxSubtotal(Arrays.asList(taxSubtotalType));
			debitNoteLineType.setTaxTotal(Arrays.asList(taxTotalType));

			// Price
			PriceType priceType1 = new PriceType();
			PriceAmountType priceAmountType1 = new PriceAmountType(lineRep.getValorUnitario());
			priceAmountType1.setCurrencyID(rep.getMoneda());
			priceType1.setPriceAmount(priceAmountType1);
			debitNoteLineType.setPrice(priceType1);

			debitNoteLineTypes.add(debitNoteLineType);
		}
		return debitNoteLineTypes;
	}

	public static PerceptionType toPerceptionType(OrganizationModel organization, RetentionRepresentation rep) {
		PerceptionType type = new PerceptionType();
		type.setUblVersionID(UblSunatConfiguration.VERSION_ID.getCodigo());
		type.setCustomizationID(UblSunatConfiguration.CUSTOMIZATION_ID.getCodigo());
		type.addSignature(toSignatureType(organization));
		type.setId(rep.getSerie() + UblSunatConfiguration.ID_SEPARATOR.getCodigo() + rep.getNumero());
		// Date
		if (rep.getFechaDeEmision() != null) {
			type.setIssueDate(toGregorianCalendar(rep.getFechaDeEmision().toLocalDate()));
		}
		type.setAgentParty(toAgentPartyType(organization));
		type.setReceiverParty(toReceiverPartyType(rep));
		if (rep.getCodigoDocumento() != null) {
			type.setSunatPerceptionSystemCode(rep.getCodigoDocumento());
		}
		if (rep.getTasaDocumento() != null) {
			type.setSunatPerceptionPercent(rep.getTasaDocumento());
		}
		BigDecimal totalInvoiceAmount = new BigDecimal(0);
		BigDecimal totalPaid = new BigDecimal(0);
		for (DocumentReferenceRepresentation reference : rep.getReference()) {
			BigDecimal amount = new BigDecimal(0);
			BigDecimal paid = new BigDecimal(0);
			if (rep.getMoneda() != null && reference.getMonedaPago() != null
					&& reference.getMonedaPago() != rep.getMoneda()) {
				if (reference.getTipoDeCambio() != null && reference.getTipoDeCambio() != new BigDecimal(0)) {
					amount = reference.getTotalPago().multiply(reference.getTipoDeCambio())
							.multiply(rep.getTasaDocumento());
					paid = reference.getTotalPago().multiply(reference.getTipoDeCambio()).subtract(amount);
				}
			} else {
				amount = reference.getTotalPago().multiply(rep.getTasaDocumento());
				paid = reference.getTotalPago().subtract(amount);
			}
			totalInvoiceAmount.add(amount);
			totalPaid.add(paid);
		}
		type.setTotalInvoiceAmount(totalInvoiceAmount, rep.getMoneda());
		type.setSunatTotalCashed(totalPaid, rep.getMoneda());
		for (DocumentReferenceRepresentation reference : rep.getReference()) {
			type.addPerceptionDocumentReference(
					toPerceptionDocumentReferenceType(reference, rep.getMoneda(), rep.getTasaDocumento()));
		}
		return type;
	}

	private static SUNATPerceptionDocumentReferenceType toPerceptionDocumentReferenceType(
			DocumentReferenceRepresentation rep, String currencyCode, BigDecimal perception) {
		SUNATPerceptionDocumentReferenceType type = new SUNATPerceptionDocumentReferenceType();
		type.setId(toIDType(rep));
		if (rep.getFechaDeDocumentoRelacionado() != null) {
			type.setIssueDate(toGregorianCalendar(rep.getFechaDeDocumentoRelacionado().toLocalDate()));
		}
		if (rep.getTotalDocumentoRelacionado() != null && rep.getMonedaDocumentRelacionado() != null) {
			type.setTotalInvoiceAmount(rep.getTotalDocumentoRelacionado(), rep.getMonedaDocumentRelacionado());
		}
		type.setPayment(toPaymentType(rep));
		type.setSunatPerceptionInformation(toSUNATPerceptionInformationType(rep, currencyCode, perception));
		return type;
	}

	private static SUNATPerceptionInformationType toSUNATPerceptionInformationType(DocumentReferenceRepresentation rep,
			String currencyCode, BigDecimal perception) {
		SUNATPerceptionInformationType type = new SUNATPerceptionInformationType();
		type.setSunatPerceptionAmount(toSUNATRetentionAmountType(rep, currencyCode, perception));
		if (rep.getFechaPago() != null) {
			type.setSunatPerceptionDate(toGregorianCalendar(rep.getFechaPago().toLocalDate()));
		}
		type.setSunatNetTotalCashed(tosetSUNATNetTotalPaidType(rep, currencyCode, perception));
		type.setExchangeRate(toExchangeRateType(rep, currencyCode));
		return type;
	}

	public static RetentionType toRetentionType(OrganizationModel organization, RetentionRepresentation rep) {
		RetentionType type = new RetentionType();
		type.setUblVersionID(UblSunatConfiguration.VERSION_ID.getCodigo());
		type.setCustomizationID(UblSunatConfiguration.CUSTOMIZATION_ID.getCodigo());
		type.addSignature(toSignatureType(organization));
		type.setId(rep.getSerie() + UblSunatConfiguration.ID_SEPARATOR.getCodigo() + rep.getNumero());
		// Date
		if (rep.getFechaDeEmision() != null) {
			type.setIssueDate(toGregorianCalendar(rep.getFechaDeEmision().toLocalDate()));
		}
		type.setAgentParty(toAgentPartyType(organization));
		type.setReceiverParty(toReceiverPartyType(rep));
		if (rep.getCodigoDocumento() != null) {
			type.setSunatRetentionSystemCode(rep.getCodigoDocumento());
		}
		if (rep.getTasaDocumento() != null) {
			type.setSunatRetentionPercent(rep.getTasaDocumento());
		}
		BigDecimal totalInvoiceAmount = new BigDecimal(0);
		BigDecimal totalPaid = new BigDecimal(0);
		for (DocumentReferenceRepresentation reference : rep.getReference()) {
			BigDecimal amount = new BigDecimal(0);
			BigDecimal paid = new BigDecimal(0);
			if (rep.getMoneda() != null && reference.getMonedaPago() != null
					&& reference.getMonedaPago() != rep.getMoneda()) {
				if (reference.getTipoDeCambio() != null && reference.getTipoDeCambio() != new BigDecimal(0)) {
					amount = reference.getTotalPago().multiply(reference.getTipoDeCambio())
							.multiply(rep.getTasaDocumento());
					paid = reference.getTotalPago().multiply(reference.getTipoDeCambio()).subtract(amount);
				}
			} else {
				amount = reference.getTotalPago().multiply(rep.getTasaDocumento());
				paid = reference.getTotalPago().subtract(amount);
			}
			totalInvoiceAmount.add(amount);
			totalPaid.add(paid);
		}
		type.setTotalInvoiceAmount(totalInvoiceAmount, rep.getMoneda());
		type.setSunatTotalPaid(totalPaid, rep.getMoneda());
		for (DocumentReferenceRepresentation reference : rep.getReference()) {
			type.addRetentionDocumentReference(
					toRetentionDocumentReferenceType(reference, rep.getMoneda(), rep.getTasaDocumento()));
		}
		return type;
	}

	private static SUNATRetentionDocumentReferenceType toRetentionDocumentReferenceType(
			DocumentReferenceRepresentation rep, String currencyCode, BigDecimal retencion) {
		SUNATRetentionDocumentReferenceType type = new SUNATRetentionDocumentReferenceType();
		type.setID(toIDType(rep));
		if (rep.getFechaDeDocumentoRelacionado() != null) {
			type.setIssueDate(toGregorianCalendar(rep.getFechaDeDocumentoRelacionado().toLocalDate()));
		}
		if (rep.getTotalDocumentoRelacionado() != null && rep.getMonedaDocumentRelacionado() != null) {
			type.setTotalInvoiceAmount(rep.getTotalDocumentoRelacionado(), rep.getMonedaDocumentRelacionado());
		}
		type.setPayment(toPaymentType(rep));
		type.setSUNATRetentionInformation(toSUNATRetentionInformation(rep, currencyCode, retencion));
		return type;
	}

	private static SUNATRetentionInformationType toSUNATRetentionInformation(DocumentReferenceRepresentation rep,
			String currencyCode, BigDecimal retencion) {
		SUNATRetentionInformationType type = new SUNATRetentionInformationType();
		type.setSUNATRetentionAmount(toSUNATRetentionAmountType(rep, currencyCode, retencion));
		if (rep.getFechaPago() != null) {
			type.setSUNATRetentionDate(toGregorianCalendar(rep.getFechaPago().toLocalDate()));
		}
		type.setSUNATNetTotalPaid(tosetSUNATNetTotalPaidType(rep, currencyCode, retencion));
		type.setExchangeRate(toExchangeRateType(rep, currencyCode));
		return type;
	}

	private static AmountType tosetSUNATNetTotalPaidType(DocumentReferenceRepresentation rep, String currencyCode,
			BigDecimal retencion) {
		AmountType type = new AmountType();
		type.setCurrencyID(currencyCode);
		BigDecimal retentionAmount = new BigDecimal(0);
		BigDecimal totalPaid = new BigDecimal(0);
		if (currencyCode != null && rep.getMonedaPago() != null && rep.getMonedaPago() != currencyCode) {
			if (rep.getTipoDeCambio() != null && rep.getTipoDeCambio() != new BigDecimal(0)) {
				retentionAmount = rep.getTotalPago().multiply(rep.getTipoDeCambio()).multiply(retencion);
				totalPaid = rep.getTotalPago().multiply(rep.getTipoDeCambio()).subtract(retentionAmount);
			}
		} else {
			retentionAmount = rep.getTotalPago().multiply(retencion);
			totalPaid = rep.getTotalPago().subtract(retentionAmount);
		}
		type.setValue(totalPaid);
		return type;
	}

	private static AmountType toSUNATRetentionAmountType(DocumentReferenceRepresentation rep, String currencyCode,
			BigDecimal retencion) {
		AmountType type = new AmountType();
		type.setCurrencyID(currencyCode);
		BigDecimal retentionAmount = new BigDecimal(0);
		if (currencyCode != null && rep.getMonedaPago() != null && rep.getMonedaPago() != currencyCode) {
			if (rep.getTipoDeCambio() != null && rep.getTipoDeCambio() != new BigDecimal(0)) {
				retentionAmount = rep.getTotalPago().multiply(rep.getTipoDeCambio()).multiply(retencion);
			}
		} else {
			retentionAmount = rep.getTotalPago().multiply(retencion);
		}
		type.setValue(retentionAmount);
		return type;
	}

	private static ExchangeRateType toExchangeRateType(DocumentReferenceRepresentation rep, String currencyCode) {
		ExchangeRateType type = new ExchangeRateType();
		if (rep.getMonedaPago() != null) {
			type.setSourceCurrencyCode(rep.getMonedaPago());
		}
		type.setTargetCurrencyCode(currencyCode);
		if (rep.getTipoDeCambio() != null) {
			type.setCalculationRate(rep.getTipoDeCambio());
		}
		if (rep.getFechaPago() != null) {
			type.setDate(toGregorianCalendar(rep.getFechaPago().toLocalDate()));
		}
		return type;
	}

	private static PaymentType toPaymentType(DocumentReferenceRepresentation rep) {
		PaymentType type = new PaymentType();
		if (rep.getNumeroPago() != null) {
			type.setID(rep.getNumeroPago());
		}
		type.setPaidAmount(toPaidAmountType(rep));
		if (rep.getFechaPago() != null) {
			type.setPaidDate(toGregorianCalendar(rep.getFechaPago().toLocalDate()));
		}
		return type;
	}

	private static PaidAmountType toPaidAmountType(DocumentReferenceRepresentation rep) {
		PaidAmountType type = new PaidAmountType();
		if (rep.getMonedaPago() != null) {
			type.setCurrencyID(rep.getMonedaPago());
		}
		if (rep.getTotalPago() != null) {
			type.setValue(rep.getTotalPago());
		}
		return type;
	}

	private static IDType toIDType(DocumentReferenceRepresentation rep) {
		IDType type = new IDType();
		if (rep.getTipoDocumentRelacionado() != null) {
			type.setSchemeID(rep.getTipoDocumentRelacionado());
		}
		if (rep.getNumeroDocumentRelacionado() != null) {
			type.setValue(rep.getNumeroDocumentRelacionado());
		}
		return type;
	}

	private static PartyType toReceiverPartyType(RetentionRepresentation rep) {
		PartyType type = new PartyType();
		type.setPartyIdentification(toPartyIdentificationType(rep));
		type.setPartyName(toPartyNameType(rep));
		type.setPostalAddress(toPostalAddressType(rep));
		type.setPartyLegalEntity(toPartyLegalEntityType(rep));
		return type;
	}

	private static List<PartyLegalEntityType> toPartyLegalEntityType(RetentionRepresentation rep) {
		List<PartyLegalEntityType> list = new ArrayList<>();
		PartyLegalEntityType type = new PartyLegalEntityType();
		if (rep.getEntidadDenominacion() != null) {
			type.setRegistrationName(rep.getEntidadDenominacion());
		}
		return list;
	}

	private static AddressType toPostalAddressType(RetentionRepresentation rep) {
		AddressType type = new AddressType();
		if (rep.getEntidadDireccion() != null) {
			type.setStreetName(rep.getEntidadDireccion());
		}
		return type;
	}

	private static List<PartyNameType> toPartyNameType(RetentionRepresentation rep) {
		List<PartyNameType> list = new ArrayList<>();
		PartyNameType type = new PartyNameType();
		if (rep.getEntidadDenominacion() != null) {
			type.setName(rep.getEntidadDenominacion());
		}
		list.add(type);
		return list;
	}

	private static List<PartyIdentificationType> toPartyIdentificationType(RetentionRepresentation rep) {
		List<PartyIdentificationType> list = new ArrayList<>();
		PartyIdentificationType type = new PartyIdentificationType();
		type.setID(toIDType(rep));
		list.add(type);
		return list;
	}

	private static IDType toIDType(RetentionRepresentation rep) {
		IDType type = new IDType();
		if (rep.getEntidadTipoDeDocumento() != null) {
			type.setSchemeID(rep.getEntidadTipoDeDocumento());
		}
		if (rep.getEntidadNumeroDeDocumento() != null) {
			type.setValue(rep.getEntidadNumeroDeDocumento());
		}
		return type;
	}

	private static PartyType toAgentPartyType(OrganizationModel organization) {
		PartyType type = new PartyType();
		type.setPartyIdentification(Arrays.asList(toPartyIdentificationType(organization, false)));
		type.setPartyName(Arrays.asList(toPartyNameType(organization)));
		type.setPostalAddress(toPostalAddressType(organization));
		type.setPartyLegalEntity(Arrays.asList(toPartyLegalEntityType(organization)));
		return type;
	}

	private static PartyLegalEntityType toPartyLegalEntityType(OrganizationModel organization) {
		PartyLegalEntityType type = new PartyLegalEntityType();
		if (organization.getRegistrationName() != null) {
			type.setRegistrationName(organization.getRegistrationName());
		}
		return type;
	}

	private static AddressType toPostalAddressType(OrganizationModel organization) {
		AddressType type = new AddressType();
		if (organization.getPostalAddressId() != null) {
			type.setID(organization.getPostalAddressId());
		}
		if (organization.getStreetName() != null) {
			type.setStreetName(organization.getStreetName());
		}
		if (organization.getCitySubdivisionName() != null) {
			type.setCitySubdivisionName(organization.getCitySubdivisionName());
		}
		if (organization.getCityName() != null) {
			type.setCityName(organization.getCityName());
		}
		if (organization.getCountrySubentity() != null) {
			type.setCountrySubentity(organization.getCountrySubentity());
		}
		if (organization.getDistrict() != null) {
			type.setDistrict(organization.getDistrict());
		}
		type.setCountry(toCountryType(organization));
		return type;
	}

	private static CountryType toCountryType(OrganizationModel organization) {
		CountryType type = new CountryType();
		if (organization.getCountryIdentificationCode() != null) {
			type.setIdentificationCode(organization.getCountryIdentificationCode());
		}
		return type;
	}

	public static SignatureType toSignatureType(OrganizationModel organization) {
		SignatureType type = new SignatureType();
		if (organization.getName() != null) {
			type.setID(UblSunatConfiguration.ID_SIGN.getCodigo() + organization.getName());
		}
		type.setSignatoryParty(toSignatoryPartyType(organization));
		type.setDigitalSignatureAttachment(toDigitalSignatureAttachmentType(organization));
		return type;
	}

	private static AttachmentType toDigitalSignatureAttachmentType(OrganizationModel organization) {
		AttachmentType type = new AttachmentType();
		type.setExternalReference(toExternalReferenceType(organization));
		return type;
	}

	private static ExternalReferenceType toExternalReferenceType(OrganizationModel organization) {
		ExternalReferenceType type = new ExternalReferenceType();
		if (organization.getName() != null) {
			type.setURI(UblSunatConfiguration.URI_SIGN.getCodigo() + organization.getName());
		}
		return type;
	}

	private static PartyType toSignatoryPartyType(OrganizationModel organization) {
		PartyType type = new PartyType();
		type.setPartyIdentification(Arrays.asList(toPartyIdentificationType(organization, true)));
		type.setPartyName(Arrays.asList(toPartyNameType(organization)));
		return type;
	}

	private static PartyNameType toPartyNameType(OrganizationModel organization) {
		PartyNameType type = new PartyNameType();
		if (organization.getRegistrationName() != null) {
			type.setName(organization.getRegistrationName());
		}
		return type;
	}

	private static PartyIdentificationType toPartyIdentificationType(OrganizationModel organization, boolean sign) {
		PartyIdentificationType type = new PartyIdentificationType();
		if (sign) {
			if (organization.getAssignedIdentificationId() != null) {
				type.setID(organization.getAssignedIdentificationId());
			}
		} else {
			type.setID(toIDType(organization));
		}
		return type;
	}

	private static IDType toIDType(OrganizationModel organization) {
		IDType type = new IDType();
		if (organization.getAssignedIdentificationId() != null) {
			type.setValue(organization.getAssignedIdentificationId());
		}
		if (organization.getAdditionalAccountId() != null) {
			type.setSchemeID(organization.getAdditionalAccountId());
		}
		return type;
	}

	public static SummaryDocumentsType toSummaryDocumentType(OrganizationModel organization,
			SummaryRepresentation rep) {
		SummaryDocumentsType type = new SummaryDocumentsType();
		type.setUblVersionID(UblSunatConfiguration.VERSION_ID.getCodigo());
		type.setCustomizationID(UblSunatConfiguration.CUSTOMIZATION_ID.getCodigo());
		type.setId(rep.getSerie() + UblSunatConfiguration.ID_SEPARATOR.getCodigo() + rep.getNumero());
		if (rep.getFechaDeEmision() != null) {
			type.setIssueDate(toGregorianCalendar(rep.getFechaDeEmision().toLocalDate()));
		}
		if (rep.getFechaDeReferencia() != null) {
			type.setIssueDate(toGregorianCalendar(rep.getFechaDeReferencia().toLocalDate()));
		}
		type.addSignature(toSignatureType(organization));
		type.setAccountingSupplierParty(toAccountingSupplierPartyType(organization));
		for (SummaryLineRepresentation line : rep.getLine()) {
			type.addSummaryDocumentsLine(toSummaryDocumentsLineType(line));
		}
		return type;
	}

	private static SummaryDocumentsLineType toSummaryDocumentsLineType(SummaryLineRepresentation rep) {
		SummaryDocumentsLineType type = new SummaryDocumentsLineType();
		if (rep.getLineID() != null) {
			type.setID(rep.getLineID());
		}
		if (rep.getCodigoDocumento() != null) {
			type.setDocumentTypeCode(rep.getCodigoDocumento());
		}
		if (rep.getSerieDocumento() != null) {
			type.setDocumentSerialID(rep.getSerieDocumento());
		}
		if (rep.getNumeroInicioDocumento() != null) {
			type.setStartDocumentNumberID(rep.getNumeroInicioDocumento());
		}
		if (rep.getNumeroFinDocumento() != null) {
			type.setEndDocumentNumberID(rep.getNumeroFinDocumento());
		}
		if (rep.getTotalAmount() != null && rep.getMoneda() != null) {
			type.setTotalAmount(rep.getTotalAmount(), rep.getMoneda());
		}
		if (rep.getPayment() != null) {
			for (BillingPaymentRepresentation payment : rep.getPayment()) {
				type.addBillingPayment(toBillingPaymentType(payment, rep.getMoneda()));
			}
		}
		type.addAllowanceCharge(toAllowanceChargeType(rep));
		for (TaxTotalRepresentation tax : rep.getTaxTotal()) {
			type.addTaxTotal(toTaxTotalType(tax, rep.getMoneda()));
		}
		return type;
	}

	private static TaxTotalType toTaxTotalType(TaxTotalRepresentation rep, String currencyCode) {
		TaxTotalType type = new TaxTotalType();
		type.setTaxAmount(toTaxAmountType(rep, currencyCode));
		type.addTaxSubtotal(toTaxSubtotalType(rep, currencyCode));
		return type;
	}

	private static TaxSubtotalType toTaxSubtotalType(TaxTotalRepresentation rep, String currencyCode) {
		TaxSubtotalType type = new TaxSubtotalType();
		type.setTaxAmount(toTaxAmountType(rep, currencyCode));
		type.setTaxCategory(toTaxCategoryType(rep));
		return type;
	}

	private static TaxCategoryType toTaxCategoryType(TaxTotalRepresentation rep) {
		TaxCategoryType type = new TaxCategoryType();
		type.setTaxScheme(toTaxSchemeType(rep));
		return type;
	}

	private static TaxSchemeType toTaxSchemeType(TaxTotalRepresentation rep) {
		TaxSchemeType type = new TaxSchemeType();
		if (rep.getTaxSchemeID() != null) {
			type.setID(rep.getTaxSchemeID());
		}
		if (rep.getTaxSchemeName() != null) {
			type.setName(rep.getTaxSchemeName());
		}
		if (rep.getTaxTypeCode() != null) {
			type.setTaxTypeCode(rep.getTaxTypeCode());
		}
		return type;
	}

	private static TaxAmountType toTaxAmountType(TaxTotalRepresentation rep, String currencycode) {
		TaxAmountType type = new TaxAmountType();
		type.setCurrencyID(currencycode);
		if (rep.getTaxAmount() != null) {
			type.setValue(rep.getTaxAmount());
		}
		return type;
	}

	private static AllowanceChargeType toAllowanceChargeType(SummaryLineRepresentation rep) {
		AllowanceChargeType type = new AllowanceChargeType();
		type.setChargeIndicator(rep.isChargeIndicator());
		if (rep.getChargeAmount() != null) {
			type.setAmount(rep.getChargeAmount());
		}
		return type;
	}

	private static PaymentType toBillingPaymentType(BillingPaymentRepresentation rep, String currencycode) {
		PaymentType type = new PaymentType();
		type.setPaidAmount(toPaidAmountType(rep, currencycode));
		if (rep.getInstructionID() != null) {
			type.setInstructionID(rep.getInstructionID());
		}
		return type;
	}

	private static PaidAmountType toPaidAmountType(BillingPaymentRepresentation rep, String currencycode) {
		PaidAmountType type = new PaidAmountType();
		type.setCurrencyID(currencycode);
		if (rep.getPaidAmount() != null) {
			type.setValue(rep.getPaidAmount());
		}
		return type;
	}

	public static VoidedDocumentsType toVoidedDocumentType(OrganizationModel organization, VoidedRepresentation rep) {
		VoidedDocumentsType type = new VoidedDocumentsType();
		type.setUBLVersionID(UblSunatConfiguration.VERSION_ID.getCodigo());
		type.setCustomizationID(UblSunatConfiguration.CUSTOMIZATION_ID.getCodigo());
		type.setID(rep.getSerie() + UblSunatConfiguration.ID_SEPARATOR.getCodigo() + rep.getNumero());
		if (rep.getFechaDeDocumento() != null) {
			type.setIssueDate(toGregorianCalendar(rep.getFechaDeDocumento().toLocalDate()));
		}
		if (rep.getFechaReferencia() != null) {
			type.setIssueDate(toGregorianCalendar(rep.getFechaReferencia().toLocalDate()));
		}
		type.addSignature(toSignatureType(organization));
		type.setAccountingSupplierParty(toAccountingSupplierPartyType(organization));
		for (VoidedLineRepresentation line : rep.getLine()) {
			type.addVoidedDocumentsLine(toVoidedDocumentsLineType(line));
		}
		return type;
	}

	private static VoidedDocumentsLineType toVoidedDocumentsLineType(VoidedLineRepresentation rep) {
		VoidedDocumentsLineType type = new VoidedDocumentsLineType();
		if (rep.getLineID() != null) {
			type.setLineID(rep.getLineID());
		}
		if (rep.getCodigoDocumento() != null) {
			type.setDocumentTypeCode(rep.getCodigoDocumento());
		}
		if (rep.getSerieDocumento() != null) {
			type.setDocumentSerialID(rep.getSerieDocumento());
		}
		if (rep.getNumeroDocumento() != null) {
			type.setDocumentNumberID(rep.getNumeroDocumento());
		}
		if (rep.getDescripcion() != null) {
			type.setVoidReasonDescription(rep.getDescripcion());
		}
		return type;
	}

	private static SupplierPartyType toAccountingSupplierPartyType(OrganizationModel organization) {
		SupplierPartyType type = new SupplierPartyType();
		type.setCustomerAssignedAccountID(organization.getAssignedIdentificationId());
		type.setAdditionalAccountID(toAdditionalAccountIDType(organization));
		type.setParty(toPartyType(organization));
		return type;
	}

	private static PartyType toPartyType(OrganizationModel organization) {
		PartyType type = new PartyType();
		type.setPartyLegalEntity(Arrays.asList(toPartyLegalEntityType(organization)));
		return type;
	}

	private static List<AdditionalAccountIDType> toAdditionalAccountIDType(OrganizationModel organization) {
		List<AdditionalAccountIDType> list = new ArrayList<>();
		AdditionalAccountIDType type = new AdditionalAccountIDType();
		type.setValue(organization.getAdditionalAccountId());
		list.add(type);
		return list;
	}

	public static SupplierPartyType toSupplierParty(OrganizationModel organization) {
		SupplierPartyType supplierPartyType = new SupplierPartyType();
		if (organization.getAssignedIdentificationId() != null) {
			List<AdditionalAccountIDType> additionalAccountIDType = new ArrayList<>();
			additionalAccountIDType.add(new AdditionalAccountIDType(organization.getAdditionalAccountId()));
			supplierPartyType.setAdditionalAccountID(additionalAccountIDType);
		}
		if (organization.getAdditionalAccountId() != null) {
			supplierPartyType.setCustomerAssignedAccountID(organization.getAssignedIdentificationId());
		}

		PartyType partyType = new PartyType();
		if (organization.getRegistrationName() != null) {
			List<PartyLegalEntityType> partyLegalEntityTypes = new ArrayList<>();
			PartyLegalEntityType partyLegalEntityType = new PartyLegalEntityType();
			partyLegalEntityType.setRegistrationName(organization.getRegistrationName());

			partyLegalEntityTypes.add(partyLegalEntityType);
			partyType.setPartyLegalEntity(partyLegalEntityTypes);

			// Address
			AddressType addressType = new AddressType();
			if (organization.getStreetName() == null || organization.getCitySubdivisionName() == null
					|| organization.getCityName() == null || organization.getCountrySubentity() == null
					|| organization.getDistrict() == null || organization.getCountryIdentificationCode() == null) {
				throw new ModelException("Inssuficient information on organization");
			}

			addressType.setStreetName(organization.getStreetName());
			addressType.setCitySubdivisionName(organization.getCitySubdivisionName());
			addressType.setCityName(organization.getCityName());
			addressType.setCountrySubentity(organization.getCountrySubentity());

			CountryType countryType = new CountryType();
			countryType.setIdentificationCode(organization.getCountryIdentificationCode());
			addressType.setCountry(countryType);

			partyType.setPostalAddress(addressType);
		}
		supplierPartyType.setParty(partyType);

		return supplierPartyType;
	}

	public static CustomerPartyType toCustomerPartyType(DocumentRepresentation rep) {
		CustomerPartyType customerPartyType = new CustomerPartyType();
		if (rep.getEntidadTipoDeDocumento() != null) {
			List<AdditionalAccountIDType> additionalAccountIDType = new ArrayList<>();
			additionalAccountIDType.add(new AdditionalAccountIDType(rep.getEntidadTipoDeDocumento()));
			customerPartyType.setAdditionalAccountID(additionalAccountIDType);
		}
		if (rep.getEntidadNumeroDeDocumento() != null) {
			customerPartyType.setCustomerAssignedAccountID(rep.getEntidadNumeroDeDocumento());
		}

		PartyType partyType = new PartyType();
		if (rep.getEntidadDenominacion() != null) {
			List<PartyLegalEntityType> partyLegalEntityTypes = new ArrayList<>();
			PartyLegalEntityType partyLegalEntityType = new PartyLegalEntityType();
			partyLegalEntityType.setRegistrationName(rep.getEntidadDenominacion());

			partyLegalEntityTypes.add(partyLegalEntityType);
			partyType.setPartyLegalEntity(partyLegalEntityTypes);

			// Address
			if (rep.getEntidadDireccion() != null) {
				AddressType addressType = new AddressType();
				addressType.setStreetName(rep.getEntidadDireccion());
				partyType.setPostalAddress(addressType);
			}
			// Contact
			if (rep.getEntidadEmail() != null) {
				ContactType contactType = new ContactType();
				contactType.setElectronicMail(rep.getEntidadEmail());
				partyType.setContact(contactType);
			}
		}
		customerPartyType.setParty(partyType);

		return customerPartyType;
	}

	public static MonetaryTotalType toLegalMonetaryTotalType(DocumentRepresentation rep) {
		MonetaryTotalType monetaryTotalType = new MonetaryTotalType();
		if (rep.getTotal() != null) {
			PayableAmountType payableAmountType = new PayableAmountType(rep.getTotal());
			payableAmountType.setCurrencyID(rep.getMoneda());
			monetaryTotalType.setPayableAmount(payableAmountType);
		}
		if (rep.getTotalOtrosCargos() != null) {
			ChargeTotalAmountType chargeTotalAmountType = new ChargeTotalAmountType(rep.getTotalOtrosCargos());
			chargeTotalAmountType.setCurrencyID(rep.getMoneda());
			monetaryTotalType.setChargeTotalAmount(chargeTotalAmountType);
		}

		return monetaryTotalType;
	}

	public static TaxTotalType toTaxTotalIGV(DocumentRepresentation rep) {
		TaxTotalType taxTotalType = new TaxTotalType();

		// Tax amount
		TaxAmountType taxAmountType = new TaxAmountType(rep.getTotalIgv());
		taxAmountType.setCurrencyID(rep.getMoneda());

		taxTotalType.setTaxAmount(taxAmountType);

		// Tax Subtotal
		TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
		taxSubtotalType.setTaxAmount(taxAmountType);

		TaxCategoryType taxCategoryType = new TaxCategoryType();
		TaxSchemeType taxSchemeType = new TaxSchemeType();

		taxSchemeType.setID(CodigoTipoTributo.IGV.getId());
		taxSchemeType.setName(CodigoTipoTributo.IGV.toString());
		taxSchemeType.setTaxTypeCode(CodigoTipoTributo.IGV.getCodigo());
		taxCategoryType.setTaxScheme(taxSchemeType);
		taxSubtotalType.setTaxCategory(taxCategoryType);

		return taxTotalType;
	}

	public static Element generateElement(AdditionalInformationTypeSunatAgg object) {
		try {
			InvoiceFactory factory = new InvoiceFactory();
			JAXBContext context = JAXBContext.newInstance(InvoiceFactory.class);

			Marshaller marshallerElement = context.createMarshaller();
			JAXBElement<AdditionalInformationTypeSunatAgg> jaxbElement = factory.createAdditionalInformation(object);
			DOMResult res = new DOMResult();
			marshallerElement.marshal(jaxbElement, res);
			Element element = ((Document) res.getNode()).getDocumentElement();
			return element;
		} catch (JAXBException e) {
			throw new ModelException(e);
		}

	}

	public static AdditionalPropertyType generateAdditionalInformationSunatTotal(BigDecimal amount) {
		MoneyConverters converter = MoneyConverters.SPANISH_BANKING_MONEY_VALUE;
		String valueAsWords = converter.asWords(amount);

		AdditionalPropertyType additionalProperty = new AdditionalPropertyType();
		additionalProperty.setID(new IDType(CodigoElementosAdicionalesComprobante.MONTO_EN_LETRAS.getCodigo()));
		additionalProperty.setValue(new ValueType(valueAsWords));
		return additionalProperty;
	}

}
