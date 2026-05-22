package apf2.ChifaXinYan.Controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

<<<<<<< HEAD
import apf2.ChifaXinYan.Service.MesaService;
import apf2.ChifaXinYan.Service.PedidoService;
import apf2.ChifaXinYan.Service.ProductoService;
import apf2.ChifaXinYan.Service.VentaService;

=======
import apf2.ChifaXinYan.Service.PedidoService;
import apf2.ChifaXinYan.Service.ProductoService;
import apf2.ChifaXinYan.Service.VentaService;
>>>>>>> 0a71042947e863f58f51a24e38f758253f8febe9

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
    
    @Autowired
    private MesaService mesaService;

    // GET /api/dashboard/resumen - Resumen para el ADMIN
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Ventas de hoy
        Double ventasHoy = ventaService.obtenerVentasDelDia();
        dashboard.put("ventasHoy", ventasHoy != null ? ventasHoy : 0.0);
        
        // Pedidos activos
        dashboard.put("pedidosActivos", pedidoService.listarPedidosActivos().size());
        
        // Productos con stock bajo
        dashboard.put("productosStockBajo", productoService.listarStockBajo(20).size());
        
        // Mesas
        dashboard.put("mesasDisponibles", mesaService.contarDisponibles());
        dashboard.put("mesasOcupadas", mesaService.listarOcupadas().size());
        dashboard.put("mesasPendientePago", mesaService.listarPendientePago().size());
        
        // Total mesas
        dashboard.put("totalMesas", mesaService.listarTodas().size());
        
        // Fecha actual
        dashboard.put("fecha", LocalDate.now().toString());
        
        return ResponseEntity.ok(dashboard);
    }
}