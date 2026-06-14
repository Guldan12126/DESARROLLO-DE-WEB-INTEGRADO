package apf3.ChifaXinYan.Service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Enum.EstadoPedido;
import apf3.ChifaXinYan.Enum.RolUsuario;
import apf3.ChifaXinYan.Model.Categoria;
import apf3.ChifaXinYan.Model.Mesa;
import apf3.ChifaXinYan.Model.Pedido;
import apf3.ChifaXinYan.Model.Producto;
import apf3.ChifaXinYan.Model.Usuario;
import apf3.ChifaXinYan.Repository.CategoriaRepository;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional 
public class PedidoServiceTest {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private MesaService mesaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private static Long pedidoId;
    private static Long mesaId;
    private static Long mozoId;

    @Test
    @Order(1)
    @DisplayName("1. Debe crear un nuevo pedido")
    public void testCrearPedido() {
        if (mesaId == null) {
            List<Mesa> mesas = mesaService.listarTodas();
            if (mesas.isEmpty()) {
                Mesa nuevaMesa = new Mesa();
                nuevaMesa.setNumero(99);
                nuevaMesa.setCapacidad(4);
                nuevaMesa.setUbicacion("Prueba");
                Mesa mesaCreada = mesaService.crearMesa(nuevaMesa);
                mesaId = mesaCreada.getId();
            } else {
                mesaId = mesas.get(0).getId();
            }
        }
        
        if (mozoId == null) {
            Usuario mozo = new Usuario();
            mozo.setNombre("Mozo Test");
            mozo.setEmail("mozotest@test.com");
            mozo.setPassword("test123");
            mozo.setRol(RolUsuario.MOZO.name());
            mozoId = usuarioService.registrarUsuario(mozo).getId();           
        }
        
        Mesa mesa = mesaService.obtenerPorId(mesaId);
        Usuario mozo = usuarioService.obtenerPorId(mozoId);

        assertNotNull(mesa, "La mesa debe existir para crear el pedido");
        assertNotNull(mozo, "El mozo debe existir para crear el pedido");

        Pedido nuevo = new Pedido();
        nuevo.setMesa(mesa);
        nuevo.setUsuario(mozo);
        nuevo.setDetalles(new ArrayList<>());
        
        Pedido creado = pedidoService.crearPedido(nuevo);
        pedidoId = creado.getId();
        
        assertNotNull(creado.getId());
        assertNotNull(creado.getFechaPedido());
        assertEquals(EstadoPedido.PENDIENTE, creado.getEstado());
    }

    @Test
    @Order(2)
    @DisplayName("2. Debe agregar detalle al pedido")
    public void testAgregarDetalle() {
        List<Producto> productos = productoService.listarTodos();
        Long productoId;
        
        if (productos.isEmpty()) {
            Producto nuevoProducto = new Producto();
            
            Categoria categoria = categoriaRepository.findByNombre("PRUEBA")
                    .orElseGet(() -> categoriaRepository.save(new Categoria(null, "PRUEBA")));

            nuevoProducto.setNombre("Producto Test");
            nuevoProducto.setCategoria(categoria);
            nuevoProducto.setPrecio(10.0);
            nuevoProducto.setStock(100);
            Producto productoGuardado = productoService.crearProducto(nuevoProducto);
            productoId = productoGuardado.getId();
        } else {
            productoId = productos.get(0).getId();
        } //
        
        // Llamar a agregarDetalle con los parámetros correctos
        Pedido actualizado = pedidoService.agregarDetalle(pedidoId, productoId, 2);
        
        assertNotNull(actualizado);
        assertFalse(actualizado.getDetalles().isEmpty());
        assertEquals(50.0, actualizado.getTotal(), 0.01);
    }

    @Test
    @Order(3)
    @DisplayName("3. Debe actualizar el estado del pedido")
    public void testActualizarEstado() {
        Pedido actualizado = pedidoService.actualizarEstado(pedidoId, EstadoPedido.EN_PREPARACION.name());
        
        assertNotNull(actualizado);
        assertEquals(EstadoPedido.EN_PREPARACION, actualizado.getEstado());
    }

    @Test
    @Order(4)
    @DisplayName("4. Debe listar pedidos activos")
    public void testListarPedidosActivos() {
        List<Pedido> activos = pedidoService.listarPedidosActivos();
        assertNotNull(activos);
    }
    
    @Test
    @Order(5)
    @DisplayName("5. Debe marcar pedido como listo para servir")
    public void testMarcarPedidoListo() {
        Pedido actualizado = pedidoService.actualizarEstado(pedidoId, EstadoPedido.LISTO.name());
        
        assertNotNull(actualizado);
        assertEquals(EstadoPedido.LISTO, actualizado.getEstado());
    }
    
    @Test
    @Order(6)
    @DisplayName("6. Debe obtener pedido por ID de mesa")
    public void testObtenerPedidoPorMesa() {
        List<Pedido> pedidos = pedidoService.listarPorMesa(mesaId);
        assertNotNull(pedidos);
    }
    
    @Test
    @Order(7)
    @DisplayName("7. Debe calcular total del pedido correctamente")
    public void testCalcularTotal() {
        List<Producto> productos = productoService.listarTodos();
        if (!productos.isEmpty()) {
            Long productoId = productos.get(0).getId();
            int cantidad = 1;
            pedidoService.agregarDetalle(pedidoId, productoId, cantidad);
        }

        Pedido pedido = pedidoService.obtenerPorId(pedidoId);
        assertNotNull(pedido);
        assertTrue(pedido.getTotal() > 0);
    }

    @Test
    @Order(8)
    @DisplayName("8. No debe permitir agregar detalle si no hay stock suficiente")
    public void testAgregarDetalleSinStock() {
        // 1. Preparar un producto con stock limitado
        Producto producto = new Producto();
        producto.setNombre("Producto Stock Limitado");
        producto.setPrecio(20.0);
        producto.setStock(3); // Solo hay 3 unidades
        
        Categoria cat = categoriaRepository.findByNombre("PRUEBA_STOCK")
                .orElseGet(() -> categoriaRepository.save(new Categoria(null, "PRUEBA_STOCK")));
        producto.setCategoria(cat);
        
        Producto guardado = productoService.crearProducto(producto);
        
        // 2. Intentar agregar 5 unidades (más de lo que hay en stock)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.agregarDetalle(pedidoId, guardado.getId(), 5);
        });

        // 3. Verificar que el mensaje de error sea el correcto (según lógica en PedidoService)
        assertTrue(exception.getMessage().contains("Stock insuficiente"));
    }
}