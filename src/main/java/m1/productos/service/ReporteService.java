package m1.productos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import m1.productos.model.Reporte;
import m1.productos.repository.ReporteRepository;
@Service

public class ReporteService {
    @Autowired
    private ReporteRepository reporteRepository;

    public Reporte crearReporte(Reporte reporte) {
        return reporteRepository.save(reporte);
    }
    public List<Reporte> findAll() {
        return reporteRepository.findAll();
    }

    public Reporte findById(int id) {
        return reporteRepository.findById(id);
    }

    public List<Reporte> findByTipoProducto(String tipoProducto) {
    return reporteRepository.findByProductos_TipoProducto(tipoProducto);
}

}
