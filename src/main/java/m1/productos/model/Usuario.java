package m1.productos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cargo cargo;

    public Object getNombre() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getCargo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNombre(Object nombre2) {
        throw new UnsupportedOperationException("Unimplemented method 'setNombre'");
    }

    public void setCargo(Object cargo2) {
        throw new UnsupportedOperationException("Unimplemented method 'setCargo'");
    }


}