package apf3.ChifaXinYan.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apf3.ChifaXinYan.Model.Caja;
import apf3.ChifaXinYan.Model.MovimientoCaja;
import apf3.ChifaXinYan.Repository.MovimientoCajaRepository;
import apf3.ChifaXinYan.Service.CajaService;

@RestController
@RequestMapping("/api/cajas")
public class CajaController {

    private final CajaService cajaService;
    private final MovimientoCajaRepository movimientoCajaRepository;

    public CajaController(CajaService cajaService, MovimientoCajaRepository movimientoCajaRepository) {
        this.cajaService = cajaService;
        this.movimientoCajaRepository = movimientoCajaRepository;
    }

    // GET /api/cajas - Lista todas las cajas
    @GetMapping
    public ResponseEntity<List<Caja>> listarTodas() {
        return ResponseEntity.ok(cajaService.listarTodasLasCajas());
    }

    // GET /api/cajas/abierta - Obtiene la caja actualmente abierta
    @GetMapping("/abierta")
    public ResponseEntity<Caja> obtenerCajaAbierta() {
        return cajaService.obtenerCajaAbierta()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    // GET /api/cajas/movimientos - Lista TODOS los movimientos de todas las cajas
    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoCaja>> listarTodosLosMovimientos() {
        return ResponseEntity.ok(movimientoCajaRepository.findAll());
    }

    // GET /api/cajas/{id}/movimientos - Lista los movimientos de una caja específica
    @GetMapping("/{id}/movimientos")
    public ResponseEntity<List<MovimientoCaja>> listarMovimientosPorCaja(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoCajaRepository.findByCajaId(id));
    }

    // POST /api/cajas/abrir?usuarioId=&montoApertura= - Abre una nueva caja
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

    // POST /api/cajas/{id}/cerrar?montoCierre= - Cierra una caja
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

    // POST /api/cajas/{id}/movimiento - Registra un movimiento en una caja
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