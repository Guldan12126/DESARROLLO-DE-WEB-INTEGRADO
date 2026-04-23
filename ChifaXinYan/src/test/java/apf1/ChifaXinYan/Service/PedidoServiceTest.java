package apf1.ChifaXinYan.Service;

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

import apf1.ChifaXinYan.Model.DetallePedido;
import apf1.ChifaXinYan.Model.Pedido;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PedidoServiceTest {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoService productoService;

    private static Long pedidoId;

    @Test
    @Order(1)
    @DisplayName("1. Debe crear un nuevo pedido")
    public void testCrearPedido() {
        Pedido nuevo = new Pedido();
        nuevo.setIdMesa(1L);
        nuevo.setDetalles(new ArrayList<>());
        
        Pedido creado = pedidoService.crearPedido(nuevo);
        pedidoId = creado.getId();
        
        assertNotNull(creado.getId());
        assertEquals("PENDIENTE", creado.getEstado());
    }

    @Test
    @Order(2)
    @DisplayName("2. Debe agregar detalle al pedido")
    public void testAgregarDetalle() {
        var productos = productoService.listarTodos();
        Long productoId = productos.get(0).getId();
        
        DetallePedido detalle = new DetallePedido();
        detalle.setIdProducto(productoId);
        detalle.setCantidad(2);
        
        Pedido actualizado = pedidoService.agregarDetalle(pedidoId, detalle);
        
        assertNotNull(actualizado);
        assertTrue(actualizado.getDetalles().size() > 0);
    }

    @Test
    @Order(3)
    @DisplayName("3. Debe actualizar el estado del pedido")
    public void testActualizarEstado() {
        Pedido actualizado = pedidoService.actualizarEstado(pedidoId, "EN_COCINA");
        
        assertNotNull(actualizado);
        assertEquals("EN_COCINA", actualizado.getEstado());
    }

    @Test
    @Order(4)
    @DisplayName("4. Debe listar pedidos activos")
    public void testListarPedidosActivos() {
        var activos = pedidoService.listarPedidosActivos();
        assertNotNull(activos);
    }
}