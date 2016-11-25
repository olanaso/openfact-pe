package org.openfact.pe.models.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import org.openfact.common.finance.MoneyConverters;
import org.openfact.models.ModelException;
import org.openfact.models.OrganizationModel;
import org.openfact.pe.constants.CodigoConceptosTributarios;
import org.openfact.pe.constants.CodigoElementosAdicionalesComprobante;
import org.openfact.pe.model.types.PerceptionType;
import org.openfact.pe.model.types.RetentionType;
import org.openfact.pe.model.types.SummaryDocumentsType;
import org.openfact.pe.model.types.VoidedDocumentsType;
import org.openfact.pe.models.types.common.AdditionalInformationTypeSunatAgg;
import org.openfact.pe.models.types.common.AdditionalMonetaryTotalType;
import org.openfact.pe.models.types.common.AdditionalPropertyType;
import org.openfact.pe.models.types.common.InvoiceFactory;
import org.openfact.pe.representations.idm.DocumentRepresentation;
import org.openfact.pe.representations.idm.LineRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ContactType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CustomerPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.MonetaryTotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PriceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PricingReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AdditionalAccountIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InvoicedQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.LineExtensionAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.NoteType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PayableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.ValueType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.UBLExtensionsType;
import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import oasis.names.specification.ubl.schema.xsd.debitnote_21.DebitNoteType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public class SunatRepresentationToType {

    public static XMLGregorianCalendar toGregorianCalendar(LocalDate date) {
        GregorianCalendar gcal = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (DatatypeConfigurationException e) {
            throw new ModelException(e);
        }
    }

    public static InvoiceType toInvoiceType(OrganizationModel organization, DocumentRepresentation rep) {
        InvoiceType type = new InvoiceType();

        // Supplier
        type.setAccountingSupplierParty(toSupplierParty(organization));
        
        //Customer
        type.setAccountingCustomerParty(toCustomerPartyType(rep));

        if (rep.getNumero() != null && rep.getSerie() != null) {
            type.setID(rep.getNumero() + "-" + rep.getSerie());
        }
        if (rep.getTipo() != null) {
            type.setInvoiceTypeCode(rep.getTipo());
        }
        if (rep.getMoneda() != null) {
            type.setDocumentCurrencyCode(rep.getMoneda());
        }
        if (rep.getTipoDeCambio() != null) {
        }
        if (rep.getObservaciones() != null) {
            List<NoteType> noteTypes = new ArrayList<>();
            noteTypes.add(new NoteType(rep.getObservaciones()));
            type.setNote(noteTypes);
        }

        // Date
        if (rep.getFechaDeEmision() != null) {
            type.setIssueDate(toGregorianCalendar(rep.getFechaDeEmision().toLocalDate()));
        }
        if (rep.getFechaDeVencimiento() != null) {
            type.setDueDate(toGregorianCalendar(rep.getFechaDeVencimiento().toLocalDate()));
        }

        // Legal monetary total
        MonetaryTotalType monetaryTotalType = new MonetaryTotalType();
        if (rep.getTotal() != null) {
            monetaryTotalType.setPayableAmount(rep.getTotal());
        }
        if (rep.getTotalOtrosCargos() != null) {
            monetaryTotalType.setChargeTotalAmount(rep.getTotalOtrosCargos());
        }
        type.setLegalMonetaryTotal(monetaryTotalType);
        
        // IGV
        List<TaxTotalType> taxTotalTypes = new ArrayList<>();
        if (rep.getTotalIgv() != null) {
            TaxTotalType taxTotalType = new TaxTotalType();
            taxTotalType.setTaxAmount(rep.getTotalIgv());

            TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
            taxSubtotalType.setTaxableAmount(rep.getTotalIgv());
            taxTotalType.setTaxSubtotal(new ArrayList<>(Arrays.asList(taxSubtotalType)));

            taxTotalTypes.add(taxTotalType);
        }
        type.setTaxTotal(taxTotalTypes);        

        // Extensions
        UBLExtensionsType ublExtensionsType = new UBLExtensionsType();
        UBLExtensionType ublExtensionType = new UBLExtensionType();
        ExtensionContentType extensionContentType = new ExtensionContentType();

        AdditionalMonetaryTotalType gravado = new AdditionalMonetaryTotalType();
        if (rep.getTotalGravada() != null) {
            gravado.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRAVADAS.getCodigo()));
            gravado.setPayableAmount(new PayableAmountType(rep.getTotalGravada()));
        }
        AdditionalMonetaryTotalType inafecto = new AdditionalMonetaryTotalType();
        if (rep.getTotalInafecta() != null) {
            inafecto.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_INAFECTAS.getCodigo()));
            inafecto.setPayableAmount(new PayableAmountType(rep.getTotalInafecta()));
        }
        AdditionalMonetaryTotalType exonerado = new AdditionalMonetaryTotalType();
        if (rep.getTotalExonerada() != null) {
            exonerado.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_EXONERADAS.getCodigo()));
            exonerado.setPayableAmount(new PayableAmountType(rep.getTotalExonerada()));
        }
        AdditionalMonetaryTotalType gratuito = new AdditionalMonetaryTotalType();
        if (rep.getTotalGratuita() != null) {
            gratuito.setID(new IDType(CodigoConceptosTributarios.TOTAL_VALOR_VENTA_OPERACIONES_GRATUITAS.getCodigo()));
            gratuito.setPayableAmount(new PayableAmountType(rep.getTotalGratuita()));
        }
        AdditionalPropertyType additionalProperty = generateAdditionalInformationSunatTotal(rep.getTotal());

        AdditionalInformationTypeSunatAgg additionalInformation = new AdditionalInformationTypeSunatAgg();
        additionalInformation.getAdditionalMonetaryTotal().addAll(Arrays.asList(gravado, inafecto, exonerado, gratuito));
        additionalInformation.getAdditionalProperty().add(additionalProperty);

        extensionContentType.setAny(generateElement(additionalInformation));
        ublExtensionType.setExtensionContent(extensionContentType);
        ublExtensionsType.setUBLExtension(new ArrayList<>(Arrays.asList(ublExtensionType)));
        type.setUBLExtensions(ublExtensionsType);

        // Lines
        if (rep.getLines() != null) {
            type.setInvoiceLine(toInvoiceLineType(rep));
        }

        return type;
    }

    private static List<InvoiceLineType> toInvoiceLineType(DocumentRepresentation rep) {
        List<InvoiceLineType> invoiceLineTypes = new ArrayList<>();
        for (int i = 0; i < rep.getLines().size(); i++) {
            LineRepresentation lineRep = rep.getLines().get(i);
        
            InvoiceLineType invoiceLineType = new InvoiceLineType();
            
            // ID
            invoiceLineType.setID(new IDType(String.valueOf(i)));
            
            // Quantity
            InvoicedQuantityType invoicedQuantityType = new InvoicedQuantityType(lineRep.getCantidad());
            if(lineRep.getUnitCode() != null) {
                invoicedQuantityType.setUnitCode(lineRep.getUnitCode());
            }
            invoiceLineType.setInvoicedQuantity(invoicedQuantityType);
            
            // Subtotal
            LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType();
            lineExtensionAmountType.setCurrencyID(rep.getMoneda());
            lineExtensionAmountType.setValue(lineRep.getSubtotal());
            invoiceLineType.setLineExtensionAmount(lineExtensionAmountType);
            
            // Precio y valor unitario
            PricingReferenceType pricingReferenceType = new PricingReferenceType();
            PriceType priceType = new PriceType();
            priceType.setPriceAmount(lineRep.getPrecioUnitario());
            pricingReferenceType.setAlternativeConditionPrice(Arrays.asList(priceType));
            invoiceLineType.setPricingReference(pricingReferenceType);
        }
        return invoiceLineTypes;
    }

    public static CreditNoteType toCreditNoteType(DocumentRepresentation rep) {
        // TODO Auto-generated method stub
        return null;
    }

    public static DebitNoteType toDebitNoteType(DocumentRepresentation rep) {
        // TODO Auto-generated method stub
        return null;
    }

    public static PerceptionType toPerceptionType(DocumentRepresentation rep) {
        PerceptionType type = new PerceptionType();

        if (rep.getDescuentoGlobal() != null) {

        }

        return type;
    }

    public static RetentionType toRetentionType(DocumentRepresentation rep) {
        return null;
    }

    public static SummaryDocumentsType toSummaryDocumentType(DocumentRepresentation rep) {
        return null;
    }

    public static VoidedDocumentsType toVoidedDocumentType(DocumentRepresentation rep) {
        return null;
    }

    public static SupplierPartyType toSupplierParty(OrganizationModel organization) {
        SupplierPartyType supplierPartyType = new SupplierPartyType();
        if (organization.getAssignedIdentificationId() != null) {
            List<AdditionalAccountIDType> additionalAccountIDType = new ArrayList<>();
            additionalAccountIDType.add(new AdditionalAccountIDType(organization.getAssignedIdentificationId()));
            supplierPartyType.setAdditionalAccountID(additionalAccountIDType);
        }
        if (organization.getAdditionalAccountId() != null) {
            supplierPartyType.setCustomerAssignedAccountID(organization.getAdditionalAccountId());
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
            if (organization.getStreetName() == null 
                    || organization.getCitySubdivisionName() == null
                    || organization.getCityName() == null || organization.getCountrySubentity() == null
                    || organization.getDistrict() == null
                    || organization.getCountryIdentificationCode() == null) {
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
