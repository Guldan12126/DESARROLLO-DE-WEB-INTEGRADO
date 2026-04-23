package apf1.ChifaXinYan.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apf1.ChifaXinYan.Model.Mesa;
import apf1.ChifaXinYan.Service.MesaService;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    // GET /api/mesas - Listar todas las mesas
    @GetMapping
    public ResponseEntity<List<Mesa>> listarTodas() {
        return ResponseEntity.ok(mesaService.listarTodas());
    }

    // GET /api/mesas/disponibles - Listar solo mesas libres
    @GetMapping("/disponibles")
    public ResponseEntity<List<Mesa>> listarDisponibles() {
        return ResponseEntity.ok(mesaService.listarDisponibles());
    }

    // GET /api/mesas/{id} - Obtener mesa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Mesa> obtenerPorId(@PathVariable Long id) {
        Mesa mesa = mesaService.obtenerPorId(id);
        if (mesa != null) {
            return ResponseEntity.ok(mesa);
        }
        return ResponseEntity.notFound().build();
    }

    // POST /api/mesas - Crear nueva mesa
    @PostMapping
    public ResponseEntity<Mesa> crearMesa(@RequestBody Mesa mesa) {
        Mesa nuevaMesa = mesaService.crearMesa(mesa);
        return new ResponseEntity<>(nuevaMesa, HttpStatus.CREATED);
    }

    // PUT /api/mesas/{id}/estado?estado=OCUPADA - Actualizar estado de mesa
    @PutMapping("/{id}/estado")
    public ResponseEntity<Mesa> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Mesa mesa = mesaService.actualizarEstado(id, estado);
        if (mesa != null) {
            return ResponseEntity.ok(mesa);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /api/mesas/{id} - Eliminar mesa
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarMesa(@PathVariable Long id) {
        boolean eliminado = mesaService.eliminarMesa(id);
        Map<String, String> response = new HashMap<>();
        if (eliminado) {
            response.put("message", "Mesa eliminada correctamente");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Mesa no encontrada");
        return ResponseEntity.status(404).body(response);
    }
}