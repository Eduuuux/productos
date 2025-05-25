package m1.productos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import m1.productos.model.Producto;
import m1.productos.service.ProductoService;






@RestController
@RequestMapping("api/productos")
public class ControllerProducto {
    
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
    public ResponseEntity<Producto> registrar(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.findById(producto.getId());
        if (nuevoProducto == null) {
            return new ResponseEntity<>(productoService.crearProducto(producto), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
        
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable int id) {
        return new ResponseEntity<>(productoService.findById(id), HttpStatus.OK);

    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Producto> updateById(@PathVariable int id, @RequestBody Producto producto) {
        Producto productoActualizado = productoService.updateById(id, producto);
        if (productoActualizado != null) {
            return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}




