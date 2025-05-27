package m1.productos.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Cargo {
    EMPLEADO,
    GERENTE;

    @JsonCreator
    public static Cargo fromString(String value) {
        return Cargo.valueOf(value.trim().toUpperCase());
    }
}