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
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Producto {
    

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    @Column(name= "nombre_producto",nullable = false)
    private String nombreProducto;

    @Column(name= "Tipo",nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoProducto tipoProducto;

    @Column(name= "valor",nullable = false, length = 10)
    private int valor;

    @Column(name= "stock",nullable = false, length = 10)
    private int stock;

    @Column(name= "reseña",nullable = false)
    private String resena;

    @Column(name = "fecha_registro", nullable = false)
    private String fechaRegistro;

    @Column(name = "usuario_registrador",nullable = false)
    private int idUsuario;
    
    


}
