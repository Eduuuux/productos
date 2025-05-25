package m1.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import m1.productos.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    
}
