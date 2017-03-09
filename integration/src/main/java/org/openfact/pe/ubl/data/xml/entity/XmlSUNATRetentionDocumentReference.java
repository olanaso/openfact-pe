package org.openfact.pe.ubl.data.xml.entity;

import org.openfact.ubl.data.xml.annotations.JsonWrapper;
import org.openfact.ubl.data.xml.annotations.SimpleKey;
import org.openfact.ubl.data.xml.mappers.BigdecimalMapper;
import org.openfact.ubl.data.xml.mappers.StringMapper;

import java.math.BigDecimal;

@JsonWrapper
public class XmlSUNATRetentionDocumentReference {

    @SimpleKey(key = {"ID", "content"}, mapper = StringMapper.class)
    private String sunat_id;

    @SimpleKey(key = {"TotalInvoiceAmount", "content"}, mapper = BigdecimalMapper.class)
    private BigDecimal sunat_total_invoice_amount;

    @SimpleKey(key = {"Payment", "ID"}, mapper = StringMapper.class)
    private String sunat_payment_id;

    @SimpleKey(key = {"Payment", "PaidAmount", "content"}, mapper = BigdecimalMapper.class)
    private BigDecimal sunat_payment_amount;


    @SimpleKey(key = {"SUNATRetentionInformation", "SUNATRetentionAmount", "content"}, mapper = BigdecimalMapper.class)
    private BigDecimal sunat_retention_amount;

    @SimpleKey(key = {"SUNATRetentionInformation", "SUNATNetTotalCashed", "content"}, mapper = BigdecimalMapper.class)
    private BigDecimal sunat_net_total_cashed;

    public String getSunat_id() {
        return sunat_id;
    }

    public void setSunat_id(String sunat_id) {
        this.sunat_id = sunat_id;
    }

    public BigDecimal getSunat_total_invoice_amount() {
        return sunat_total_invoice_amount;
    }

    public void setSunat_total_invoice_amount(BigDecimal sunat_total_invoice_amount) {
        this.sunat_total_invoice_amount = sunat_total_invoice_amount;
    }

    public String getSunat_payment_id() {
        return sunat_payment_id;
    }

    public void setSunat_payment_id(String sunat_payment_id) {
        this.sunat_payment_id = sunat_payment_id;
    }

    public BigDecimal getSunat_payment_amount() {
        return sunat_payment_amount;
    }

    public void setSunat_payment_amount(BigDecimal sunat_payment_amount) {
        this.sunat_payment_amount = sunat_payment_amount;
    }

    public BigDecimal getSunat_retention_amount() {
        return sunat_retention_amount;
    }

    public void setSunat_retention_amount(BigDecimal sunat_retention_amount) {
        this.sunat_retention_amount = sunat_retention_amount;
    }

    public BigDecimal getSunat_net_total_cashed() {
        return sunat_net_total_cashed;
    }

    public void setSunat_net_total_cashed(BigDecimal sunat_net_total_cashed) {
        this.sunat_net_total_cashed = sunat_net_total_cashed;
    }
}
