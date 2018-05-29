package org.openfact.pe.ubl.data.xml.entity;

import org.openfact.ubl.data.xml.annotations.JsonWrapper;
import org.openfact.ubl.data.xml.annotations.SimpleKey;
import org.openfact.ubl.data.xml.mappers.BigdecimalMapper;
import org.openfact.ubl.data.xml.mappers.StringMapper;

import java.math.BigDecimal;

@JsonWrapper
public class XmlSUNATPerceptionDocumentReference {

    @SimpleKey(key = {"ID", "content"}, mapper = StringMapper.class)
    private String sunat_id;

    @SimpleKey(key = {"IssueDate"}, mapper = StringMapper.class)
    private String sunat_issue_date;

    @SimpleKey(key = {"TotalInvoiceAmount", "content"}, mapper = BigdecimalMapper.class)
    private BigDecimal sunat_total_invoice_amount;

    @SimpleKey(key = {"TotalInvoiceAmount", "currencyID"}, mapper = StringMapper.class)
    private String sunat_total_invoice_currency_code;

    @SimpleKey(key = {"Payment", "ID"}, mapper = StringMapper.class)
    private String sunat_payment_id;

    @SimpleKey(key = {"Payment", "PaidAmount", "content"}, mapper = BigdecimalMapper.class)
    private BigDecimal sunat_payment_amount;

    @SimpleKey(key = {"Payment", "PaidAmount", "currencyID"}, mapper = StringMapper.class)
    private String sunat_payment_currency_code;

    @SimpleKey(key = {"Payment", "PaidDate"}, mapper = StringMapper.class)
    private String sunat_paid_date;

    @SimpleKey(key = {"SUNATPerceptionInformation", "SUNATPerceptionAmount", "content"}, mapper = BigdecimalMapper.class)
    private BigDecimal sunat_perception_amount;

    @SimpleKey(key = {"SUNATPerceptionInformation", "SUNATPerceptionDate"}, mapper = StringMapper.class)
    private String sunat_perception_date;

    @SimpleKey(key = {"SUNATPerceptionInformation", "SUNATNetTotalCashed", "content"}, mapper = BigdecimalMapper.class)
    private BigDecimal sunat_net_total_cashed;

    @SimpleKey(key = {"SUNATPerceptionInformation", "ExchangeRate", "SourceCurrencyCode"}, mapper = StringMapper.class)
    private String sunat_exchange_source_currency_code;

    @SimpleKey(key = {"SUNATPerceptionInformation", "ExchangeRate", "TargetCurrencyCode"}, mapper = StringMapper.class)
    private String sunat_exchange_target_currency_code;

    @SimpleKey(key = {"SUNATPerceptionInformation", "ExchangeRate", "CalculationRate"}, mapper = BigdecimalMapper.class)
    private BigDecimal sunat_exchange_calculation_rate;

    @SimpleKey(key = {"SUNATPerceptionInformation", "ExchangeRate", "Date"}, mapper = StringMapper.class)
    private String sunat_exchange_date;

    public String getSunat_id() {
        return sunat_id;
    }

    public void setSunat_id(String sunat_id) {
        this.sunat_id = sunat_id;
    }

    public String getSunat_issue_date() {
        return sunat_issue_date;
    }

    public void setSunat_issue_date(String sunat_issue_date) {
        this.sunat_issue_date = sunat_issue_date;
    }

    public BigDecimal getSunat_total_invoice_amount() {
        return sunat_total_invoice_amount;
    }

    public void setSunat_total_invoice_amount(BigDecimal sunat_total_invoice_amount) {
        this.sunat_total_invoice_amount = sunat_total_invoice_amount;
    }

    public String getSunat_total_invoice_currency_code() {
        return sunat_total_invoice_currency_code;
    }

    public void setSunat_total_invoice_currency_code(String sunat_total_invoice_currency_code) {
        this.sunat_total_invoice_currency_code = sunat_total_invoice_currency_code;
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

    public String getSunat_payment_currency_code() {
        return sunat_payment_currency_code;
    }

    public void setSunat_payment_currency_code(String sunat_payment_currency_code) {
        this.sunat_payment_currency_code = sunat_payment_currency_code;
    }

    public String getSunat_paid_date() {
        return sunat_paid_date;
    }

    public void setSunat_paid_date(String sunat_paid_date) {
        this.sunat_paid_date = sunat_paid_date;
    }

    public BigDecimal getSunat_perception_amount() {
        return sunat_perception_amount;
    }

    public void setSunat_perception_amount(BigDecimal sunat_perception_amount) {
        this.sunat_perception_amount = sunat_perception_amount;
    }

    public String getSunat_perception_date() {
        return sunat_perception_date;
    }

    public void setSunat_perception_date(String sunat_perception_date) {
        this.sunat_perception_date = sunat_perception_date;
    }

    public BigDecimal getSunat_net_total_cashed() {
        return sunat_net_total_cashed;
    }

    public void setSunat_net_total_cashed(BigDecimal sunat_net_total_cashed) {
        this.sunat_net_total_cashed = sunat_net_total_cashed;
    }

    public String getSunat_exchange_source_currency_code() {
        return sunat_exchange_source_currency_code;
    }

    public void setSunat_exchange_source_currency_code(String sunat_exchange_source_currency_code) {
        this.sunat_exchange_source_currency_code = sunat_exchange_source_currency_code;
    }

    public String getSunat_exchange_target_currency_code() {
        return sunat_exchange_target_currency_code;
    }

    public void setSunat_exchange_target_currency_code(String sunat_exchange_target_currency_code) {
        this.sunat_exchange_target_currency_code = sunat_exchange_target_currency_code;
    }

    public BigDecimal getSunat_exchange_calculation_rate() {
        return sunat_exchange_calculation_rate;
    }

    public void setSunat_exchange_calculation_rate(BigDecimal sunat_exchange_calculation_rate) {
        this.sunat_exchange_calculation_rate = sunat_exchange_calculation_rate;
    }

    public String getSunat_exchange_date() {
        return sunat_exchange_date;
    }

    public void setSunat_exchange_date(String sunat_exchange_date) {
        this.sunat_exchange_date = sunat_exchange_date;
    }
}
