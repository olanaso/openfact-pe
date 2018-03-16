package org.openfact.pe.report.scriptlet;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import org.openfact.pe.utils.finance.MoneyConverters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NumberOpenfactFormats extends JRDefaultScriptlet {

    public String asWords(BigDecimal number) {
        if (number == null) {
            return null;
        }

        return MoneyConverters.SPANISH_BANKING_MONEY_VALUE.asWords(number).toUpperCase();
    }

}
