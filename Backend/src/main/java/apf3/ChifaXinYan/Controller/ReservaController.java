package apf3.ChifaXinYan.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import apf3.ChifaXinYan.Model.Reserva;
import apf3.ChifaXinYan.Service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<Reserva>> listarTodas() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.obtenerPorId(id);
        return reserva != null ? ResponseEntity.ok(reserva) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Reserva> crear(@RequestBody Reserva reserva) {
        return new ResponseEntity<>(reservaService.crearReserva(reserva), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Reserva> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Reserva actualizada = reservaService.actualizarEstado(id, estado);
        return actualizada != null ? ResponseEntity.ok(actualizada) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (reservaService.eliminarReserva(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}