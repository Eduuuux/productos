package m1.productos.model;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reporte")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Reporte {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id_reporte;

    @Column(nullable = false)
    private String nombreReporte;

    @Column(nullable = false)
    private LocalDate fechaReporte;


    @ManyToMany
    @JoinTable(
        name = "reporte_producto", // nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "reporte_id"), // FK a Reporte
        inverseJoinColumns = @JoinColumn(name = "producto_id") // FK a Producto
    )
    private Set<Producto> productos;
}
