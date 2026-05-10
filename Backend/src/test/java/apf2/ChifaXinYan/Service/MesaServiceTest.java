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

import apf2.ChifaXinYan.Enum.EstadoMesa;
import apf2.ChifaXinYan.Model.Mesa;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class MesaServiceTest {

    @Autowired
    private MesaService mesaService;
    
    private static Long mesaId;
    private static Long mesaOcupadaId;

    @Test
    @Order(1)
    @DisplayName("1. Debe listar todas las mesas")
    public void testListarTodas() {
        var mesas = mesaService.listarTodas();
        assertNotNull(mesas);
        assertTrue(mesas.size() >= 12, "Debe haber al menos 12 mesas iniciales");
        System.out.println("✅ Total mesas: " + mesas.size());
    }

    @Test
    @Order(2)
    @DisplayName("2. Debe listar solo mesas activas")
    public void testListarActivas() {
        var activas = mesaService.listarActivas();
        assertNotNull(activas);
        assertTrue(activas.size() > 0);
        System.out.println("✅ Mesas activas: " + activas.size());
    }

    @Test
    @Order(3)
    @DisplayName("3. Debe listar solo mesas disponibles")
    public void testListarDisponibles() {
        var disponibles = mesaService.listarDisponibles();
        assertNotNull(disponibles);
        for (Mesa mesa : disponibles) {
            assertEquals(EstadoMesa.DISPONIBLE, mesa.getEstado());
        }
        System.out.println("✅ Mesas disponibles: " + disponibles.size());
    }

    @Test
    @Order(4)
    @DisplayName("4. Debe listar solo mesas ocupadas")
    public void testListarOcupadas() {
        var ocupadas = mesaService.listarOcupadas();
        assertNotNull(ocupadas);
        for (Mesa mesa : ocupadas) {
            assertEquals(EstadoMesa.OCUPADA, mesa.getEstado());
        }
        System.out.println("✅ Mesas ocupadas: " + ocupadas.size());
    }

    @Test
    @Order(5)
    @DisplayName("5. Debe listar mesas pendientes de pago")
    public void testListarPendientePago() {
        var pendientes = mesaService.listarPendientePago();
        assertNotNull(pendientes);
        System.out.println("✅ Mesas pendientes de pago: " + pendientes.size());
    }

    @Test
    @Order(6)
    @DisplayName("6. Debe crear una nueva mesa")
    public void testCrearMesa() {
        Mesa nueva = new Mesa();
        nueva.setNumero(20);
        nueva.setCapacidad(6);
        nueva.setUbicacion("Nueva Terraza");
        
        Mesa creada = mesaService.crearMesa(nueva);
        mesaId = creada.getId();
        
        assertNotNull(creada.getId());
        assertEquals(20, creada.getNumero());
        assertEquals(6, creada.getCapacidad());
        assertEquals(EstadoMesa.DISPONIBLE, creada.getEstado());
        System.out.println("✅ Mesa creada con ID: " + mesaId);
    }

    @Test
    @Order(7)
    @DisplayName("7. Debe obtener mesa por ID")
    public void testObtenerPorId() {
        Mesa mesa = mesaService.obtenerPorId(mesaId);
        
        assertNotNull(mesa);
        assertEquals(mesaId, mesa.getId());
        assertEquals(20, mesa.getNumero());
        System.out.println("✅ Mesa encontrada: N° " + mesa.getNumero());
    }

    @Test
    @Order(8)
    @DisplayName("8. Debe obtener mesa por número")
    public void testObtenerPorNumero() {
        Mesa mesa = mesaService.obtenerPorNumero(20);
        
        assertNotNull(mesa);
        assertEquals(20, mesa.getNumero());
        System.out.println("✅ Mesa encontrada por número: " + mesa.getNumero());
    }

    @Test
    @Order(9)
    @DisplayName("9. Debe ocupar una mesa")
    public void testOcuparMesa() {
        // Primero crear una mesa disponible
        Mesa nueva = new Mesa();
        nueva.setNumero(21);
        nueva.setCapacidad(4);
        nueva.setUbicacion("Prueba");
        Mesa creada = mesaService.crearMesa(nueva);
        mesaOcupadaId = creada.getId();
        
        // Ocupar la mesa
        Mesa ocupada = mesaService.ocuparMesa(mesaOcupadaId, 100L); // pedidoId = 100
        
        assertNotNull(ocupada);
        assertEquals(EstadoMesa.OCUPADA, ocupada.getEstado());
        assertEquals(100L, ocupada.getPedidoActualId());
        System.out.println("✅ Mesa ocupada: N° " + ocupada.getNumero());
    }

    @Test
    @Order(10)
    @DisplayName("10. Mesa inexistente debe retornar null")
    public void testBuscarMesaInexistente() {
        Mesa mesa = mesaService.obtenerPorId(9999L);
        assertNull(mesa);
        System.out.println("✅ Mesa inexistente retorna null correctamente");
    }
}