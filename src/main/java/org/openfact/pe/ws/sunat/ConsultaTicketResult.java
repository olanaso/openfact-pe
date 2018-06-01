package org.openfact.pe.ws.sunat;

public enum  ConsultaTicketResult {

    PROCESO_CORRECTAMENTE(0, "Procesó correctamente"),
    EN_PROCESO(98, "En proceso"),
    PROCESO_CON_ERRORES(99, "Procesó con errores"),
    ERROR(-1, "Error");

    private final int codigo;
    private final String mensaje;

    ConsultaTicketResult(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

}
