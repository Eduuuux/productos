package m1.productos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import m1.productos.model.Producto;
import m1.productos.model.TipoProducto;
import m1.productos.service.ProductoService;






@RestController
@RequestMapping("api/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;

    @GetMapping()
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.findAll();
        if (productos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
        }


    @PostMapping("/usuario/{idUsuario}/registroProd")
    public ResponseEntity<Producto> registrarProducto(@RequestBody Producto producto, @PathVariable int idUsuario) {
        try {
            Producto nuevoProducto = productoService.registrar(producto, idUsuario);
            return ResponseEntity.ok(nuevoProducto);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }
        
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable int id) {
        Producto producto = productoService.findById(id);
        
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(producto);
    }


    @PutMapping("/actualizar/{idProducto}/usuario/{idUsuario}")
    public ResponseEntity<?> updateById(
        @PathVariable int idProducto,
        @PathVariable int idUsuario,
        @RequestBody Producto producto) {
        
        try {
            Producto productoActualizado = productoService.actualizarProducto(idProducto, producto, idUsuario);
            
            if (productoActualizado != null) {
                return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Producto no encontrado.", HttpStatus.NOT_FOUND);
            }
            
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/eliminar/{idProducto}/usuario/{idUsuario}")
    public ResponseEntity<?> deleteProducto(
        @PathVariable int idProducto,
        @PathVariable int idUsuario) {

        try {
            productoService.eliminarProducto(idProducto, idUsuario);
            return new ResponseEntity<>("Producto eliminado correctamente.", HttpStatus.OK);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<?> getByNombre(@PathVariable String nombre) {
        try {
            Producto producto = productoService.findByNombreProducto(nombre);
            return new ResponseEntity<>(producto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/reporte/tipo/{tipoProducto}")
    public ResponseEntity<?> getByTipo(@PathVariable String tipoProducto) {
        try {
            TipoProducto tipoEnum = TipoProducto.valueOf(tipoProducto.trim().toUpperCase());
            List<Producto> productos = productoService.findByTipoProducto(tipoEnum);

            if (productos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(productos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Tipo de producto inválido: " + tipoProducto, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/stock/retirar/{idProducto}")
    public ResponseEntity<?> retirarStock(
            @PathVariable int idProducto,
            @RequestParam int cantidad,
            @RequestParam int idUsuario) {
        try {
            Producto actualizado = productoService.retirarStock(idProducto, cantidad, idUsuario);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/stock/agregar/{idProducto}")
    public ResponseEntity<?> agregarStock(
            @PathVariable int idProducto,
            @RequestParam int cantidad,
            @RequestParam int idUsuario) {
        try {
            Producto actualizado = productoService.agregarStock(idProducto, cantidad, idUsuario);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




}




