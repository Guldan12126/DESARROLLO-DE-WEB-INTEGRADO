package apf2.ChifaXinYan.Service;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import apf2.ChifaXinYan.Model.DetallePedido;
import apf2.ChifaXinYan.Model.Pedido;
import apf2.ChifaXinYan.Model.Producto;
import apf2.ChifaXinYan.Model.Usuario;
import apf2.ChifaXinYan.Enum.EstadoPedido;
import apf2.ChifaXinYan.Enum.RolUsuario;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional  // Para que los tests no afecten la base de datos real
public class PedidoServiceTest {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private MesaService mesaService;

    private static Long pedidoId;
    private static Long mesaId;
    private static Long mozoId;

    @Test
    @Order(1)
    @DisplayName("1. Debe crear un nuevo pedido")
    public void testCrearPedido() {
        // Crear una mesa primero si no existe
        if (mesaId == null) {
            // Buscar o crear una mesa para la prueba
            var mesas = mesaService.listarTodas();
            if (mesas.isEmpty()) {
                // Crear mesa temporal para prueba
                apf2.ChifaXinYan.Model.Mesa nuevaMesa = new apf2.ChifaXinYan.Model.Mesa();
                nuevaMesa.setNumero(99);
                nuevaMesa.setCapacidad(4);
                nuevaMesa.setUbicacion("Prueba");
                var mesaCreada = mesaService.crearMesa(nuevaMesa);
                mesaId = mesaCreada.getId();
            } else {
                mesaId = mesas.get(0).getId();
            }
        }
        
        // Crear un mozo para asociar al pedido
        if (mozoId == null) {
            Usuario mozo = new Usuario();
            mozo.setNombre("Mozo Test");
            mozo.setEmail("mozotest@test.com");
            mozo.setPassword("test123");
            mozo.setRol(RolUsuario.MOZO.name());
            var mozoCreado = usuarioService.crearUsuario(mozo);
            mozoId = mozoCreado.getId();
        }
        
        Pedido nuevo = new Pedido();
        nuevo.setIdMesa(mesaId);
        nuevo.setIdMozo(mozoId);
        nuevo.setDetalles(new ArrayList<>());
        
        Pedido creado = pedidoService.crearPedido(nuevo);
        pedidoId = creado.getId();
        
        assertNotNull(creado.getId());
        assertNotNull(creado.getFechaPedido());
        assertEquals(EstadoPedido.PENDIENTE.name(), creado.getEstado());
    }

    @Test
    @Order(2)
    @DisplayName("2. Debe agregar detalle al pedido")
    public void testAgregarDetalle() {
        // Verificar que existan productos, si no crear uno
        var productos = productoService.listarTodos();
        Long productoId;
        
        if (productos.isEmpty()) {
            // Crear producto temporal para prueba
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre("Producto Test");
            nuevoProducto.setCategoria("PRUEBA");
            nuevoProducto.setPrecio(10.0);
            nuevoProducto.setStock(100);
            var productoGuardado = productoService.crearProducto(nuevoProducto);
            productoId = productoGuardado.getId();
        } else {
            productoId = productos.get(0).getId();
        }
        
        DetallePedido detalle = new DetallePedido();
        detalle.setIdProducto(productoId);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(25.0);
        
        Pedido actualizado = pedidoService.agregarDetalle(pedidoId, detalle);
        
        assertNotNull(actualizado);
        assertTrue(actualizado.getDetalles().size() > 0);
        assertEquals(50.0, actualizado.getTotal(), 0.01); // 2 x 25 = 50
    }

    @Test
    @Order(3)
    @DisplayName("3. Debe actualizar el estado del pedido")
    public void testActualizarEstado() {
        Pedido actualizado = pedidoService.actualizarEstado(pedidoId, EstadoPedido.EN_PREPARACION.name());
        
        assertNotNull(actualizado);
        assertEquals(EstadoPedido.EN_PREPARACION.name(), actualizado.getEstado());
    }

    @Test
    @Order(4)
    @DisplayName("4. Debe listar pedidos activos")
    public void testListarPedidosActivos() {
        var activos = pedidoService.listarPedidosActivos();
        assertNotNull(activos);
    }
    
    @Test
    @Order(5)
    @DisplayName("5. Debe marcar pedido como listo para servir")
    public void testMarcarPedidoListo() {
        Pedido actualizado = pedidoService.actualizarEstado(pedidoId, EstadoPedido.LISTO.name());
        
        assertNotNull(actualizado);
        assertEquals(EstadoPedido.LISTO.name(), actualizado.getEstado());
    }
    
    @Test
    @Order(6)
    @DisplayName("6. Debe obtener pedido por ID de mesa")
    public void testObtenerPedidoPorMesa() {
        var pedidos = pedidoService.listarPedidosPorMesa(mesaId);
        assertNotNull(pedidos);
    }
    
    @Test
    @Order(7)
    @DisplayName("7. Debe calcular total del pedido correctamente")
    public void testCalcularTotal() {
        // Agregar otro detalle para verificar el total
        var productos = productoService.listarTodos();
        if (!productos.isEmpty()) {
            DetallePedido otroDetalle = new DetallePedido();
            otroDetalle.setIdProducto(productos.get(0).getId());
            otroDetalle.setCantidad(1);
            otroDetalle.setPrecioUnitario(30.0);
            pedidoService.agregarDetalle(pedidoId, otroDetalle);
        }
        
        Pedido pedido = pedidoService.obtenerPorId(pedidoId);
        assertNotNull(pedido);
        assertTrue(pedido.getTotal() > 0);
    }
}