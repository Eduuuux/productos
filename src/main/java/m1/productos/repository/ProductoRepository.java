package m1.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import m1.productos.model.Producto;
import m1.productos.model.TipoProducto;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findAll();

    Producto findById(int id);

    Producto save(Producto producto);

    Producto findByNombreProducto(String nombreProducto);

    List<Producto> findByTipoProducto(TipoProducto tipoProducto);
}
