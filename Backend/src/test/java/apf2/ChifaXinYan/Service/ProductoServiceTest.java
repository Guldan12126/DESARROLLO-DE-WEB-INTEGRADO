package apf2.ChifaXinYan.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import apf2.ChifaXinYan.Model.Producto;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    private static Long productoId;

    @Test
    @Order(1)
    @DisplayName("1. Debe listar todos los productos")
    public void testListarTodos() {
        var productos = productoService.listarTodos();
        assertNotNull(productos);
        assertTrue(productos.size() >= 16, "Debe haber al menos 16 productos iniciales");
        System.out.println("✅ Total productos: " + productos.size());
    }

    @Test
    @Order(2)
    @DisplayName("2. Debe listar productos por categoría CHAUFA")
    public void testListarPorCategoria() {
        var productos = productoService.listarPorCategoria("CHAUFA");
        assertNotNull(productos);
        assertTrue(productos.size() >= 3, "Debe haber al menos 3 productos en categoría CHAUFA");
        
        for (Producto p : productos) {
            assertEquals("CHAUFA", p.getCategoria());
        }
        System.out.println("✅ Productos en categoría CHAUFA: " + productos.size());
    }

    @Test
    @Order(3)
    @DisplayName("3. Debe crear un nuevo producto")
    public void testCrearProducto() {
        Producto nuevo = new Producto();
        nuevo.setNombre("Tallarin Saltado");
        nuevo.setCategoria("FIDEOS");
        nuevo.setPrecio(29.00);
        nuevo.setStock(50);
        nuevo.setImagenUrl("/images/tallarin.jpg");
        
        Producto creado = productoService.crearProducto(nuevo);
        productoId = creado.getId();
        
        assertNotNull(creado.getId());
        assertEquals("Tallarin Saltado", creado.getNombre());
        assertEquals(29.00, creado.getPrecio());
        System.out.println("✅ Producto creado con ID: " + productoId);
    }

    @Test
    @Order(4)
    @DisplayName("4. Debe obtener producto por ID")
    public void testObtenerPorId() {
        Producto producto = productoService.obtenerPorId(productoId);
        
        assertNotNull(producto);
        assertEquals(productoId, producto.getId());
        assertEquals("Tallarin Saltado", producto.getNombre());
        System.out.println("✅ Producto encontrado: " + producto.getNombre());
    }

    @Test
    @Order(5)
    @DisplayName("5. Debe actualizar datos del producto")
    public void testActualizarProducto() {
        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Tallarin Saltado Especial");
        productoActualizado.setCategoria("FIDEOS");
        productoActualizado.setPrecio(32.00);
        productoActualizado.setStock(45);
        productoActualizado.setImagenUrl("/images/tallarin-especial.jpg");
        
        Producto actualizado = productoService.actualizarProducto(productoId, productoActualizado);
        
        assertNotNull(actualizado);
        assertEquals("Tallarin Saltado Especial", actualizado.getNombre());
        assertEquals(32.00, actualizado.getPrecio());
        System.out.println("✅ Producto actualizado correctamente");
    }

    @Test
    @Order(6)
    @DisplayName("6. Debe actualizar el stock del producto")
    public void testActualizarStock() {
        Producto actualizado = productoService.actualizarStock(productoId, 200);
        
        assertNotNull(actualizado);
        assertEquals(200, actualizado.getStock());
        System.out.println("✅ Stock actualizado a: " + actualizado.getStock());
    }

    @Test
    @Order(7)
    @DisplayName("7. Debe listar productos activos")
    public void testListarActivos() {
        var activos = productoService.listarActivos();
        assertNotNull(activos);
        assertTrue(!activos.isEmpty());  // ← Cambiado para evitar warning
        System.out.println("✅ Productos activos: " + activos.size());
    }

    @Test
    @Order(8)
    @DisplayName("8. Debe listar productos con stock bajo (menor a 20)")
    public void testListarStockBajo() {
        var stockBajo = productoService.listarStockBajo(20);
        assertNotNull(stockBajo);
        System.out.println("✅ Productos con stock bajo (<20): " + stockBajo.size());
    }

    @Test
    @Order(9)
    @DisplayName("9. Debe buscar productos por categoría BEBIDAS")
    public void testListarBebidas() {
        var bebidas = productoService.listarPorCategoria("BEBIDAS");
        assertNotNull(bebidas);
        
        for (Producto p : bebidas) {
            assertEquals("BEBIDAS", p.getCategoria());
            assertTrue(p.getPrecio() > 0);
        }
        System.out.println("✅ Bebidas disponibles: " + bebidas.size());
    }

    @Test
    @Order(10)
    @DisplayName("10. Debe eliminar producto correctamente")
    public void testEliminarProducto() {
        Producto temporal = new Producto();
        temporal.setNombre("Producto Temporal");
        temporal.setCategoria("TEST");
        temporal.setPrecio(10.00);
        temporal.setStock(5);
        
        Producto creado = productoService.crearProducto(temporal);
        Long tempId = creado.getId();
        
        boolean eliminado = productoService.eliminarProducto(tempId);
        
        assertTrue(eliminado);
        
        Producto buscado = productoService.obtenerPorId(tempId);
        assertNull(buscado);
        System.out.println("✅ Producto temporal eliminado correctamente");
    }
}