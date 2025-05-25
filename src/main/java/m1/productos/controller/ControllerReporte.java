package m1.productos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import m1.productos.model.Reporte;
import m1.productos.service.ReporteService;


@RestController
@RequestMapping("/api/reportesProductos")
public class ControllerReporte {

    @Autowired
    private ReporteService reporteService;

    @GetMapping()
    public ResponseEntity<List<Reporte>> listar(){
        List<Reporte> reportes = reporteService.findAll();
        if (reportes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportes, HttpStatus.OK);
    }

    @PutMapping("/reporte/{tipo_producto}")
        public ResponseEntity<List<Reporte>>listarPorTipo(@PathVariable String tipo_producto) {
        List<Reporte> reportes = reporteService.findByTipoProducto(tipo_producto);
        if (tipo_producto.toLowerCase().equals("perfume femenino")) {
            return new ResponseEntity<>(reportes, HttpStatus.OK);
        }
        if (tipo_producto.toLowerCase().equals("perfume masculino")) {
            return new ResponseEntity<>(reportes, HttpStatus.OK);
        }
        if (tipo_producto.toLowerCase().equals("perfume unisex")) {
            return new ResponseEntity<>(reportes, HttpStatus.OK);
        }
        return new ResponseEntity<>(reportes, HttpStatus.OK);
    }

    }

