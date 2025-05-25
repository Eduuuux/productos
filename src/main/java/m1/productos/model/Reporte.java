package m1.productos.model;

import java.time.LocalDate;

import org.hibernate.annotations.Collate;
import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
}
