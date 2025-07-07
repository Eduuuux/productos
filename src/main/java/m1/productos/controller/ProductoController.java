package m1.productos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<List<Producto>> listar() {
        List<Producto> productos = productoService.findAll();
        if (productos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
        }


    @PostMapping("/registrar")
    public ResponseEntity<Producto> registrar(
            @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.findById(producto.getId());
        if (nuevoProducto == null) {
            return new ResponseEntity<>(productoService.crearProducto(producto), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
        
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable int id) {
        return new ResponseEntity<>(productoService.findById(id), HttpStatus.OK);

    }

    // @PutMapping("/actualizar/{id}")
    // public ResponseEntity<?> updateById(@PathVariable int id, @RequestBody Producto producto) {
    //     // Validar tipoProducto
    //     String tipo = producto.getTipoProducto().toString().toUpperCase();
    //     if (!tipo.equals("HOMBRE") && !tipo.equals("MUJER") && !tipo.equals("UNISEX")) {
    //         return new ResponseEntity<>("El tipo de producto debe ser HOMBRE, MUJER o UNISEX.", HttpStatus.BAD_REQUEST);
    //     }
    //     Producto productoActualizado = productoService.updateById(id, producto);
    //     if (productoActualizado != null) {
    //         return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
    //     } else {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> updateById(
        @PathVariable int id,
        @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.updateById(id, producto);
            if (productoActualizado != null) {
                return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteById(
        @PathVariable int id) {
        try {
            boolean eliminado = productoService.deleteById(id);
            if (eliminado) {
                return new ResponseEntity<>("Producto eliminado correctamente.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Producto no encontrado.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<Producto> getByNombre(@PathVariable String nombre) {
        Producto producto = productoService.findByNombreProducto(nombre);
        if (producto != null) {
            return new ResponseEntity<>(producto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/reporte/tipo/{tipoProducto}")
    public ResponseEntity<List<Producto>> getByTipo(
            @PathVariable String tipoProducto) {
        try {
            TipoProducto tipoEnum = TipoProducto.valueOf(tipoProducto.trim().toUpperCase());
            List<Producto> productos = productoService.findByTipoProducto(tipoEnum);
            if (productos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}




