package m1.productos.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoMovimiento {
    AGREGAR,
    RETIRAR;

    @JsonCreator
    public static TipoMovimiento fromString(String value) {
        return TipoMovimiento.valueOf(value.trim().toUpperCase());
    }
}