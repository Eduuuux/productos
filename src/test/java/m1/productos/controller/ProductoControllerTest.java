package m1.productos.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import m1.productos.model.Producto;
import m1.productos.model.TipoProducto;
import m1.productos.service.ProductoService;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarProductos_RetornaListaOK() throws Exception {
        List<Producto> productos = List.of(
            crearProductoEjemplo(1, "Perfume A", TipoProducto.HOMBRE),
            crearProductoEjemplo(2, "Perfume B", TipoProducto.MUJER)
        );
        when(productoService.findAll()).thenReturn(productos);

        mockMvc.perform(get("/api/productos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombreProducto").value("Perfume A"))
            .andExpect(jsonPath("$[1].tipoProducto").value("MUJER"));
    }

    @Test
    void listarProductos_SinProductos_NoContent() throws Exception {
        when(productoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/productos"))
            .andExpect(status().isNoContent());
    }

    @Test
    void registrarProducto_Exito() throws Exception {
        Producto nuevoProducto = crearProductoEjemplo(1, "Perfume Nuevo", TipoProducto.UNISEX);
        when(productoService.registrar(any(Producto.class), any(Integer.class))).thenReturn(nuevoProducto);

        mockMvc.perform(post("/api/productos/usuario/1/registroProd")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoProducto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombreProducto").value("Perfume Nuevo"))
            .andExpect(jsonPath("$.tipoProducto").value("UNISEX"));
    }

    @Test
    void registrarProducto_Falla() throws Exception {
        when(productoService.registrar(any(Producto.class), any(Integer.class)))
            .thenThrow(new RuntimeException("Error de registro"));

        Producto producto = crearProductoEjemplo(0, "Perfume Error", TipoProducto.HOMBRE);

        mockMvc.perform(post("/api/productos/usuario/1/registroProd")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getById_ProductoExiste_OK() throws Exception {
        Producto producto = crearProductoEjemplo(1, "Perfume X", TipoProducto.MUJER);
        when(productoService.findById(1)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombreProducto").value("Perfume X"));
    }

    @Test
    void getById_ProductoNoExiste_NotFound() throws Exception {
        when(productoService.findById(99)).thenReturn(null);

        mockMvc.perform(get("/api/productos/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void updateById_ProductoActualizado_OK() throws Exception {
        
        Producto producto = crearProductoEjemplo(1, "Perfume Actualizado", TipoProducto.UNISEX);

        when(productoService.actualizarProducto(eq(1), any(Producto.class), eq(1)))
            .thenReturn(producto);

        
        mockMvc.perform(put("/api/productos/actualizar/1/usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombreProducto").value("Perfume Actualizado"))
            .andExpect(jsonPath("$.tipoProducto").value("UNISEX"));
    }


    @Test
    void updateById_UsuarioSinPermiso_BadRequest() throws Exception {
        Producto producto = crearProductoEjemplo(1, "Perfume sin permiso", TipoProducto.MUJER);

        when(productoService.actualizarProducto(eq(1), any(Producto.class), eq(1)))
            .thenThrow(new RuntimeException("Usuario sin permisos para actualizar productos"));

        mockMvc.perform(put("/api/productos/actualizar/1/usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Usuario sin permisos para actualizar productos"));
    }

    @Test
    void updateById_ErrorInterno_InternalServerError() throws Exception {
        Producto producto = crearProductoEjemplo(1, "Perfume", TipoProducto.HOMBRE);

        when(productoService.actualizarProducto(eq(1), any(Producto.class), eq(1)))
            .thenThrow(new RuntimeException(new Exception("Fallo interno")));

        mockMvc.perform(put("/api/productos/actualizar/1/usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
            .andExpect(status().isBadRequest());  // Cambia si manejas el `Exception` diferente
    }




    @Test
    void deleteProducto_Exito() throws Exception {
        doNothing().when(productoService).eliminarProducto(1, 1);

        mockMvc.perform(delete("/api/productos/eliminar/1/usuario/1"))
            .andExpect(status().isOk())
            .andExpect(content().string("Producto eliminado correctamente."));
    }

    @Test
    void deleteProducto_Error() throws Exception {
        doNothing().when(productoService).eliminarProducto(1, 1);
        doNothing().when(productoService).eliminarProducto(1, 1);

        doNothing().when(productoService).eliminarProducto(1, 1);
        org.mockito.Mockito.doThrow(new RuntimeException("No tiene permiso"))
            .when(productoService).eliminarProducto(2, 2);

        mockMvc.perform(delete("/api/productos/eliminar/2/usuario/2"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("No tiene permiso"));
    }

    @Test
    void getByTipo_ProductosEncontrados_ReturnsOk() throws Exception {
    List<Producto> productos = List.of(
        crearProductoEjemplo(1, "Perfume Hombre", TipoProducto.HOMBRE),
        crearProductoEjemplo(2, "Perfume Hombre 2", TipoProducto.HOMBRE)
    );

    when(productoService.findByTipoProducto(TipoProducto.HOMBRE)).thenReturn(productos);

    mockMvc.perform(get("/api/productos/reporte/tipo/hombre"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nombreProducto").value("Perfume Hombre"))
        .andExpect(jsonPath("$[1].nombreProducto").value("Perfume Hombre 2"));
}
    @Test
    void getByTipo_SinProductos_ReturnsNoContent() throws Exception {
        when(productoService.findByTipoProducto(TipoProducto.MUJER)).thenReturn(List.of());

        mockMvc.perform(get("/api/productos/reporte/tipo/mujer"))
            .andExpect(status().isNoContent());
    }

    @Test
    void getByTipo_TipoInvalido_ReturnsBadRequest() throws Exception {
    mockMvc.perform(get("/api/productos/reporte/tipo/invalido"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Tipo de producto inválido: invalido"));
}

    @Test
    void testRetirarStock_Success() throws Exception {
    Producto productoActualizado = crearProductoEjemplo(1, "Chanel No. 5", TipoProducto.UNISEX);

        when(productoService.retirarStock(1, 5, 10)).thenReturn(productoActualizado);

        mockMvc.perform(patch("/api/productos/stock/retirar/1")
                .param("cantidad", "5")
                .param("idUsuario", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.stock").value(10)); // Espera el nuevo stock
    }

    @Test
    void testRetirarStock_Error() throws Exception {
        when(productoService.retirarStock(1, 50, 10))
            .thenThrow(new RuntimeException("Stock insuficiente. Disponible: 20"));

        mockMvc.perform(patch("/api/productos/stock/retirar/1")
                .param("cantidad", "50")
                .param("idUsuario", "10"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Stock insuficiente. Disponible: 20"));
    }
        @Test
    void testGetByNombre_Success() throws Exception {
        String nombreProducto = "Chanel No. 5";
        Producto producto = crearProductoEjemplo(1, nombreProducto, TipoProducto.UNISEX);

        when(productoService.findByNombreProducto(nombreProducto)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/buscar/{nombre}", nombreProducto)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombreProducto").value(nombreProducto))
            .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void testGetByNombre_NotFound() throws Exception {
        String nombreProducto = "ProductoNoExistente";

        when(productoService.findByNombreProducto(nombreProducto))
            .thenThrow(new RuntimeException("Producto no encontrado"));

        mockMvc.perform(get("/api/productos/buscar/{nombre}", nombreProducto)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Producto no encontrado"));
    }
    @Test
    void testAgregarStock_Success() throws Exception {
        Producto productoActualizado = crearProductoEjemplo(1, "Chanel No. 5", TipoProducto.UNISEX);
        productoActualizado.setStock(15); 

        when(productoService.agregarStock(1, 5, 10)).thenReturn(productoActualizado);

        mockMvc.perform(patch("/api/productos/stock/agregar/1")
                .param("cantidad", "5")
                .param("idUsuario", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.stock").value(15)); 
    }

    @Test
    void testAgregarStock_RuntimeException() throws Exception {
        when(productoService.agregarStock(1, 5, 10))
            .thenThrow(new RuntimeException("Usuario sin permisos para modificar stock"));

        mockMvc.perform(patch("/api/productos/stock/agregar/1")
                .param("cantidad", "5")
                .param("idUsuario", "10"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Usuario sin permisos para modificar stock"));
    }



    private Producto crearProductoEjemplo(int id, String nombre, TipoProducto tipo) {
        Producto p = new Producto();
        p.setNombreProducto(nombre);
        p.setTipoProducto(tipo);
        p.setValor(15000);
        p.setStock(10);
        p.setResena("Reseña ejemplo");
        p.setFechaRegistro("2025-07-08");
        p.setIdUsuario(1);
        return p;
    }
}
