package org.openfact.pe.models.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
import org.openfact.common.converts.DateUtils;
import org.openfact.common.finance.MoneyConverters;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.models.enums.*;
import org.openfact.pe.models.types.*;
import org.openfact.pe.models.types.perception.PerceptionType;
import org.openfact.pe.models.types.perception.SUNATPerceptionDocumentReferenceType;
import org.openfact.pe.models.types.perception.SUNATPerceptionInformationType;
import org.openfact.pe.models.types.retention.RetentionType;
import org.openfact.pe.models.types.retention.SUNATRetentionDocumentReferenceType;
import org.openfact.pe.models.types.retention.SUNATRetentionInformationType;
import org.openfact.pe.models.types.summary.SummaryDocumentsLineType;
import org.openfact.pe.models.types.summary.SummaryDocumentsType;
import org.openfact.pe.models.types.voided.VoidedDocumentsLineType;
import org.openfact.pe.models.types.voided.VoidedDocumentsType;
import org.openfact.pe.representations.idm.*;
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
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String locale = sdf.format(DateUtils.asDate(date));
            XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(locale);
            return xmlCal;
        } catch (DatatypeConfigurationException e) {
            throw new ModelException(e);
        }
    }

    public static XMLGregorianCalendar toGregorianCalendarTime(LocalDateTime date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String locale = sdf.format(DateUtils.asDate(date));
            XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(locale);
            return xmlCal;
        } catch (DatatypeConfigurationException e) {
            throw new ModelException(e);
        }
    }

    public static InvoiceType toInvoiceType(OrganizationModel organization, DocumentRepresentation rep) {
        InvoiceType invoiceType = new InvoiceType();

        // General config
        invoiceType.setUBLVersionID(SunatRepresentationToType.UBL_VERSION_ID);
        invoiceType.setCustomizationID(SunatRepresentationToType.CUSTOMIZATION_ID);

        // documentId
        if (rep.getNumero() != null && rep.getSerie() != null && !rep.getNumero().trim().isEmpty() && !rep.getSerie().trim().isEmpty()) {
            invoiceType.setID(rep.getSerie().toUpperCase() + "-" + rep.getNumero());
        }

        // Issue Date
        if (rep.getFechaDeEmision() != null) {
            invoiceType.setIssueDate(toGregorianCalendar(rep.getFechaDeEmision().toLocalDate()));
            invoiceType.setIssueTime(toGregorianCalendarTime(rep.getFechaDeEmision()));
        } else {
            invoiceType.setIssueDate(toGregorianCalendar(LocalDate.now()));
            invoiceType.setIssueTime(toGregorianCalendarTime(LocalDateTime.now()));
        }
        if (rep.getFechaDeVencimiento() != null) {
            invoiceType.setDueDate(toGregorianCalendar(rep.getFechaDeVencimiento().toLocalDate()));
        }

        // Currency
        if (rep.getMoneda() != null) {
            invoiceType.setDocumentCurrencyCode(rep.getMoneda());
        }

        // Supplier
        invoiceType.setAccountingSupplierParty(toSupplierParty(organization));

        // Customer
        invoiceType.setAccountingCustomerParty(toCustomerPartyType(rep));

        // Tax Total
        if (rep.getTotalIgv() != null && rep.getTotalIgv().compareTo(BigDecimal.ZERO) > 0) {
            invoiceType.setTaxTotal(Arrays.asList(toTaxTotalIGV(rep)));
        }

        // Legal monetary total
        invoiceType.setLegalMonetaryTotal(toLegalMonetaryTotalType(rep));

        // Invoice type code
        if (rep.getTipo() != null) {
            invoiceType.setInvoiceTypeCode(rep.getTipo());
        }

        if (rep.getTipoDeCambio() != null) {
        }

        // Notes type
        if (rep.getObservaciones() != null) {
            List<NoteType> noteTypes = new ArrayList<>();
            noteTypes.add(new NoteType(rep.getObservaciones()));
            invoiceType.setNote(noteTypes);
        }

        // Signature
        invoiceType.setSignature(Arrays.asList(toSignatureType(organization)));

        // Lines
        if (rep.getDetalle() != null) {
            invoiceType.setInvoiceLine(toInvoiceLineType(rep));
        }

        // Extensions
        invoiceType.setUBLExtensions(toUBLExtensionsType(rep));

        return invoiceType;
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
            List<PriceType> priceTypes = new ArrayList<>();

            PriceType primaryPriceType = null; // Operaciones gravadas y exoneradas
            PriceType secondaryPriceType = null; // Operaciones inafectas, necesitan dos AlternativeConditionPrice una con valor cero y otra con el valor del producto

            TipoAfectacionIgv tipoAfectacionIgv = TipoAfectacionIgv.searchFromCodigo(lineRep.getTipoDeIgv());
            if (tipoAfectacionIgv == null) {
                throw new IllegalStateException("Invalid IGV code");
            }

            if (!tipoAfectacionIgv.isOperacionNoOnerosa()) {
                primaryPriceType = toPriceType(rep.getMoneda(), lineRep.getPrecioUnitario(), TipoPrecioVentaUnitario.PRECIO_UNITARIO.getCodigo());
                priceTypes.add(primaryPriceType);
            } else {
                primaryPriceType = toPriceType(rep.getMoneda(), BigDecimal.ZERO, TipoPrecioVentaUnitario.PRECIO_UNITARIO.getCodigo());
                priceTypes.add(primaryPriceType);

                secondaryPriceType = toPriceType(rep.getMoneda(), lineRep.getPrecioUnitario(), TipoPrecioVentaUnitario.VALOR_REF_UNIT_EN_OPER_NO_ORENOSAS.getCodigo());
                priceTypes.add(secondaryPriceType);
            }

            pricingReferenceType.setAlternativeConditionPrice(priceTypes);
            invoiceLineType.setPricingReference(pricingReferenceType);

            // Item
            ItemType itemType = new ItemType();
            itemType.setDescription(Arrays.asList(new DescriptionType(lineRep.getDescripcion())));
            invoiceLineType.setItem(itemType);

            // Tax Total
            TaxTotalType taxTotalType = new TaxTotalType();
            TaxAmountType taxAmountType1 = new TaxAmountType(lineRep.getIgv());
            taxAmountType1.setCurrencyID(rep.getMoneda());
            taxTotalType.setTaxAmount(taxAmountType1);

            TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
            TaxAmountType taxAmountType2 = new TaxAmountType(lineRep.getIgv());
            taxAmountType2.setCurrencyID(rep.getMoneda());
            taxSubtotalType.setTaxAmount(taxAmountType2);

            TaxCategoryType taxCategoryType = new TaxCategoryType();
            taxCategoryType.setTaxExemptionReasonCode(lineRep.getTipoDeIgv());
            TaxSchemeType taxSchemeType = new TaxSchemeType();
            taxSchemeType.setID(TipoTributo.IGV.getId());
            taxSchemeType.setName(TipoTributo.IGV.toString());
            taxSchemeType.setTaxTypeCode(TipoTributo.IGV.getCodigo());
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

        // documentId
        if (rep.getNumero() != null && rep.getSerie() != null && !rep.getNumero().trim().isEmpty() && !rep.getSerie().trim().isEmpty()) {
            type.setID(rep.getSerie().toUpperCase() + "-" + rep.getNumero());
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

        // Discrepancy response
        ResponseType responseType = new ResponseType();
        responseType.setReferenceID(rep.getDocumentoQueSeModifica());
        responseType.setResponseCode(rep.getTipoDeNotaDeCredito());
        if (rep.getObservaciones() != null) {
            responseType.setDescription(Arrays.asList(new DescriptionType(rep.getObservaciones())));
        } else {
            responseType.setDescription(Arrays.asList(new DescriptionType("Sin observacion")));
        }
        type.setDiscrepancyResponse(Arrays.asList(responseType));

        // Billing reference
        BillingReferenceType billingReferenceType = new BillingReferenceType();

        DocumentReferenceType documentReferenceType = new DocumentReferenceType();
        documentReferenceType.setID(rep.getDocumentoQueSeModifica().trim().toUpperCase());

        String identificator = rep.getDocumentoQueSeModifica().substring(0, 1);
        if (identificator.equalsIgnoreCase(TipoInvoice.BOLETA.toString().substring(0, 1))) {
            documentReferenceType.setDocumentTypeCode(TipoInvoice.BOLETA.getCodigo());
        } else if (identificator.equalsIgnoreCase(TipoInvoice.FACTURA.toString().substring(0, 1))) {
            documentReferenceType.setDocumentTypeCode(TipoInvoice.FACTURA.getCodigo());
        }

        billingReferenceType.setInvoiceDocumentReference(documentReferenceType);

        type.setBillingReference(Arrays.asList(billingReferenceType));

        // Supplier
        type.setAccountingSupplierParty(toSupplierParty(organization));

        // Customer
        type.setAccountingCustomerParty(toCustomerPartyType(rep));

        // Tax Total
        if (rep.getTotalIgv() != null && rep.getTotalIgv().compareTo(BigDecimal.ZERO) > 0) {
            type.setTaxTotal(Arrays.asList(toTaxTotalIGV(rep)));
        }

        // Legal monetary total
        type.setLegalMonetaryTotal(toLegalMonetaryTotalType(rep));

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
            type.setCreditNoteLine(toCreditNoteLineType(rep));
        }

        // Extensions
        type.setUBLExtensions(toUBLExtensionsType(rep));

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
            List<PriceType> priceTypes = new ArrayList<>();

            PriceType primaryPriceType = null; // Operaciones gravadas y exoneradas
            PriceType secondaryPriceType = null; // Operaciones inafectas, necesitan dos AlternativeConditionPrice una con valor cero y otra con el valor del producto

            TipoAfectacionIgv tipoAfectacionIgv = TipoAfectacionIgv.searchFromCodigo(lineRep.getTipoDeIgv());
            if (tipoAfectacionIgv == null) {
                throw new IllegalStateException("Invalid IGV code");
            }

            if (!tipoAfectacionIgv.isOperacionNoOnerosa()) {
                primaryPriceType = toPriceType(rep.getMoneda(), lineRep.getPrecioUnitario(), TipoPrecioVentaUnitario.PRECIO_UNITARIO.getCodigo());
                priceTypes.add(primaryPriceType);
            } else {
                primaryPriceType = toPriceType(rep.getMoneda(), BigDecimal.ZERO, TipoPrecioVentaUnitario.PRECIO_UNITARIO.getCodigo());
                priceTypes.add(primaryPriceType);

                secondaryPriceType = toPriceType(rep.getMoneda(), lineRep.getPrecioUnitario(), TipoPrecioVentaUnitario.VALOR_REF_UNIT_EN_OPER_NO_ORENOSAS.getCodigo());
                priceTypes.add(secondaryPriceType);
            }

            pricingReferenceType.setAlternativeConditionPrice(priceTypes);
            creditNoteLineType.setPricingReference(pricingReferenceType);

            // Item
            ItemType itemType = new ItemType();
            itemType.setDescription(Arrays.asList(new DescriptionType(lineRep.getDescripcion())));
            creditNoteLineType.setItem(itemType);

            // Tax Total
            TaxTotalType taxTotalType = new TaxTotalType();
            TaxAmountType taxAmountType1 = new TaxAmountType(lineRep.getIgv());
            taxAmountType1.setCurrencyID(rep.getMoneda());
            taxTotalType.setTaxAmount(taxAmountType1);

            TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
            TaxAmountType taxAmountType2 = new TaxAmountType(lineRep.getIgv());
            taxAmountType2.setCurrencyID(rep.getMoneda());
            taxSubtotalType.setTaxAmount(taxAmountType2);

            TaxCategoryType taxCategoryType = new TaxCategoryType();
            taxCategoryType.setTaxExemptionReasonCode(lineRep.getTipoDeIgv());
            TaxSchemeType taxSchemeType = new TaxSchemeType();
            taxSchemeType.setID(TipoTributo.IGV.getId());
            taxSchemeType.setName(TipoTributo.IGV.toString());
            taxSchemeType.setTaxTypeCode(TipoTributo.IGV.getCodigo());
            taxCategoryType.setTaxScheme(taxSchemeType);
            taxSubtotalType.setTaxCategory(taxCategoryType);

            taxTotalType.setTaxSubtotal(Arrays.asList(taxSubtotalType));
            creditNoteLineType.setTaxTotal(Arrays.asList(taxTotalType));

            taxTotalType.setTaxSubtotal(Arrays.asList(taxSubtotalType));
            creditNoteLineType.setTaxTotal(Arrays.asList(taxTotalType));

            //En nota de credito el price va el total y no el valor unitario como en factura
            // Price
            PriceType priceType1 = new PriceType();
            PriceAmountType priceAmountType1 = new PriceAmountType(lineRep.getTotal());
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
        if (rep.getNumero() != null && rep.getSerie() != null && !rep.getNumero().trim().isEmpty() && !rep.getSerie().trim().isEmpty()) {
            type.setID(rep.getSerie().toUpperCase() + "-" + rep.getNumero());
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
        if (rep.getTotalIgv() != null && rep.getTotalIgv().compareTo(BigDecimal.ZERO) > 0) {
            type.setTaxTotal(Arrays.asList(toTaxTotalIGV(rep)));
        }

        // Notes type
        if (rep.getObservaciones() != null) {
            List<NoteType> noteTypes = new ArrayList<>();
            noteTypes.add(new NoteType(rep.getObservaciones()));
            type.setNote(noteTypes);
        }

        // Discrepancy response
        ResponseType responseType = new ResponseType();
        responseType.setReferenceID(rep.getDocumentoQueSeModifica());
        responseType.setResponseCode(rep.getTipoDeNotaDeDebito());
        if (rep.getObservaciones() != null) {
            responseType.setDescription(Arrays.asList(new DescriptionType(rep.getObservaciones())));
        }
        type.setDiscrepancyResponse(Arrays.asList(responseType));

        // Billing reference
        BillingReferenceType billingReferenceType = new BillingReferenceType();

        DocumentReferenceType documentReferenceType = new DocumentReferenceType();
        documentReferenceType.setID(rep.getDocumentoQueSeModifica().trim().toUpperCase());

        String identificator = rep.getDocumentoQueSeModifica().substring(0, 1);
        if (identificator.equalsIgnoreCase(TipoInvoice.BOLETA.toString().substring(0, 1))) {
            documentReferenceType.setDocumentTypeCode(TipoInvoice.BOLETA.getCodigo());
        } else if (identificator.equalsIgnoreCase(TipoInvoice.FACTURA.toString().substring(0, 1))) {
            documentReferenceType.setDocumentTypeCode(TipoInvoice.FACTURA.getCodigo());
        }

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
        type.setUBLExtensions(toUBLExtensionsType(rep));

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
            List<PriceType> priceTypes = new ArrayList<>();

            PriceType primaryPriceType = null; // Operaciones gravadas y exoneradas
            PriceType secondaryPriceType = null; // Operaciones inafectas, necesitan dos AlternativeConditionPrice una con valor cero y otra con el valor del producto

            TipoAfectacionIgv tipoAfectacionIgv = TipoAfectacionIgv.searchFromCodigo(lineRep.getTipoDeIgv());
            if (tipoAfectacionIgv == null) {
                throw new IllegalStateException("Invalid IGV code");
            }

            if (!tipoAfectacionIgv.isOperacionNoOnerosa()) {
                primaryPriceType = toPriceType(rep.getMoneda(), lineRep.getPrecioUnitario(), TipoPrecioVentaUnitario.PRECIO_UNITARIO.getCodigo());
                priceTypes.add(primaryPriceType);
            } else {
                primaryPriceType = toPriceType(rep.getMoneda(), BigDecimal.ZERO, TipoPrecioVentaUnitario.PRECIO_UNITARIO.getCodigo());
                priceTypes.add(primaryPriceType);

                secondaryPriceType = toPriceType(rep.getMoneda(), lineRep.getPrecioUnitario(), TipoPrecioVentaUnitario.VALOR_REF_UNIT_EN_OPER_NO_ORENOSAS.getCodigo());
                priceTypes.add(secondaryPriceType);
            }

            pricingReferenceType.setAlternativeConditionPrice(priceTypes);
            debitNoteLineType.setPricingReference(pricingReferenceType);

            // Item
            ItemType itemType = new ItemType();
            itemType.setDescription(Arrays.asList(new DescriptionType(lineRep.getDescripcion())));
            debitNoteLineType.setItem(itemType);

            // Tax Total
            TaxTotalType taxTotalType = new TaxTotalType();
            TaxAmountType taxAmountType1 = new TaxAmountType(lineRep.getIgv());
            taxAmountType1.setCurrencyID(rep.getMoneda());
            taxTotalType.setTaxAmount(taxAmountType1);

            TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
            TaxAmountType taxAmountType2 = new TaxAmountType(lineRep.getIgv());
            taxAmountType2.setCurrencyID(rep.getMoneda());
            taxSubtotalType.setTaxAmount(taxAmountType2);

            TaxCategoryType taxCategoryType = new TaxCategoryType();
            taxCategoryType.setTaxExemptionReasonCode(lineRep.getTipoDeIgv());
            TaxSchemeType taxSchemeType = new TaxSchemeType();
            taxSchemeType.setID(TipoTributo.IGV.getId());
            taxSchemeType.setName(TipoTributo.IGV.toString());
            taxSchemeType.setTaxTypeCode(TipoTributo.IGV.getCodigo());
            taxCategoryType.setTaxScheme(taxSchemeType);
            taxSubtotalType.setTaxCategory(taxCategoryType);

            taxTotalType.setTaxSubtotal(Arrays.asList(taxSubtotalType));
            debitNoteLineType.setTaxTotal(Arrays.asList(taxTotalType));

            taxTotalType.setTaxSubtotal(Arrays.asList(taxSubtotalType));
            debitNoteLineType.setTaxTotal(Arrays.asList(taxTotalType));

            //En nota de credito el price va el total y no el valor unitario como en factura
            // Price
            // Price
            PriceType priceType1 = new PriceType();
            PriceAmountType priceAmountType1 = new PriceAmountType(lineRep.getTotal());
            priceAmountType1.setCurrencyID(rep.getMoneda());
            priceType1.setPriceAmount(priceAmountType1);
            debitNoteLineType.setPrice(priceType1);

            debitNoteLineTypes.add(debitNoteLineType);
        }
        return debitNoteLineTypes;
    }

    public static PerceptionType toPerceptionType(OpenfactSession session, OrganizationModel organization, DocumentoSunatRepresentation rep) {
        PerceptionType type = new PerceptionType();
        UBLExtensionsType ublExtensionsType = new UBLExtensionsType();
        UBLExtensionType ublExtensionType = new UBLExtensionType();
        ExtensionContentType extensionContentType = new ExtensionContentType();
        type.setUblVersionID(UblSunatConfiguration.VERSION_ID.getCodigo());
        type.setCustomizationID(UblSunatConfiguration.CUSTOMIZATION_ID10.getCodigo());
        type.addSignature(toSignatureType(organization));
        if (rep.getSerieDocumento() != null && rep.getNumeroDocumento() != null) {
            type.setId(rep.getSerieDocumento() + UblSunatConfiguration.ID_SEPARATOR.getCodigo() + rep.getNumeroDocumento());
        }

        // Date
        if (rep.getFechaDeEmision() != null) {
            type.setIssueDate(toGregorianCalendar(DateUtils.asLocalDateTime(rep.getFechaDeEmision()).toLocalDate()));
        } else {
            type.setIssueDate(toGregorianCalendar(LocalDate.now()));
        }
        type.setAgentParty(toAgentPartyType(organization));
        type.setReceiverParty(toReceiverPartyType(rep));
        if (rep.getCodigoDocumento() != null) {
            type.setSunatPerceptionSystemCode(rep.getCodigoDocumento());
        }
        if (rep.getTasaDocumento() != null) {
            type.setSunatPerceptionPercent(rep.getTasaDocumento());
        }
        if (rep.getObservaciones() != null) {
            type.addNote(rep.getObservaciones());
        }
        extensionContentType.setAny(SunatSignerUtils.getSignToElement(session, organization));
        ublExtensionType.setExtensionContent(extensionContentType);
        ublExtensionsType.setUBLExtension(new ArrayList<>(Arrays.asList(ublExtensionType)));
        type.setUblExtensions(ublExtensionsType);

        // Totales
        type.setTotalInvoiceAmount(rep.getTotalDocumentoSunat(), rep.getMonedaDocumento());
        type.setSunatTotalCashed(rep.getTotalPago(), rep.getMonedaDocumento());

        for (DocumentoSunatLineRepresentation reference : rep.getDetalle()) {
            type.addPerceptionDocumentReference(toPerceptionDocumentReferenceType(reference, rep.getMonedaDocumento(), rep.getTasaDocumento()));
        }
        return type;
    }

    private static SUNATPerceptionDocumentReferenceType toPerceptionDocumentReferenceType(DocumentoSunatLineRepresentation rep, String currencyCode, BigDecimal perception) {
        SUNATPerceptionDocumentReferenceType type = new SUNATPerceptionDocumentReferenceType();
        type.setId(toIDType(rep));
        if (rep.getFechaDocumentoRelacionado() != null) {
            type.setIssueDate(toGregorianCalendar(DateUtils.asLocalDate(rep.getFechaDocumentoRelacionado())));
        }
        if (rep.getTotalDocumentoRelacionado() != null && rep.getMonedaDocumentoRelacionado() != null) {
            type.setTotalInvoiceAmount(rep.getTotalDocumentoRelacionado(), rep.getMonedaDocumentoRelacionado());
        }
        type.setPayment(toPaymentType(rep));
        type.setSunatPerceptionInformation(toSUNATPerceptionInformationType(rep, currencyCode, perception));
        return type;
    }

    private static SUNATPerceptionInformationType toSUNATPerceptionInformationType(DocumentoSunatLineRepresentation rep, String currencyCode, BigDecimal perception) {
        SUNATPerceptionInformationType type = new SUNATPerceptionInformationType();
        type.setSunatPerceptionAmount(toSUNATAmountType(rep, currencyCode, perception));
        if (rep.getFechaDocumentoRelacionado() != null) {
            type.setSunatPerceptionDate(toGregorianCalendar(DateUtils.asLocalDate(rep.getFechaDocumentoRelacionado())));
        }
        type.setSunatNetTotalCashed(tosetSUNATNetTotalPaidType(rep, currencyCode, perception));
        type.setExchangeRate(toExchangeRateType(rep, currencyCode));
        return type;
    }

    public static RetentionType toRetentionType(OpenfactSession session, OrganizationModel organization, DocumentoSunatRepresentation rep) {
        RetentionType type = new RetentionType();
        type.setUblVersionID(UblSunatConfiguration.VERSION_ID.getCodigo());
        type.setCustomizationID(UblSunatConfiguration.CUSTOMIZATION_ID10.getCodigo());
        type.addSignature(toSignatureType(organization));
        UBLExtensionsType ublExtensionsType = new UBLExtensionsType();
        UBLExtensionType ublExtensionType = new UBLExtensionType();
        ExtensionContentType extensionContentType = new ExtensionContentType();
        if (rep.getSerieDocumento() != null && rep.getNumeroDocumento() != null) {
            type.setId(rep.getSerieDocumento() + UblSunatConfiguration.ID_SEPARATOR.getCodigo() + rep.getNumeroDocumento());
        }

        // Date
        if (rep.getFechaDeEmision() != null) {
            type.setIssueDate(toGregorianCalendar(DateUtils.asLocalDateTime(rep.getFechaDeEmision()).toLocalDate()));
        } else {
            type.setIssueDate(toGregorianCalendar(LocalDate.now()));
        }
        type.setAgentParty(toAgentPartyType(organization));
        type.setReceiverParty(toReceiverPartyType(rep));
        if (rep.getCodigoDocumento() != null) {
            type.setSunatRetentionSystemCode(rep.getCodigoDocumento());
        }
        if (rep.getTasaDocumento() != null) {
            type.setSunatRetentionPercent(rep.getTasaDocumento());
        }
        if (rep.getObservaciones() != null) {
            type.addNote(rep.getObservaciones());
        }
        extensionContentType.setAny(SunatSignerUtils.getSignToElement(session, organization));
        ublExtensionType.setExtensionContent(extensionContentType);
        ublExtensionsType.setUBLExtension(new ArrayList<>(Arrays.asList(ublExtensionType)));
        type.setUblExtensions(ublExtensionsType);

        // Totales
        type.setTotalInvoiceAmount(rep.getTotalDocumentoSunat(), rep.getMonedaDocumento());
        type.setSunatTotalPaid(rep.getTotalPago(), rep.getMonedaDocumento());

        for (DocumentoSunatLineRepresentation reference : rep.getDetalle()) {
            type.addRetentionDocumentReference(toRetentionDocumentReferenceType(reference, rep.getMonedaDocumento(), rep.getTasaDocumento()));
        }
        return type;
    }

    private static SUNATRetentionDocumentReferenceType toRetentionDocumentReferenceType(DocumentoSunatLineRepresentation rep, String currencyCode, BigDecimal retencion) {
        SUNATRetentionDocumentReferenceType type = new SUNATRetentionDocumentReferenceType();
        type.setID(toIDType(rep));
        if (rep.getFechaDocumentoRelacionado() != null) {
            type.setIssueDate(toGregorianCalendar(DateUtils.asLocalDate(rep.getFechaDocumentoRelacionado())));
        }
        if (rep.getTotalDocumentoRelacionado() != null && rep.getMonedaDocumentoRelacionado() != null) {
            type.setTotalInvoiceAmount(rep.getTotalDocumentoRelacionado(), rep.getMonedaDocumentoRelacionado());
        }
        type.setPayment(toPaymentType(rep));
        type.setSUNATRetentionInformation(toSUNATRetentionInformation(rep, currencyCode, retencion));
        return type;
    }


    private static SUNATRetentionInformationType toSUNATRetentionInformation(DocumentoSunatLineRepresentation rep, String currencyCode, BigDecimal retencion) {
        SUNATRetentionInformationType type = new SUNATRetentionInformationType();
        type.setSUNATRetentionAmount(toSUNATAmountType(rep, currencyCode, retencion));
        if (rep.getFechaDocumentoRelacionado() != null) {
            type.setSUNATRetentionDate(toGregorianCalendar(DateUtils.asLocalDate(rep.getFechaDocumentoRelacionado())));
        }
        type.setSUNATNetTotalPaid(tosetSUNATNetTotalPaidType(rep, currencyCode, retencion));
        if (rep.getTipoCambio() != null) {
            type.setExchangeRate(toExchangeRateType(rep, currencyCode));
        }
        return type;
    }

    public static UBLExtensionsType toUBLExtensionsType(DocumentRepresentation rep) {
        UBLExtensionsType ublExtensionsType = new UBLExtensionsType();
        UBLExtensionType ublExtensionType = new UBLExtensionType();
        ExtensionContentType extensionContentType = new ExtensionContentType();

        AdditionalMonetaryTotalType gravado = new AdditionalMonetaryTotalType();
        if (rep.getTotalGravada() != null) {
            gravado.setID(new IDType(TipoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRAVADAS.getCodigo()));

            PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalGravada());
            payableAmountType.setCurrencyID(rep.getMoneda());
            gravado.setPayableAmount(payableAmountType);
        }

        AdditionalMonetaryTotalType inafecto = new AdditionalMonetaryTotalType();
        if (rep.getTotalInafecta() != null) {
            inafecto.setID(new IDType(TipoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_INAFECTAS.getCodigo()));

            PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalInafecta());
            payableAmountType.setCurrencyID(rep.getMoneda());
            inafecto.setPayableAmount(payableAmountType);
        }

        AdditionalMonetaryTotalType exonerado = new AdditionalMonetaryTotalType();
        if (rep.getTotalExonerada() != null) {
            exonerado.setID(new IDType(TipoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_EXONERADAS.getCodigo()));

            PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalExonerada());
            payableAmountType.setCurrencyID(rep.getMoneda());
            exonerado.setPayableAmount(payableAmountType);
        }

        AdditionalMonetaryTotalType gratuito = new AdditionalMonetaryTotalType();
        if (rep.getTotalGratuita() != null) {
            gratuito.setID(new IDType(TipoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRATUITAS.getCodigo()));

            PayableAmountType payableAmountType = new PayableAmountType(rep.getTotalGratuita());
            payableAmountType.setCurrencyID(rep.getMoneda());
            gratuito.setPayableAmount(payableAmountType);
        }

        AdditionalPropertyType leyendaMontoTexto = null;
        AdditionalPropertyType leyendaGratuito = null;

        if (rep.getTotal().compareTo(new BigDecimal(Integer.MAX_VALUE)) < 0) {
            MoneyConverters converter = MoneyConverters.SPANISH_BANKING_MONEY_VALUE;
            String valueAsWords = converter.asWords(rep.getTotal());

            leyendaMontoTexto = new AdditionalPropertyType();
            leyendaMontoTexto.setID(new IDType(TipoElementosAdicionalesComprobante.MONTO_EN_LETRAS.getCodigo()));
            leyendaMontoTexto.setValue(new ValueType(valueAsWords));
        }
        if (rep.isOperacionGratuita()) {
            leyendaGratuito = new AdditionalPropertyType();
            leyendaGratuito.setID(new IDType(TipoElementosAdicionalesComprobante.LEYENDA_TRANSFERENCIA_GRATUITA.getCodigo()));
            leyendaGratuito.setValue(new ValueType(TipoElementosAdicionalesComprobante.LEYENDA_TRANSFERENCIA_GRATUITA.getDenominacion()));
        }

        // Se coloca solo los monetary mayores a cero ya que si se ponen todos la sunat lo rechaza
        List<AdditionalMonetaryTotalType> additionalMonetaryTotalTypes = Arrays.asList(gravado, inafecto, exonerado, gratuito).stream()
                .filter(p -> p.getPayableAmount().getValue().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
        List<AdditionalPropertyType> additionalPropertyTypes = Arrays.asList(leyendaMontoTexto, leyendaGratuito).stream()
                .filter(p -> p != null)
                .collect(Collectors.toList());

        AdditionalInformationTypeSunatAgg additionalInformation = new AdditionalInformationTypeSunatAgg();
        if (!additionalMonetaryTotalTypes.isEmpty()) {
            additionalInformation.getAdditionalMonetaryTotal().addAll(additionalMonetaryTotalTypes);
        }
        if (!additionalPropertyTypes.isEmpty()) {
            additionalInformation.getAdditionalProperty().addAll(additionalPropertyTypes);
        }

        extensionContentType.setAny(generateElement(additionalInformation));
        ublExtensionType.setExtensionContent(extensionContentType);
        ublExtensionsType.setUBLExtension(new ArrayList<>(Arrays.asList(ublExtensionType)));
        return ublExtensionsType;
    }

    public static PriceType toPriceType(String currencyID, BigDecimal amount, String priceTypeCode) {
        PriceType priceType = new PriceType();

        PriceAmountType amountType = new PriceAmountType(amount);
        amountType.setCurrencyID(currencyID);

        priceType.setPriceTypeCode(priceTypeCode);
        priceType.setPriceAmount(amountType);

        return priceType;
    }


    private static AmountType tosetSUNATNetTotalPaidType(DocumentoSunatLineRepresentation rep, String currencyCode, BigDecimal retencion) {
        AmountType type = new AmountType();
        type.setCurrencyID(currencyCode);
        type.setValue(rep.getImportePago());
        return type;
    }

    private static AmountType toSUNATAmountType(DocumentoSunatLineRepresentation rep, String currencyCode, BigDecimal retencion) {
        AmountType type = new AmountType();
        type.setCurrencyID(currencyCode);
        type.setValue(rep.getImporteDocumentoSunat());
        return type;
    }

    private static ExchangeRateType toExchangeRateType(DocumentoSunatLineRepresentation rep, String currencyCode) {
        ExchangeRateType type = new ExchangeRateType();
        if (rep.getTipoCambio() != null) {
            type.setCalculationRate(rep.getTipoCambio());
            if (rep.getMonedaDocumentoRelacionado() != null) {
                type.setSourceCurrencyCode(rep.getMonedaDocumentoRelacionado());
            }
            type.setTargetCurrencyCode(currencyCode);
            if (rep.getFechaCambio() != null) {
                type.setDate(toGregorianCalendar(DateUtils.asLocalDate(rep.getFechaCambio())));
            }
        } else {
            type.setCalculationRate(new BigDecimal(1));
            type.setSourceCurrencyCode(currencyCode);
            type.setTargetCurrencyCode(currencyCode);
            type.setDate(toGregorianCalendar(LocalDate.now()));
        }
        return type;
    }

    private static PaymentType toPaymentType(DocumentoSunatLineRepresentation rep) {
        PaymentType type = new PaymentType();
        if (rep.getNumeroPago() != null) {
            type.setID(rep.getNumeroPago());
        }
        type.setPaidAmount(toPaidAmountType(rep));
        if (rep.getFechaDocumentoRelacionado() != null) {
            type.setPaidDate(toGregorianCalendar(DateUtils.asLocalDate(rep.getFechaDocumentoRelacionado())));
        }
        return type;
    }

    private static PaidAmountType toPaidAmountType(DocumentoSunatLineRepresentation rep) {
        PaidAmountType type = new PaidAmountType();
        if (rep.getMonedaDocumentoRelacionado() != null) {
            type.setCurrencyID(rep.getMonedaDocumentoRelacionado());
        }
        if (rep.getPagoDocumentoSunat() != null) {
            type.setValue(rep.getPagoDocumentoSunat());
        }
        return type;
    }

    private static IDType toIDType(DocumentoSunatLineRepresentation rep) {
        IDType type = new IDType();
        if (rep.getTipoDocumentoRelacionado() != null) {
            type.setSchemeID(rep.getTipoDocumentoRelacionado());
        }
        if (rep.getNumeroDocumentoRelacionado() != null) {
            type.setValue(rep.getNumeroDocumentoRelacionado().toUpperCase());
        }
        return type;
    }

    private static PartyType toReceiverPartyType(DocumentoSunatRepresentation rep) {
        PartyType type = new PartyType();
        type.setPartyIdentification(toPartyIdentificationType(rep));
        type.setPartyName(toPartyNameType(rep));
        type.setPostalAddress(toPostalAddressType(rep));
        type.setPartyLegalEntity(Arrays.asList(toPartyLegalEntityType(rep)));
        type.setContact(toContactType(rep));
        return type;
    }

    private static ContactType toContactType(DocumentoSunatRepresentation rep) {
        ContactType type = new ContactType();
        if (rep.getEntidadEmail() != null) {
            type.setElectronicMail(rep.getEntidadEmail());
        }
        return type;
    }

    private static PartyLegalEntityType toPartyLegalEntityType(DocumentoSunatRepresentation rep) {
        PartyLegalEntityType type = new PartyLegalEntityType();
        if (rep.getEntidadDenominacion() != null) {
            type.setRegistrationName(rep.getEntidadDenominacion());
        }
        return type;
    }

    private static AddressType toPostalAddressType(DocumentoSunatRepresentation rep) {
        if (rep.getEntidadDireccion() != null) {
            AddressType type = new AddressType();
            type.setStreetName(rep.getEntidadDireccion());
            return type;
        }
        return null;
    }

    private static List<PartyNameType> toPartyNameType(DocumentoSunatRepresentation rep) {
        List<PartyNameType> list = new ArrayList<>();
        PartyNameType type = new PartyNameType();
        if (rep.getEntidadDenominacion() != null) {
            type.setName(rep.getEntidadDenominacion());
        }
        list.add(type);
        return list;
    }

    private static List<PartyIdentificationType> toPartyIdentificationType(DocumentoSunatRepresentation rep) {
        List<PartyIdentificationType> list = new ArrayList<>();
        PartyIdentificationType type = new PartyIdentificationType();
        type.setID(toIDType(rep));
        list.add(type);
        return list;
    }

    private static IDType toIDType(DocumentoSunatRepresentation rep) {
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
            type.setID(UblSunatConfiguration.ID_SIGN.getCodigo() + organization.getName().toUpperCase().replaceAll("\\s", ""));
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
            type.setURI(UblSunatConfiguration.URI_SIGN.getCodigo() + organization.getName().toUpperCase().replaceAll("\\s", ""));
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

    public static SummaryDocumentsType toSummaryDocumentType(OpenfactSession session, OrganizationModel organization, SummaryRepresentation rep) {
        SummaryDocumentsType type = new SummaryDocumentsType();

        // Firma ficticia
        UBLExtensionsType ublExtensionsType = new UBLExtensionsType();
        UBLExtensionType ublExtensionType = new UBLExtensionType();
        ExtensionContentType extensionContentType = new ExtensionContentType();
        extensionContentType.setAny(SunatSignerUtils.getSignToElement(session, organization));
        ublExtensionType.setExtensionContent(extensionContentType);
        ublExtensionsType.setUBLExtension(new ArrayList<>(Arrays.asList(ublExtensionType)));
        type.setUblExtensions(ublExtensionsType);

        type.setUblVersionID(UblSunatConfiguration.VERSION_ID.getCodigo());
        type.setCustomizationID(UblSunatConfiguration.CUSTOMIZATION_ID10.getCodigo());
        if (rep.getFechaDeReferencia() != null) {
            type.setReferenceDateTime(toGregorianCalendar(rep.getFechaDeReferencia().toLocalDate()));
        }
        if (rep.getFechaDeEmision() != null) {
            type.setIssueDate(toGregorianCalendar(rep.getFechaDeEmision().toLocalDate()));
        } else {
            type.setIssueDate(toGregorianCalendar(LocalDate.now()));
        }
//        type.setId(rep.getSerie() + UblSunatConfiguration.ID_SEPARATOR.getCodigo() + rep.getNumero());
        type.addSignature(toSignatureType(organization));
        type.setAccountingSupplierParty(toAccountingSupplierPartyType(organization));

        if (rep.getLine() != null && !rep.getLine().isEmpty()) {
            for (int i = 0; i < rep.getLine().size(); i++) {
                SummaryLineRepresentation summaryLineRepresentation = rep.getLine().get(i);

                SummaryDocumentsLineType lineType = new SummaryDocumentsLineType();
                lineType.setLineID(String.valueOf(i + 1));

                if (summaryLineRepresentation.getCodigoDocumento() != null) {
                    lineType.setDocumentTypeCode(summaryLineRepresentation.getCodigoDocumento());
                }
                if (summaryLineRepresentation.getSerieDocumento() != null) {
                    lineType.setDocumentSerialID(summaryLineRepresentation.getSerieDocumento());
                }
                if (summaryLineRepresentation.getNumeroInicioDocumento() != null) {
                    lineType.setStartDocumentNumberID(summaryLineRepresentation.getNumeroInicioDocumento());
                }
                if (summaryLineRepresentation.getNumeroFinDocumento() != null) {
                    lineType.setEndDocumentNumberID(summaryLineRepresentation.getNumeroFinDocumento());
                }
                if (summaryLineRepresentation.getTotalAmount() != null && summaryLineRepresentation.getMoneda() != null) {
                    lineType.setTotalAmount(summaryLineRepresentation.getTotalAmount(), summaryLineRepresentation.getMoneda());
                }
                if (summaryLineRepresentation.getPayment() != null) {
                    for (BillingPaymentRepresentation payment : summaryLineRepresentation.getPayment()) {
                        lineType.addBillingPayment(toBillingPaymentType(payment, summaryLineRepresentation.getMoneda()));
                    }
                }
                lineType.addAllowanceCharge(toAllowanceChargeType(summaryLineRepresentation, summaryLineRepresentation.getMoneda()));

                if (summaryLineRepresentation.getTaxTotal() != null && !summaryLineRepresentation.getTaxTotal().isEmpty()) {
                    for (TaxTotalRepresentation tax : summaryLineRepresentation.getTaxTotal()) {
                        lineType.addTaxTotal(toTaxTotalType(tax, summaryLineRepresentation.getMoneda()));
                    }
                }

                type.addSummaryDocumentsLine(lineType);
            }
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

    private static AllowanceChargeType toAllowanceChargeType(SummaryLineRepresentation rep, String moneda) {
        AllowanceChargeType type = new AllowanceChargeType();
        type.setChargeIndicator(rep.isChargeIndicator());
        if (rep.getChargeAmount() != null) {
            AmountType amountType = new AmountType();
            amountType.setValue(rep.getChargeAmount());
            amountType.setCurrencyID(moneda);
            type.setAmount(amountType);
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

    public static VoidedDocumentsType toVoidedDocumentType(OpenfactSession session, OrganizationModel organization, VoidedRepresentation rep) {
        VoidedDocumentsType type = new VoidedDocumentsType();
        UBLExtensionsType ublExtensionsType = new UBLExtensionsType();
        UBLExtensionType ublExtensionType = new UBLExtensionType();
        ExtensionContentType extensionContentType = new ExtensionContentType();
        type.setUBLVersionID(UblSunatConfiguration.VERSION_ID.getCodigo());
        type.setCustomizationID(UblSunatConfiguration.CUSTOMIZATION_ID10.getCodigo());
        if (rep.getSerieDocumento() != null && rep.getNumeroDocumento() != null) {
            type.setID(rep.getSerieDocumento() + UblSunatConfiguration.ID_SEPARATOR.getCodigo() + rep.getNumeroDocumento());
        }
        if (rep.getFechaDeEmision() != null) {
            type.setIssueDate(toGregorianCalendar(DateUtils.asLocalDateTime(rep.getFechaDeEmision()).toLocalDate()));
            type.setReferenceDate(toGregorianCalendar(DateUtils.asLocalDateTime(rep.getFechaDeEmision()).toLocalDate()));
        } else {
            type.setIssueDate(toGregorianCalendar(LocalDate.now()));
            type.setReferenceDate(toGregorianCalendar(LocalDate.now()));
        }

        if (rep.getObservaciones() != null) {
            type.addNote(rep.getObservaciones());
        }
        type.addSignature(toSignatureType(organization));
        type.setAccountingSupplierParty(toSupplierParty(organization));
        extensionContentType.setAny(SunatSignerUtils.getSignToElement(session, organization));
        ublExtensionType.setExtensionContent(extensionContentType);
        ublExtensionsType.setUBLExtension(new ArrayList<>(Arrays.asList(ublExtensionType)));
        type.setUBLExtensions(ublExtensionsType);
        if (rep.getDetalle() != null) {
            for (int i = 0; i < rep.getDetalle().size(); i++) {
                VoidedLineRepresentation lineRepresentation = rep.getDetalle().get(i);
                VoidedDocumentsLineType voidedDocumentsLineType = new VoidedDocumentsLineType();

                voidedDocumentsLineType.setLineID(String.valueOf(i + 1));
                if (lineRepresentation.getTipoDocumentoRelacionado() != null) {
                    voidedDocumentsLineType.setDocumentTypeCode(lineRepresentation.getTipoDocumentoRelacionado());
                }
                if (lineRepresentation.getNumeroDocumentoRelacionado() != null) {
                    String[] splits = (lineRepresentation.getNumeroDocumentoRelacionado().toUpperCase()).split("-");
                    voidedDocumentsLineType.setDocumentSerialID(splits[0]);
                    voidedDocumentsLineType.setDocumentNumberID(splits[1]);
                }
                if (lineRepresentation.getDescripcionDocumentoRelacionado() != null) {
                    voidedDocumentsLineType.setVoidReasonDescription(lineRepresentation.getDescripcionDocumentoRelacionado());
                }

                type.addVoidedDocumentsLine(voidedDocumentsLineType);
            }
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
        if (organization.getAdditionalAccountId() != null) {
            List<AdditionalAccountIDType> additionalAccountIDType = new ArrayList<>();
            additionalAccountIDType.add(new AdditionalAccountIDType(organization.getAdditionalAccountId()));
            supplierPartyType.setAdditionalAccountID(additionalAccountIDType);
        }
        if (organization.getAssignedIdentificationId() != null) {
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

            addressType.setID(organization.getPostalAddressId());
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

    public static MonetaryTotalType toLegalMonetaryTotalType(DocumentRepresentation rep) {
        MonetaryTotalType monetaryTotalType = new MonetaryTotalType();
        if (rep.getTotal() != null) {
            PayableAmountType payableAmountType = new PayableAmountType(rep.getTotal());
            payableAmountType.setCurrencyID(rep.getMoneda());
            monetaryTotalType.setPayableAmount(payableAmountType);
        }
        if (rep.getTotalOtrosCargos() != null && rep.getTotalOtrosCargos().compareTo(BigDecimal.ZERO) > 0) {
            ChargeTotalAmountType chargeTotalAmountType = new ChargeTotalAmountType(rep.getTotalOtrosCargos());
            chargeTotalAmountType.setCurrencyID(rep.getMoneda());
            monetaryTotalType.setChargeTotalAmount(chargeTotalAmountType);
        }
        if (rep.getDescuentoGlobal() != null && rep.getDescuentoGlobal().compareTo(BigDecimal.ZERO) > 0) {
            AllowanceTotalAmountType allowanceTotalAmountType = new AllowanceTotalAmountType(rep.getDescuentoGlobal());
            allowanceTotalAmountType.setCurrencyID(rep.getMoneda());
            monetaryTotalType.setAllowanceTotalAmount(allowanceTotalAmountType);
        }

        return monetaryTotalType;
    }

    public static TaxTotalType toTaxTotalIGV(DocumentRepresentation rep) {
        TaxTotalType taxTotalType = new TaxTotalType();

        // Tax amount
        TaxAmountType taxAmountType1 = new TaxAmountType(rep.getTotalIgv());
        taxAmountType1.setCurrencyID(rep.getMoneda());
        taxTotalType.setTaxAmount(taxAmountType1);

        // Tax Subtotal
        TaxSubtotalType taxSubtotalType = new TaxSubtotalType();

        TaxAmountType taxAmountType2 = new TaxAmountType(rep.getTotalIgv());
        taxAmountType2.setCurrencyID(rep.getMoneda());
        taxSubtotalType.setTaxAmount(taxAmountType2);

        TaxCategoryType taxCategoryType = new TaxCategoryType();
        TaxSchemeType taxSchemeType = new TaxSchemeType();

        taxSchemeType.setID(TipoTributo.IGV.getId());
        taxSchemeType.setName(TipoTributo.IGV.toString());
        taxSchemeType.setTaxTypeCode(TipoTributo.IGV.getCodigo());
        taxCategoryType.setTaxScheme(taxSchemeType);
        taxSubtotalType.setTaxCategory(taxCategoryType);

        taxTotalType.setTaxSubtotal(Arrays.asList(taxSubtotalType));

        return taxTotalType;
    }

}
