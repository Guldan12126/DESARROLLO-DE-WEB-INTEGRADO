package apf2.ChifaXinYan.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apf2.ChifaXinYan.Enum.MetodoPago;
import apf2.ChifaXinYan.Model.Venta;
import apf2.ChifaXinYan.Service.VentaService;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<Venta>> listarTodas() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(@PathVariable Long id) {
        Venta venta = ventaService.obtenerPorId(id);
        if (venta != null) {
            return ResponseEntity.ok(venta);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Venta>> listarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
        return ResponseEntity.ok(ventaService.listarPorFecha(fecha));
    }

    @GetMapping("/rango")
    public ResponseEntity<List<Venta>> listarPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(ventaService.listarPorRangoFechas(inicio, fin));
    }

    @GetMapping("/total/dia")
    public ResponseEntity<Map<String, Double>> obtenerTotalVentasDia() {
        Map<String, Double> response = new HashMap<>();
        response.put("total", ventaService.obtenerVentasDelDia());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Venta> registrarVenta(
            @RequestParam Long pedidoId,
            @RequestParam String metodoPago,
            @RequestParam Double montoRecibido,
            @RequestParam Long cajeroId) {
        
        Venta venta = ventaService.registrarVenta(pedidoId, MetodoPago.fromString(metodoPago), montoRecibido, cajeroId);
        return new ResponseEntity<>(venta, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> anularVenta(@PathVariable Long id, @RequestParam Long cajeroId) {
        boolean anulada = ventaService.anularVenta(id, cajeroId);
        Map<String, String> response = new HashMap<>();
        if (anulada) {
            response.put("message", "Venta anulada correctamente");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Venta no encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}