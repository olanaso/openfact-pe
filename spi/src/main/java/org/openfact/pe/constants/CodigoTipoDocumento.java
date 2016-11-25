package org.openfact.pe.constants;

public enum CodigoTipoDocumento {

    FACTURA("01", "^[F]{1}\\d{1,3}[-]\\d{1,8}$", 13),
    BOLETA("03", "^[B]{1}\\d{1,3}[-]\\d{1,8}$", 13),
    NOTA_CREDITO("07", "%C%-%", 13),
    NOTA_DEBITO("08", "%D%-%", 13),
    GUIA_REMISION("09", "F%-%", 13),
    TICKET("12", "F%-%", 13),
    DOC_SBS("13", "F%-%", 13),
    DOC_AFP("18", "F%-%", 13),
    GUIA_TRANSPORTISTA("31", "F%-%", 13),
    SEAE("56", "F%-%", 13),
    
    PERCEPCION("01", "P%-%", 13),
    RETENCION("01", "R%-%", 13),
    RESUMEN_DIARIO("01", "S%-%", 13),
    BAJA("01", "B%-%", 13);
    
    private final String codigo;

    private String mask;
    private int lenght;
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getMask() {
        return mask;
    }

    public int getLenght() {
        return lenght;
    }

    private CodigoTipoDocumento(String codigo, String mask, int lenght) {
        this.codigo = codigo;
        this.mask = mask;
        this.lenght = lenght;
    }
    
}
