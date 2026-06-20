package apf3.ChifaXinYan.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apf3.ChifaXinYan.Service.PedidoService;
import apf3.ChifaXinYan.Service.ProductoService;
import apf3.ChifaXinYan.Service.VentaService;

@RestController
@RequestMapping("/api/reportes")
public class DashboardController {

    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final VentaService ventaService;

    public DashboardController(ProductoService productoService, PedidoService pedidoService, VentaService ventaService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
        this.ventaService = ventaService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        
        Map<String, Object> data = new HashMap<>();

        // Si no hay fechas, calculamos un rango por defecto (últimos 7 días)
        if (inicio == null) inicio = LocalDateTime.now().minusDays(7);
        if (fin == null) fin = LocalDateTime.now();

        // KPIs
        data.put("totalVentasHoy", ventaService.obtenerVentasDelDia());
        data.put("pedidosActivos", pedidoService.listarPedidosActivos().size());
        data.put("productosStockBajo", productoService.listarStockBajo(10).size());
        
        // En un caso real, aquí usarías 'inicio' y 'fin' en tus servicios:
        // data.put("totalVentasRango", ventaService.obtenerTotalVentasPorRango(inicio, fin));
        
        // Datos simulados para el gráfico (En un caso real, esto vendría de una consulta agregada por fecha)
        data.put("ventasSemanalLabels", List.of("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"));
        data.put("ventasSemanalData", List.of(1200, 1900, 1500, 2100, 2800, 4500, 3800));
        
        // Distribución por categorías
        data.put("categoriasLabels", List.of("Chaufa", "Sopas", "Bebidas", "Tallarines"));
        data.put("categoriasData", List.of(45, 15, 25, 15));

        return ResponseEntity.ok(data);
    }
}