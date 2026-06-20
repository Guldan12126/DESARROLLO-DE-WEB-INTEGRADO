package apf3.ChifaXinYan.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import apf3.ChifaXinYan.Exception.ResourceNotFoundException;
import apf3.ChifaXinYan.Model.Categoria;
import apf3.ChifaXinYan.Model.Producto;
import apf3.ChifaXinYan.Repository.CategoriaRepository;
import apf3.ChifaXinYan.Repository.ProductoRepository;

/**
 * Test UNITARIO puro de ProductoService.
 * Usa Mockito para simular (mock) las dependencias externas
 * (repositorios, base de datos) sin necesidad de levantar el contexto de Spring.
 * Esto es TDD en su forma más pura: cada test verifica UNA sola unidad de lógica.
 */
@ExtendWith(MockitoExtension.class)
public class ProductoServiceUnitTest {

    // ✅ Mocks: simulamos los repositorios sin tocar la BD real
    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private CategoriaRepository categoriaRepository;

    // ✅ InjectMocks: inyecta los mocks en el servicio a probar
    @InjectMocks
    private ProductoService productoService;

    // Objetos reutilizables entre tests
    private Producto productoMock;
    private Categoria categoriaMock;

    @BeforeEach
    void setUp() {
        // Preparamos datos de prueba antes de cada test
        categoriaMock = new Categoria(1L, "CHAUFA");

        productoMock = new Producto();
        productoMock.setId(1L);
        productoMock.setNombre("Arroz Chaufa");
        productoMock.setPrecio(25.00);
        productoMock.setStock(50);
        productoMock.setCategoria(categoriaMock);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 1: listarTodos()
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("1. listarTodos() - debe retornar la lista del repositorio")
    void testListarTodos_retornaLista() {
        // GIVEN (dado que): el repositorio simulado devuelve 2 productos
        List<Producto> productosMock = Arrays.asList(productoMock, new Producto());
        when(productoRepository.findAll()).thenReturn(productosMock);

        // WHEN (cuando): se llama al servicio
        List<Producto> resultado = productoService.listarTodos();

        // THEN (entonces): se verifica el resultado esperado
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(productoRepository, times(1)).findAll(); // confirmamos que llamó al repo
        System.out.println("✅ [UNIT] listarTodos() retornó " + resultado.size() + " productos");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 2: obtenerPorId() - caso exitoso
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("2. obtenerPorId() - debe retornar el producto cuando existe")
    void testObtenerPorId_exitoso() {
        // GIVEN
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));

        // WHEN
        Producto resultado = productoService.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Arroz Chaufa", resultado.getNombre());
        System.out.println("✅ [UNIT] obtenerPorId(1) retornó: " + resultado.getNombre());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 3: obtenerPorId() - producto no encontrado lanza excepción
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("3. obtenerPorId() - debe lanzar excepción si el producto no existe")
    void testObtenerPorId_noEncontrado_lanzaExcepcion() {
        // GIVEN
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // WHEN / THEN: assertThrows verifica que la excepción se lanza correctamente
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productoService.obtenerPorId(999L)
        );

        assertTrue(exception.getMessage().contains("Producto"));
        System.out.println("✅ [UNIT] obtenerPorId(999) lanzó: " + exception.getMessage());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 4: crearProducto() - guarda correctamente
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("4. crearProducto() - debe guardar y retornar el producto creado")
    void testCrearProducto_exitoso() {
        // GIVEN: simulamos que la categoría existe por ID
        when(categoriaService.obtenerPorId(1L)).thenReturn(categoriaMock);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoMock);

        // WHEN
        Producto nuevo = new Producto();
        nuevo.setNombre("Arroz Chaufa");
        nuevo.setPrecio(25.00);
        nuevo.setStock(50);
        nuevo.setCategoria(categoriaMock);

        Producto resultado = productoService.crearProducto(nuevo);

        // THEN
        assertNotNull(resultado);
        assertEquals("Arroz Chaufa", resultado.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
        System.out.println("✅ [UNIT] crearProducto() guardó: " + resultado.getNombre());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 5: crearProducto() - sin categoría lanza excepción
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("5. crearProducto() - debe lanzar excepción si la categoría es nula")
    void testCrearProducto_sinCategoria_lanzaExcepcion() {
        // GIVEN: producto sin categoría
        Producto sinCategoria = new Producto();
        sinCategoria.setNombre("Sin Categoría");
        sinCategoria.setPrecio(10.00);
        sinCategoria.setCategoria(null);

        // WHEN / THEN
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> productoService.crearProducto(sinCategoria)
        );

        assertTrue(exception.getMessage().contains("categoría"));
        verify(productoRepository, never()).save(any()); // nunca debe guardar
        System.out.println("✅ [UNIT] crearProducto() sin categoría lanzó: " + exception.getMessage());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 6: actualizarStock() - actualiza correctamente
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("6. actualizarStock() - debe actualizar el stock del producto")
    void testActualizarStock_exitoso() {
        // GIVEN
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        Producto resultado = productoService.actualizarStock(1L, 200);

        // THEN
        assertNotNull(resultado);
        assertEquals(200, resultado.getStock());
        System.out.println("✅ [UNIT] actualizarStock(1, 200) -> stock=" + resultado.getStock());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 7: eliminarProducto() - elimina cuando existe
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("7. eliminarProducto() - debe retornar true cuando el producto existe")
    void testEliminarProducto_existe_retornaTrue() {
        // GIVEN
        when(productoRepository.existsById(1L)).thenReturn(true);

        // WHEN
        boolean resultado = productoService.eliminarProducto(1L);

        // THEN
        assertTrue(resultado);
        verify(productoRepository, times(1)).deleteById(1L);
        System.out.println("✅ [UNIT] eliminarProducto(1) -> eliminado correctamente");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 8: eliminarProducto() - retorna false cuando no existe
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("8. eliminarProducto() - debe retornar false cuando el producto no existe")
    void testEliminarProducto_noExiste_retornaFalse() {
        // GIVEN
        when(productoRepository.existsById(999L)).thenReturn(false);

        // WHEN
        boolean resultado = productoService.eliminarProducto(999L);

        // THEN
        assertFalse(resultado);
        verify(productoRepository, never()).deleteById(any()); // nunca debe intentar borrar
        System.out.println("✅ [UNIT] eliminarProducto(999) -> false, no existe");
    }
}
