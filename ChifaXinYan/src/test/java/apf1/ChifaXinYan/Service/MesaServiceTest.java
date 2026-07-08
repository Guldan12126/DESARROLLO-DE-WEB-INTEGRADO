package apf1.ChifaXinYan.Service;

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

import apf1.ChifaXinYan.Model.Mesa;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MesaServiceTest {

    @Autowired
    private MesaService mesaService;

    @Test
    @Order(1)
    @DisplayName("1. Debe listar todas las mesas")
    public void testListarTodas() {
        var mesas = mesaService.listarTodas();
        assertNotNull(mesas);
        assertTrue(mesas.size() >= 8);
    }

    @Test
    @Order(2)
    @DisplayName("2. Debe listar solo mesas disponibles")
    public void testListarDisponibles() {
        var disponibles = mesaService.listarDisponibles();
        assertNotNull(disponibles);
        for (Mesa mesa : disponibles) {
            assertEquals("LIBRE", mesa.getEstado());
        }
    }

    @Test
    @Order(3)
    @DisplayName("3. Debe crear una nueva mesa")
    public void testCrearMesa() {
        Mesa nueva = new Mesa(null, 15, "LIBRE", 4);
        Mesa creada = mesaService.crearMesa(nueva);
        
        assertNotNull(creada.getId());
        assertEquals(15, creada.getNumero());
    }

    @Test
    @Order(4)
    @DisplayName("4. Debe actualizar estado de mesa")
    public void testActualizarEstado() {
        Mesa nueva = mesaService.crearMesa(new Mesa(null, 16, "LIBRE", 2));
        Long id = nueva.getId();
        
        Mesa actualizada = mesaService.actualizarEstado(id, "OCUPADA");
        
        assertNotNull(actualizada);
        assertEquals("OCUPADA", actualizada.getEstado());
    }

    @Test
    @Order(5)
    @DisplayName("5. Mesa inexistente debe retornar null")
    public void testBuscarMesaInexistente() {
        Mesa mesa = mesaService.obtenerPorId(999L);
        assertNull(mesa);
    }
}