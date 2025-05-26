package m1.productos.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Producto {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombreProducto;

    @Column(nullable = false)
    private String tipoProducto;

    @Column(nullable = false, length = 10)
    private int valor;

    @Column(nullable = false, length = 10)
    private int stock;

    @Column(nullable = false)
    private String resena;

    @ManyToMany(mappedBy = "productos")
    private Set<Reporte> reportes;
}

