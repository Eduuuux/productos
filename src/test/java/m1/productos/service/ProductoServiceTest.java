package m1.productos.service;

import m1.productos.DTO.UsuarioDTO;
import m1.productos.model.Producto;
import m1.productos.model.TipoMovimiento;
import m1.productos.model.TipoProducto;
import m1.productos.repository.MovimientoInventarioRepository;
import m1.productos.repository.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @InjectMocks
    private ProductoService productoService;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUsuarioById() {
        int userId = 1;
        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(userId);
        mockUsuario.setEstado(true);
        mockUsuario.setPermiso(3);

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + userId, UsuarioDTO.class))
                .thenReturn(mockUsuario);

        UsuarioDTO result = productoService.findUsuarioById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertTrue(result.isEstado());
        assertEquals(3, result.getPermiso());
        verify(restTemplate, times(1)).getForObject("http://localhost:8081/usuario/" + userId, UsuarioDTO.class);
    }


    @Test
    void testFindById() {
        int productoId = 10;
        Producto mockProducto = new Producto();
        mockProducto.setId(productoId);
        mockProducto.setNombreProducto("agua brava");
        mockProducto.setTipoProducto(TipoProducto.HOMBRE);

        when(productoRepository.findById(productoId)).thenReturn(mockProducto);

        Producto result = productoService.findById(productoId);

        assertNotNull(result);
        assertEquals("agua brava", result.getNombreProducto());
        assertEquals(TipoProducto.HOMBRE, result.getTipoProducto());
        verify(productoRepository, times(1)).findById(productoId);
    }


    @Test
    void testFindAll() {
        Producto p1 = new Producto();
        p1.setId(1);
        p1.setNombreProducto("agua brava");
        p1.setTipoProducto(TipoProducto.HOMBRE);

        Producto p2 = new Producto();
        p2.setId(2);
        p2.setNombreProducto("hot");
        p2.setTipoProducto(TipoProducto.MUJER);

        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> result = productoService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("agua brava", result.get(0).getNombreProducto());
        assertEquals("hot", result.get(1).getNombreProducto());
        verify(productoRepository, times(1)).findAll();
    }


    @Test
    void testFindByNombreProducto_ProductoEncontrado() {
        String nombrePerfume = "Chanel No. 5";
        Producto perfume = new Producto();
        perfume.setId(1);
        perfume.setNombreProducto(nombrePerfume);
        perfume.setTipoProducto(TipoProducto.MUJER);
        perfume.setValor(120000);
        perfume.setStock(10);

        when(productoRepository.findByNombreProducto(nombrePerfume)).thenReturn(perfume);

        Producto resultado = productoService.findByNombreProducto(nombrePerfume);

        assertNotNull(resultado);
        assertEquals(nombrePerfume, resultado.getNombreProducto());
        assertEquals(TipoProducto.MUJER, resultado.getTipoProducto());
        verify(productoRepository).findByNombreProducto(nombrePerfume);
    }

    @Test
    void testFindByNombreProducto_ProductoNoEncontrado() {
        String nombrePerfumeInexistente = "Dior Sauvage Elixir";

        when(productoRepository.findByNombreProducto(nombrePerfumeInexistente)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            productoService.findByNombreProducto(nombrePerfumeInexistente);
        });

        assertEquals("Producto con nombre '" + nombrePerfumeInexistente + "' no encontrado", ex.getMessage());
        verify(productoRepository).findByNombreProducto(nombrePerfumeInexistente);
    }

        private Producto crearPerfumeEjemplo() {
        Producto producto = new Producto();
        producto.setNombreProducto("Chanel No. 5");
        producto.setTipoProducto(TipoProducto.MUJER);
        producto.setValor(150000);
        producto.setStock(20);
        producto.setResena("Perfume clásico y elegante");
        return producto;
    }

    private UsuarioDTO crearUsuarioValido() {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1);
        usuario.setEstado(true);
        usuario.setPermiso(2);
        return usuario;
    }

    @Test
    void testRegistrarProducto_Exito() {
        Producto perfume = crearPerfumeEjemplo();
        UsuarioDTO usuario = crearUsuarioValido();

        when(restTemplate.getForObject("http://localhost:8081/usuario/1", UsuarioDTO.class)).thenReturn(usuario);
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArguments()[0]);

        Producto resultado = productoService.registrar(perfume, 1);

        assertNotNull(resultado);
        assertEquals("Chanel No. 5", resultado.getNombreProducto());
        assertEquals(TipoProducto.MUJER, resultado.getTipoProducto());
        assertEquals(150000, resultado.getValor());
        assertEquals(20, resultado.getStock());
        assertEquals("Perfume clásico y elegante", resultado.getResena());
        assertEquals(1, resultado.getIdUsuario());
        assertNotNull(resultado.getFechaRegistro());

        verify(restTemplate).getForObject("http://localhost:8081/usuario/1", UsuarioDTO.class);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void testRegistrarProducto_UsuarioNoEncontrado() {
        Producto perfume = crearPerfumeEjemplo();

        when(restTemplate.getForObject("http://localhost:8081/usuario/99", UsuarioDTO.class)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            productoService.registrar(perfume, 99);
        });

        assertTrue(ex.getMessage().contains("Usuario con ID 99 no encontrado"));
        verify(restTemplate).getForObject("http://localhost:8081/usuario/99", UsuarioDTO.class);
    }

    @Test
    void testRegistrarProducto_UsuarioInactivo() {
        Producto perfume = crearPerfumeEjemplo();
        UsuarioDTO usuarioInactivo = crearUsuarioValido();
        usuarioInactivo.setEstado(false);

        when(restTemplate.getForObject("http://localhost:8081/usuario/1", UsuarioDTO.class)).thenReturn(usuarioInactivo);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            productoService.registrar(perfume, 1);
        });

        assertTrue(ex.getMessage().contains("Usuario con ID 1 está inactivo"));
        verify(restTemplate).getForObject("http://localhost:8081/usuario/1", UsuarioDTO.class);
    }

    @Test // Test para verificar que el permiso del usuario es válido
    void testRegistrarProducto_PermisoFueraDeRango() {
        Producto perfume = crearPerfumeEjemplo();
        UsuarioDTO usuarioSinPermiso = crearUsuarioValido();
        usuarioSinPermiso.setPermiso(1); 

        when(restTemplate.getForObject("http://localhost:8081/usuario/1", UsuarioDTO.class)).thenReturn(usuarioSinPermiso);

        Producto resultado = productoService.registrar(perfume, 1);

        assertEquals("Chanel No. 5", resultado.getNombreProducto()); 
        assertEquals(0, resultado.getIdUsuario()); 
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
void testActualizarProducto_Exito() {
    // Datos iniciales
    int idProducto = 1;
    int idUsuario = 2;

    // Producto original existente
    Producto productoExistente = new Producto();
    productoExistente.setNombreProducto("Perfume Viejo");
    productoExistente.setTipoProducto(TipoProducto.HOMBRE);
    productoExistente.setValor(10000);
    productoExistente.setStock(10);
    productoExistente.setResena("Antiguo");
    productoExistente.setIdUsuario(9);

    Producto productoActualizado = new Producto();
    productoActualizado.setNombreProducto("Perfume Nuevo");
    productoActualizado.setTipoProducto(TipoProducto.MUJER);
    productoActualizado.setValor(15000);
    productoActualizado.setStock(20);
    productoActualizado.setResena("Fragancia fresca");

    UsuarioDTO usuario = new UsuarioDTO();
    usuario.setId(idUsuario);
    usuario.setEstado(true);
    usuario.setPermiso(3);

    when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
        .thenReturn(usuario);
    when(productoRepository.findById(idProducto)).thenReturn(productoExistente);
    when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Producto resultado = productoService.actualizarProducto(idProducto, productoActualizado, idUsuario);

    assertNotNull(resultado);
    assertEquals("Perfume Nuevo", resultado.getNombreProducto());
    assertEquals(TipoProducto.MUJER, resultado.getTipoProducto());
    assertEquals(15000, resultado.getValor());
    assertEquals(20, resultado.getStock());
    assertEquals("Fragancia fresca", resultado.getResena());
    assertEquals(idUsuario, resultado.getIdUsuario());

    verify(productoRepository).save(productoExistente);
}

@Test
void testActualizarProducto_UsuarioNoEncontrado() {
    int idProducto = 1;
    int idUsuario = 2;
    Producto productoActualizado = new Producto();

    when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
        .thenReturn(null);  // Simula usuario no encontrado

    RuntimeException exception = assertThrows(RuntimeException.class, () ->
        productoService.actualizarProducto(idProducto, productoActualizado, idUsuario)
    );

    assertEquals("Usuario con ID 2 no encontrado", exception.getMessage());
}
@Test
void testActualizarProducto_UsuarioInactivo() {
    int idProducto = 1;
    int idUsuario = 2;
    Producto productoActualizado = new Producto();

    UsuarioDTO usuario = new UsuarioDTO();
    usuario.setId(idUsuario);
    usuario.setEstado(false);  // Usuario inactivo
    usuario.setPermiso(3);

    when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
        .thenReturn(usuario);

    RuntimeException exception = assertThrows(RuntimeException.class, () ->
        productoService.actualizarProducto(idProducto, productoActualizado, idUsuario)
    );

    assertEquals("Usuario con ID 2 está inactivo", exception.getMessage());
}
@Test
void testActualizarProducto_UsuarioSinPermiso() {
    int idProducto = 1;
    int idUsuario = 2;
    Producto productoActualizado = new Producto();

    UsuarioDTO usuario = new UsuarioDTO();
    usuario.setId(idUsuario);
    usuario.setEstado(true);
    usuario.setPermiso(1);  // Permiso no válido

    when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
        .thenReturn(usuario);

    RuntimeException exception = assertThrows(RuntimeException.class, () ->
        productoService.actualizarProducto(idProducto, productoActualizado, idUsuario)
    );

    assertEquals("Usuario con ID 2 no tiene permisos para actualizar productos", exception.getMessage());
}
@Test
void testActualizarProducto_ProductoNoEncontrado() {
    int idProducto = 99;
    int idUsuario = 2;
    Producto productoActualizado = new Producto();

    UsuarioDTO usuario = new UsuarioDTO();
    usuario.setId(idUsuario);
    usuario.setEstado(true);
    usuario.setPermiso(3);

    when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
        .thenReturn(usuario);
    when(productoRepository.findById(idProducto)).thenReturn(null);  // Producto no existe

    RuntimeException exception = assertThrows(RuntimeException.class, () ->
        productoService.actualizarProducto(idProducto, productoActualizado, idUsuario)
    );

    assertEquals("Producto con ID 99 no encontrado", exception.getMessage());
}

    @Test
    void testEliminarProducto_Exitoso() {
        int idProducto = 1;
        int idUsuario = 10;

        UsuarioDTO usuarioActivoConPermiso = new UsuarioDTO();
        usuarioActivoConPermiso.setEstado(true);
        usuarioActivoConPermiso.setPermiso(3);

        Producto producto = new Producto();

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(usuarioActivoConPermiso);
        when(productoRepository.findById(idProducto)).thenReturn(producto);

        productoService.eliminarProducto(idProducto, idUsuario);

        verify(productoRepository, times(1)).delete(producto);
    }

    @Test
    void testEliminarProducto_UsuarioNoEncontrado() {
        int idUsuario = 10;
        int idProducto = 1;

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.eliminarProducto(idProducto, idUsuario);
        });

        assertEquals("Usuario con ID " + idUsuario + " no encontrado", exception.getMessage());
        verify(productoRepository, never()).delete(any());
    }

    @Test
    void testEliminarProducto_UsuarioInactivo() {
        int idUsuario = 10;
        int idProducto = 1;

        UsuarioDTO usuarioInactivo = new UsuarioDTO();
        usuarioInactivo.setEstado(false);

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(usuarioInactivo);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.eliminarProducto(idProducto, idUsuario);
        });

        assertEquals("Usuario con ID " + idUsuario + " está inactivo", exception.getMessage());
        verify(productoRepository, never()).delete(any());
    }

    @Test 
    void testEliminarProducto_UsuarioSinPermiso() {
        int idUsuario = 10;
        int idProducto = 1;

        UsuarioDTO usuarioSinPermiso = new UsuarioDTO();
        usuarioSinPermiso.setEstado(true);
        usuarioSinPermiso.setPermiso(1);

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(usuarioSinPermiso);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.eliminarProducto(idProducto, idUsuario);
        });

        assertEquals("Usuario con ID " + idUsuario + " no tiene permisos para eliminar productos", exception.getMessage());
        verify(productoRepository, never()).delete(any());
    }

    @Test
    void testEliminarProducto_ProductoNoEncontrado() {
        int idUsuario = 10;
        int idProducto = 1;

        UsuarioDTO usuarioActivoConPermiso = new UsuarioDTO();
        usuarioActivoConPermiso.setEstado(true);
        usuarioActivoConPermiso.setPermiso(3);

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(usuarioActivoConPermiso);
        when(productoRepository.findById(idProducto)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.eliminarProducto(idProducto, idUsuario);
        });

        assertEquals("Producto con ID " + idProducto + " no encontrado", exception.getMessage());
        verify(productoRepository, never()).delete(any());
    }
        @Test
    void testAgregarStock_Exitoso() {
        int idProducto = 1;
        int idUsuario = 100;
        int cantidadAgregar = 5;

        // Producto inicial con stock 10
        Producto producto = new Producto();
        producto.setStock(10);

        producto.setNombreProducto("Perfume X");

        // Usuario con permiso válido
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEstado(true);
        usuario.setPermiso(3);

        // Mocks
        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(usuario);
        when(productoRepository.findById(idProducto)).thenReturn(producto);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutar método
        Producto resultado = productoService.agregarStock(idProducto, cantidadAgregar, idUsuario);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(15, resultado.getStock());

        verify(productoRepository).save(producto);

        ArgumentCaptor<m1.productos.model.MovimientoInventario> movimientoCaptor =
            ArgumentCaptor.forClass(m1.productos.model.MovimientoInventario.class);

        verify(movimientoInventarioRepository).save(movimientoCaptor.capture());
        m1.productos.model.MovimientoInventario movimientoGuardado = movimientoCaptor.getValue();

        assertEquals(TipoMovimiento.AGREGAR, movimientoGuardado.getTipoMovimiento());
        assertEquals(cantidadAgregar, movimientoGuardado.getCantidad());
        assertEquals(idUsuario, movimientoGuardado.getUsuarioId());
        assertEquals(producto, movimientoGuardado.getProducto());
        assertEquals("Agregado por usuario", movimientoGuardado.getComentario());
        assertNotNull(movimientoGuardado.getFecha());
    }

        @Test
    void testRetirarStock_Exito() {
        int idProducto = 1;
        int idUsuario = 100;
        int cantidadRetirar = 3;

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEstado(true);
        usuario.setPermiso(3);

        Producto producto = new Producto();
        producto.setStock(10);

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(usuario);
        when(productoRepository.findById(idProducto)).thenReturn(producto);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(movimientoInventarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoService.retirarStock(idProducto, cantidadRetirar, idUsuario);

        assertEquals(7, resultado.getStock());
        verify(productoRepository).save(producto);

        verify(movimientoInventarioRepository).save(argThat(movimiento -> 
            movimiento.getTipoMovimiento() == TipoMovimiento.RETIRAR &&
            movimiento.getCantidad() == cantidadRetirar &&
            movimiento.getUsuarioId() == idUsuario &&
            movimiento.getProducto() == producto &&
            "Retiro por usuario".equals(movimiento.getComentario())
        ));
    }

    @Test
    void testRetirarStock_UsuarioNoEncontrado() {
        int idProducto = 1;
        int idUsuario = 100;

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.retirarStock(idProducto, 1, idUsuario);
        });

        assertEquals("Usuario con ID " + idUsuario + " no encontrado", exception.getMessage());
        verify(productoRepository, never()).findById(anyInt());
        verify(productoRepository, never()).save(any());
        verify(movimientoInventarioRepository, never()).save(any());
    }

    @Test
    void testRetirarStock_UsuarioInactivo() {
        int idProducto = 1;
        int idUsuario = 100;

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEstado(false);
        usuario.setPermiso(3);

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(usuario);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.retirarStock(idProducto, 1, idUsuario);
        });

        assertEquals("Usuario con ID " + idUsuario + " está inactivo", exception.getMessage());
        verify(productoRepository, never()).findById(anyInt());
        verify(productoRepository, never()).save(any());
        verify(movimientoInventarioRepository, never()).save(any());
    }

    @Test
    void testRetirarStock_UsuarioSinPermiso() {
        int idProducto = 1;
        int idUsuario = 100;

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEstado(true);
        usuario.setPermiso(1); // permiso fuera del rango [2-4]

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(usuario);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.retirarStock(idProducto, 1, idUsuario);
        });

        assertEquals("Usuario sin permisos para modificar stock", exception.getMessage());
        verify(productoRepository, never()).findById(anyInt());
        verify(productoRepository, never()).save(any());
        verify(movimientoInventarioRepository, never()).save(any());
    }

    @Test
    void testRetirarStock_ProductoNoEncontrado() {
        int idProducto = 1;
        int idUsuario = 100;

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEstado(true);
        usuario.setPermiso(3);

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(usuario);
        when(productoRepository.findById(idProducto)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.retirarStock(idProducto, 1, idUsuario);
        });

        assertEquals("Producto con ID " + idProducto + " no encontrado", exception.getMessage());
        verify(productoRepository, never()).save(any());
        verify(movimientoInventarioRepository, never()).save(any());
    }

    @Test
    void testRetirarStock_StockInsuficiente() {
        int idProducto = 1;
        int idUsuario = 100;
        int cantidadRetirar = 5;

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setEstado(true);
        usuario.setPermiso(3);

        Producto producto = new Producto();
        producto.setStock(3);

        when(restTemplate.getForObject("http://localhost:8081/usuario/" + idUsuario, UsuarioDTO.class))
            .thenReturn(usuario);
        when(productoRepository.findById(idProducto)).thenReturn(producto);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.retirarStock(idProducto, cantidadRetirar, idUsuario);
        });

        assertEquals("Stock insuficiente. Disponible: " + producto.getStock(), exception.getMessage());
        verify(productoRepository, never()).save(any());
        verify(movimientoInventarioRepository, never()).save(any());
    }
    @Test
void testFindByTipoProducto() {
    TipoProducto tipo = TipoProducto.HOMBRE; // puede ser HOMBRE, MUJER o UNISEX según enum

    List<Producto> listaEsperada = Arrays.asList(
        new Producto(1, "Perfume A", tipo, 10000, 10, "Reseña A", "2025-07-08", 1),
        new Producto(2, "Perfume B", tipo, 15000, 5, "Reseña B", "2025-07-07", 2)
    );

    when(productoRepository.findByTipoProducto(tipo)).thenReturn(listaEsperada);

    List<Producto> resultado = productoService.findByTipoProducto(tipo);

    assertNotNull(resultado);
    assertEquals(2, resultado.size());
    assertEquals("Perfume A", resultado.get(0).getNombreProducto());
    assertEquals(tipo, resultado.get(0).getTipoProducto());
    assertEquals("Perfume B", resultado.get(1).getNombreProducto());

    verify(productoRepository).findByTipoProducto(tipo);
}

}







