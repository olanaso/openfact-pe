package org.openfact.pe.spi;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;

import org.openfact.common.converts.DocumentUtils;
import org.openfact.common.converts.StringUtils;
import org.openfact.models.InvoiceModel;
import org.openfact.models.ModelException;
import org.openfact.models.OpenfactSession;
import org.openfact.models.OrganizationModel;
import org.openfact.models.ScrollModel;
import org.openfact.pe.constants.CodigoTipoDocumento;
import org.openfact.ubl.UBLIDGenerator;
import org.openfact.ubl.UBLInvoiceProvider;
import org.openfact.ubl.UBLReader;
import org.openfact.ubl.UBLSender;
import org.openfact.ubl.UBLWriter;
import org.w3c.dom.Document;

import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Writer;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriterSettings;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public class SunatUBLInvoiceProvider implements UBLInvoiceProvider {

    protected OpenfactSession session;

    public SunatUBLInvoiceProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public void close() {
    }

    @Override
    public UBLIDGenerator<InvoiceType> idGenerator() {
        return new UBLIDGenerator<InvoiceType>() {
            
            @Override
            public void close() {    
            }
            
            @Override
            public String generateID(OrganizationModel organization, InvoiceType invoiceType) {
                CodigoTipoDocumento invoiceCode;
                if (CodigoTipoDocumento.FACTURA.getCodigo().equals(invoiceType.getInvoiceTypeCode())) {
                    invoiceCode = CodigoTipoDocumento.FACTURA;
                } else if (CodigoTipoDocumento.BOLETA.getCodigo().equals(invoiceType.getInvoiceTypeCode())) {
                    invoiceCode = CodigoTipoDocumento.BOLETA;
                } else {
                    throw new ModelException("Invalid invoiceTypeCode");
                }
                
                InvoiceModel lastInvoice = null;
                ScrollModel<InvoiceModel> invoices = session.invoices().getInvoicesScroll(organization, false, 4, 2);
                Iterator<InvoiceModel> iterator = invoices.iterator();
               
                Pattern pattern = Pattern.compile(invoiceCode.getMask());        
                while (iterator.hasNext()) {
                    InvoiceModel invoice = iterator.next();
                    String documentId = invoice.getDocumentId();
                    
                    Matcher matcher = pattern.matcher(documentId);            
                    if(matcher.find()) {
                        lastInvoice = invoice;
                        break;
                    }
                }
                
                int series = 0;
                int number = 0;
                if (lastInvoice != null) {
                    String[] splits = lastInvoice.getDocumentId().split("-");
                    series = Integer.parseInt(splits[0].substring(1));
                    number = Integer.parseInt(splits[1]);
                }

                int nextNumber = SunatUtils.getNextNumber(number, 99_999_999);
                int nextSeries = SunatUtils.getNextSerie(series, number, 999, 99_999_999);
                StringBuilder documentId = new StringBuilder();
                documentId.append(invoiceCode.getMask().substring(0, 1));
                documentId.append(StringUtils.padLeft(String.valueOf(nextSeries), 3, "0"));
                documentId.append("-");
                documentId.append(StringUtils.padLeft(String.valueOf(nextNumber), 8, "0"));

                return documentId.toString();
            }
        };
    }

    @Override
    public UBLReader<InvoiceType> reader() {
        return new UBLReader<InvoiceType>() {

            @Override
            public void close() {        
            }

            @Override
            public InvoiceType read(byte[] bytes) {
                return UBL21Reader.invoice().read(bytes);
            }

            @Override
            public InvoiceType read(Document document) {
                return UBL21Reader.invoice().read(document);
            }
            
        };
    }

    @Override
    public UBLWriter<InvoiceType> writer() {
        return new UBLWriter<InvoiceType>() {

            @Override
            public void close() {
            }

            @Override
            public Document write(OrganizationModel organization, InvoiceType invoiceType, Map<String, String> attributes) {
                try {
                    MapBasedNamespaceContext mapBasedNamespace = SunatUtils.getBasedNamespaceContext("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2");
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    MicroWriter.writeToStream(UBL21Writer.invoice().getAsMicroDocument(invoiceType), out, new XMLWriterSettings().setNamespaceContext(mapBasedNamespace).setPutNamespaceContextPrefixesInRoot(true));

                    Document document = DocumentUtils.byteToDocument(out.toByteArray());
                    return document;
                } catch (DatatypeConfigurationException e) {
                    throw new ModelException(e);
                } catch (Exception e) {
                    throw new ModelException(e);
                }
            }

            @Override
            public Document write(OrganizationModel organization, InvoiceType t) {
                return write(organization, t, Collections.emptyMap());
            }
        };
    }

    @Override
    public UBLSender<InvoiceModel> sender() {
        // TODO Auto-generated method stub
        return null;
    }

}
