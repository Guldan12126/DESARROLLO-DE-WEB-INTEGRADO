package apf2.ChifaXinYan.Controller;

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

import apf2.ChifaXinYan.Model.Mesa;
import apf2.ChifaXinYan.Service.MesaService;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @GetMapping
    public ResponseEntity<List<Mesa>> listarTodas() {
        return ResponseEntity.ok(mesaService.listarTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Mesa>> listarActivas() {
        return ResponseEntity.ok(mesaService.listarActivas());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Mesa>> listarDisponibles() {
        return ResponseEntity.ok(mesaService.listarDisponibles());
    }

    @GetMapping("/ocupadas")
    public ResponseEntity<List<Mesa>> listarOcupadas() {
        return ResponseEntity.ok(mesaService.listarOcupadas());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Mesa>> listarPendientePago() {
        return ResponseEntity.ok(mesaService.listarPendientePago());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mesa> obtenerPorId(@PathVariable Long id) {
        Mesa mesa = mesaService.obtenerPorId(id);
        if (mesa != null) {
            return ResponseEntity.ok(mesa);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<Mesa> obtenerPorNumero(@PathVariable Integer numero) {
        Mesa mesa = mesaService.obtenerPorNumero(numero);
        if (mesa != null) {
            return ResponseEntity.ok(mesa);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/contar/disponibles")
    public ResponseEntity<Map<String, Long>> contarDisponibles() {
        Map<String, Long> response = new HashMap<>();
        response.put("disponibles", mesaService.contarDisponibles());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Mesa> crearMesa(@RequestBody Mesa mesa) {
        Mesa nueva = mesaService.crearMesa(mesa);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mesa> actualizarMesa(@PathVariable Long id, @RequestBody Mesa mesa) {
        Mesa actualizada = mesaService.actualizarMesa(id, mesa);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/ocupar")
    public ResponseEntity<Mesa> ocuparMesa(@PathVariable Long id, @RequestParam Long pedidoId) {
        Mesa actualizada = mesaService.ocuparMesa(id, pedidoId);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/pendiente-pago")
    public ResponseEntity<Mesa> marcarPendientePago(@PathVariable Long id) {
        Mesa actualizada = mesaService.marcarPendientePago(id);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/liberar")
    public ResponseEntity<Mesa> liberarMesa(@PathVariable Long id) {
        Mesa actualizada = mesaService.liberarMesa(id);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarMesa(@PathVariable Long id) {
        boolean eliminada = mesaService.eliminarMesa(id);
        Map<String, String> response = new HashMap<>();
        if (eliminada) {
            response.put("message", "Mesa eliminada correctamente");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Mesa no encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}