package org.openfact.pe.utils.finance;

import org.junit.Assert;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class MoneyConvertersTest {

    @org.junit.Test
    public void asWords() throws Exception {
        String txt1 = MoneyConverters.SPANISH_BANKING_MONEY_VALUE.asWords(new BigDecimal("1"));
        String txt2 = MoneyConverters.SPANISH_BANKING_MONEY_VALUE.asWords(new BigDecimal("12"));
        String txt3 = MoneyConverters.SPANISH_BANKING_MONEY_VALUE.asWords(new BigDecimal("13"));
        String txt4 = MoneyConverters.SPANISH_BANKING_MONEY_VALUE.asWords(new BigDecimal("1142046.75"));
        String txt5 = MoneyConverters.SPANISH_BANKING_MONEY_VALUE.asWords(new BigDecimal("289900.91"));

        System.out.println(txt4.toUpperCase());
        System.out.println(txt5.toUpperCase());

        Assert.assertNotNull(txt5);
    }

}