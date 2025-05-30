package m1.productos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import m1.productos.model.Producto;
import m1.productos.model.TipoProducto;
import m1.productos.repository.ProductoRepository;

@Service
@Transactional

public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    
    
    public Producto findById(int id) {
        return productoRepository.findById(id);
    }
    
    
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }
    
    
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto findByNombreProducto(String nombreProducto) {
        return productoRepository.findByNombreProducto(nombreProducto);
    }

    public List<Producto> findByTipoProducto(TipoProducto tipoProducto) {
        return productoRepository.findByTipoProducto(tipoProducto);
    }

    public boolean deleteById(int id) {
        Producto producto = productoRepository.findById(id);
        if (producto != null) {
            productoRepository.delete(producto);
            return true; 
        }
        return false; 
    }

    
    public Producto updateById(int id, Producto producto) {
        Producto productoExistente = productoRepository.findById(id);
        if (productoExistente != null) {
            if (producto.getNombreProducto() != null) {
                productoExistente.setNombreProducto(producto.getNombreProducto());
            }
            if (producto.getTipoProducto() != null) {
                productoExistente.setTipoProducto(producto.getTipoProducto());
            }
            if (producto.getValor() >= 0) {
                productoExistente.setValor(producto.getValor());
            }
            if (producto.getStock() >= 0) {
                productoExistente.setStock(producto.getStock());
            }
            if (producto.getResena() != null) {
                productoExistente.setResena(producto.getResena());
            }

            return productoRepository.save(productoExistente);
        }
        return null;
    }
}
