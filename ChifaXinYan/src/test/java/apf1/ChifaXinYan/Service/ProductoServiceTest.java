package apf1.ChifaXinYan.Service;

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

import apf1.ChifaXinYan.Model.Producto;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @Test
    @Order(1)
    @DisplayName("1. Debe listar todos los productos")
    public void testListarTodos() {
        var productos = productoService.listarTodos();
        assertNotNull(productos);
        assertTrue(productos.size() >= 16);
    }

    @Test
    @Order(2)
    @DisplayName("2. Debe listar productos por categoria ARROZ")
    public void testListarPorCategoria() {
        var arroces = productoService.listarPorCategoria("ARROZ");
        assertNotNull(arroces);
        assertTrue(arroces.size() >= 3);
        
        for (Producto p : arroces) {
            assertEquals("ARROZ", p.getCategoria());
        }
    }

    @Test
    @Order(3)
    @DisplayName("3. Debe crear un nuevo producto")
    public void testCrearProducto() {
        Producto nuevo = new Producto(null, "Tallarin Saltado", "SALTEADO", 29.00, 50, "/images/tallarin.jpg");
        Producto creado = productoService.crearProducto(nuevo);
        
        assertNotNull(creado.getId());
        assertEquals("Tallarin Saltado", creado.getNombre());
    }

    @Test
    @Order(4)
    @DisplayName("4. Debe actualizar el stock")
    public void testActualizarStock() {
        var productos = productoService.listarTodos();
        Long id = productos.get(0).getId();
        
        Producto actualizado = productoService.actualizarStock(id, 200);
        
        assertNotNull(actualizado);
        assertEquals(200, actualizado.getStock());
    }

    @Test
    @Order(5)
    @DisplayName("5. Debe listar productos con stock bajo")
    public void testListarStockBajo() {
        var stockBajo = productoService.listarStockBajo(20);
        assertNotNull(stockBajo);
    }
}