package m1.productos.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "movimiento_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "tipo_movimiento", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento; // AGREGAR, RETIRAR

    @Column(nullable = false)
    private int cantidad;
    
    @Column(nullable = false )
    private LocalDateTime fecha;

    @Column(name = "usuario_id")
    private int usuarioId; // o relación @ManyToOne si estás compartiendo modelo de usuario

    @Column(name = "comentario" ,length = 255)
    private String comentario; // opcional: por qué se hizo el cambio

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
}
