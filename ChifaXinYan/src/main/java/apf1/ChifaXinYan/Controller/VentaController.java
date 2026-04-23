package apf1.ChifaXinYan.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apf1.ChifaXinYan.Model.Venta;
import apf1.ChifaXinYan.Service.VentaService;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // POST /api/ventas - Registrar una venta (Mozo/Admin)
    @PostMapping
    public ResponseEntity<Venta> registrarVenta(@RequestBody Venta venta) {
        Venta nuevaVenta = ventaService.registrarVenta(venta);
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

    // GET /api/ventas/fecha?fecha=2026-04-22 - Ventas por fecha
    @GetMapping("/fecha")
    public ResponseEntity<List<Venta>> listarPorFecha(@RequestParam String fecha) {
        return ResponseEntity.ok(ventaService.listarVentasPorFecha(fecha));
    }

    // GET /api/ventas/reporte/diario?fecha=2026-04-22 - Reporte diario
    @GetMapping("/reporte/diario")
    public ResponseEntity<Map<String, Object>> reporteDiario(@RequestParam String fecha) {
        return ResponseEntity.ok(ventaService.obtenerReporteDiario(fecha));
    }

    // GET /api/ventas/total-hoy - Total de ventas de hoy
    @GetMapping("/total-hoy")
    public ResponseEntity<Map<String, Object>> totalVentasHoy() {
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("fecha", java.time.LocalDate.now().toString());
        response.put("total", ventaService.obtenerTotalVentasHoy());
        return ResponseEntity.ok(response);
    }
}
