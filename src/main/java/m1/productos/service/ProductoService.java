package m1.productos.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;
import m1.productos.DTO.UsuarioDTO;
import m1.productos.model.MovimientoInventario;
import m1.productos.model.Producto;
import m1.productos.model.TipoMovimiento;
import m1.productos.model.TipoProducto;
import m1.productos.repository.MovimientoInventarioRepository;
import m1.productos.repository.ProductoRepository;

@Service
@Transactional

public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Autowired
    private RestTemplate restTemplate;

    
    public UsuarioDTO findUsuarioById(int id) {
        String url = "http://localhost:8081/usuario/" + id;
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }

    
    public Producto findById(int id) {
        return productoRepository.findById(id);
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto findByNombreProducto(String nombreProducto) {
        Producto producto = productoRepository.findByNombreProducto(nombreProducto);
        if (producto == null) {
            throw new RuntimeException("Producto con nombre '" + nombreProducto + "' no encontrado");
        }
        return producto;
    }


    public Producto registrar(Producto producto, int idUsuario) {
        UsuarioDTO usuario = findUsuarioById(idUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario con ID " + idUsuario + " no encontrado");
        }
        if (usuario.isEstado() == false) {
            throw new RuntimeException("Usuario con ID " + idUsuario + " está inactivo");
            
        }
        if (usuario.getPermiso() >=2 || usuario.getPermiso() <= 4) {
            producto.setNombreProducto(producto.getNombreProducto());
            producto.setTipoProducto(TipoProducto.valueOf(producto.getTipoProducto().name()));
            producto.setValor(producto.getValor());
            producto.setStock(producto.getStock());
            producto.setResena(producto.getResena());
            producto.setFechaRegistro(LocalDate.now().toString());
            producto.setIdUsuario(idUsuario);
            
        }
        
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(int idProducto, Producto productoActualizado, int idUsuario) {
    UsuarioDTO usuario = findUsuarioById(idUsuario);
    if (usuario == null) {
        throw new RuntimeException("Usuario con ID " + idUsuario + " no encontrado");
    }

    if (!usuario.isEstado()) {
        throw new RuntimeException("Usuario con ID " + idUsuario + " está inactivo");
    }

    if (usuario.getPermiso() < 2 || usuario.getPermiso() > 4) {
        throw new RuntimeException("Usuario con ID " + idUsuario + " no tiene permisos para actualizar productos");
    }

    Producto productoExistente = productoRepository.findById(idProducto);
    if (productoExistente == null) {
        throw new RuntimeException("Producto con ID " + idProducto + " no encontrado");
    }

    productoExistente.setNombreProducto(productoActualizado.getNombreProducto());
    productoExistente.setTipoProducto(TipoProducto.valueOf(productoActualizado.getTipoProducto().name()));
    productoExistente.setValor(productoActualizado.getValor());
    productoExistente.setStock(productoActualizado.getStock());
    productoExistente.setResena(productoActualizado.getResena());
    productoExistente.setFechaRegistro(LocalDate.now().toString());
    productoExistente.setIdUsuario(idUsuario);

    return productoRepository.save(productoExistente);
    }
    
    
    public void eliminarProducto(int idProducto, int idUsuario) {
    UsuarioDTO usuario = findUsuarioById(idUsuario);
    if (usuario == null) {
        throw new RuntimeException("Usuario con ID " + idUsuario + " no encontrado");
    }

    if (!usuario.isEstado()) {
        throw new RuntimeException("Usuario con ID " + idUsuario + " está inactivo");
    }

    if (usuario.getPermiso() < 2 || usuario.getPermiso() > 4) {
        throw new RuntimeException("Usuario con ID " + idUsuario + " no tiene permisos para eliminar productos");
    }

    Producto producto = productoRepository.findById(idProducto);
    if (producto == null) {
        throw new RuntimeException("Producto con ID " + idProducto + " no encontrado");
    }

    productoRepository.delete(producto);
    }

    private void registrarMovimiento(Producto producto, int cantidad, TipoMovimiento tipo, int usuarioId, String comentario) {
    MovimientoInventario movimiento = new MovimientoInventario();
    movimiento.setTipoMovimiento(tipo);
    movimiento.setCantidad(cantidad);
    movimiento.setFecha(LocalDateTime.now());
    movimiento.setUsuarioId(usuarioId);
    movimiento.setComentario(comentario);
    movimiento.setProducto(producto);
    movimientoInventarioRepository.save(movimiento);
}

    public Producto agregarStock(int idProducto, int cantidad, int idUsuario) {
    UsuarioDTO usuario = findUsuarioById(idUsuario);
    if (usuario == null) {
        throw new RuntimeException("Usuario con ID " + idUsuario + " no encontrado");
    }
    if (!usuario.isEstado()) {
        throw new RuntimeException("Usuario con ID " + idUsuario + " está inactivo");
    }
    if (usuario.getPermiso() < 2 || usuario.getPermiso() > 4) {
        throw new RuntimeException("Usuario sin permisos para modificar stock");
    }

    Producto producto = productoRepository.findById(idProducto);
    if (producto == null) {
        throw new RuntimeException("Producto con ID " + idProducto + " no encontrado");
    }

    producto.setStock(producto.getStock() + cantidad);
    productoRepository.save(producto);

    registrarMovimiento(producto, cantidad, TipoMovimiento.AGREGAR, idUsuario, "Agregado por usuario");

    return producto;
    }

    public Producto retirarStock(int idProducto, int cantidad, int idUsuario) {
    UsuarioDTO usuario = findUsuarioById(idUsuario);
    if (usuario == null) {
        throw new RuntimeException("Usuario con ID " + idUsuario + " no encontrado");
    }
    if (!usuario.isEstado()) {
        throw new RuntimeException("Usuario con ID " + idUsuario + " está inactivo");
    }
    if (usuario.getPermiso() < 2 || usuario.getPermiso() > 4) {
        throw new RuntimeException("Usuario sin permisos para modificar stock");
    }

    Producto producto = productoRepository.findById(idProducto);
    if (producto == null) {
        throw new RuntimeException("Producto con ID " + idProducto + " no encontrado");
    }

    if (producto.getStock() < cantidad) {
        throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStock());
    }

    producto.setStock(producto.getStock() - cantidad);
    productoRepository.save(producto);

    registrarMovimiento(producto, cantidad, TipoMovimiento.RETIRAR, idUsuario, "Retiro por usuario");

    return producto;
    }
    




    public List<Producto> findByTipoProducto(TipoProducto tipoProducto) {
        return productoRepository.findByTipoProducto(tipoProducto);
    }
}
