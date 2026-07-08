package apf1.ChifaXinYan.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apf1.ChifaXinYan.Service.PedidoService;
import apf1.ChifaXinYan.Service.ProductoService;
import apf1.ChifaXinYan.Service.VentaService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoService productoService;

    // GET /api/dashboard/resumen - Resumen para el ADMIN
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("ventasHoy", ventaService.obtenerTotalVentasHoy());
        dashboard.put("pedidosActivos", pedidoService.listarPedidosActivos().size());
        dashboard.put("productosStockBajo", productoService.listarStockBajo(20).size());
        dashboard.put("fecha", java.time.LocalDate.now().toString());
        
        return ResponseEntity.ok(dashboard);
    }
}