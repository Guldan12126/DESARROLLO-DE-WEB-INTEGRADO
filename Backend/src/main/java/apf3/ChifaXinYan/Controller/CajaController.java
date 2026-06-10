package apf3.ChifaXinYan.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apf3.ChifaXinYan.Model.Caja;
import apf3.ChifaXinYan.Model.MovimientoCaja;
import apf3.ChifaXinYan.Service.CajaService;

@RestController
@RequestMapping("/api/cajas")
@CrossOrigin(origins = "*")
public class CajaController {

    @Autowired
    private CajaService cajaService;

    @GetMapping
    public ResponseEntity<List<Caja>> listarTodas() {
        return ResponseEntity.ok(cajaService.listarTodasLasCajas());
    }

    @GetMapping("/abierta")
    public ResponseEntity<Caja> obtenerCajaAbierta() {
        return cajaService.obtenerCajaAbierta()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/abrir")
    public ResponseEntity<?> abrirCaja(@RequestParam Long usuarioId, @RequestParam double montoApertura) {
        try {
            Caja caja = cajaService.abrirCaja(usuarioId, montoApertura);
            return new ResponseEntity<>(caja, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarCaja(@PathVariable Long id, @RequestParam double montoCierre) {
        try {
            Caja caja = cajaService.cerrarCaja(id, montoCierre);
            return ResponseEntity.ok(caja);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/{id}/movimiento")
    public ResponseEntity<?> registrarMovimiento(
            @PathVariable Long id,
            @RequestParam String tipo,
            @RequestParam double monto,
            @RequestParam String descripcion) {
        try {
            MovimientoCaja movimiento = cajaService.registrarMovimiento(id, tipo, monto, descripcion);
            return new ResponseEntity<>(movimiento, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
