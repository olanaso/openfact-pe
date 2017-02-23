package org.openfact.pe.services.report.scriptlet;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import org.openfact.pe.models.enums.TipoComprobante;
import org.openfact.pe.models.enums.TipoInvoice;

public class SunatCodes extends JRDefaultScriptlet {

    public String tipoComprobanteFromCode(String codigo) {
        TipoComprobante tipoComprobante = TipoComprobante.getFromCode(codigo);
        if (tipoComprobante != null) {
            return tipoComprobante.getDenominacion();
        } else {
            TipoInvoice tipoInvoice = TipoInvoice.getFromCode(codigo);
            if (tipoInvoice != null) {
                return tipoInvoice.getDenominacion();
            }
        }
        return codigo;
    }

}
