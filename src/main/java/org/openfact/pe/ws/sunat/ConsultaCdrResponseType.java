package org.openfact.pe.ws.sunat;

import java.util.Optional;
import java.util.stream.Stream;

public enum ConsultaCdrResponseType {

    UNO(true, "0001", "El comprobante existe y está aceptado"),
    DOS(true, "0002", "El comprobante existe pero está rechazado"),
    TRES(true, "0003", "El comprobante existe pero está de baja"),

    CUATRO(false, "0004", "Formato de RUC no es válido (debe de contener 11 caracteres numéricos)"),
    CINCO(false, "0005", "Formato del tipo de comprobante no es válido (debe de contener 2 caracteres)"),
    SEIS(false, "0006", "Formato de serie inválido (debe de contener 4 caracteres)"),
    SIETE(false, "0007", "El numero de comprobante debe de ser mayor que cero"),
    OCHO(false, "0008", "El número de RUC no está inscrito en los registros de la SUNAT"),
    NUEVE(false, "0009", "EL tipo de comprobante debe de ser (01, 07 o 08)"),
    DIEZ(false, "0010", "Sólo se puede consultar facturas, notas de crédito y debito electrónicas, cuya serie empieza con F"),
    ONCE(false, "0011", "El comprobante de pago electrónico no existe"),
    DOCE(false, "0012", "El comprobante de pago electrónico no le pertenece");

    private final boolean tipo;
    private final String codigo;
    private final String descripcion;

    ConsultaCdrResponseType(boolean tipo, String codigo, String descripcion) {

        this.tipo = tipo;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public static Optional<ConsultaCdrResponseType> searchByCodigo(String codigo) {
        return Stream.of(ConsultaCdrResponseType.values()).filter(p -> p.getCodigo().equals(codigo)).findFirst();
    }

    public boolean isTipo() {
        return tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
