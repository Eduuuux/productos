package m1.productos.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoProducto {
    HOMBRE,
    UNISEX,
    MUJER;

    @JsonCreator
    public static TipoProducto fromString(String value) {
        return TipoProducto.valueOf(value.trim().toUpperCase());
    }
}