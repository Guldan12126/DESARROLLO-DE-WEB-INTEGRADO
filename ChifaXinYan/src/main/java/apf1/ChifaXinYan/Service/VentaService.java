package apf1.ChifaXinYan.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apf1.ChifaXinYan.Model.Venta;
import apf1.ChifaXinYan.Repository.VentaRepository;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private PedidoService pedidoService;  

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Venta registrarVenta(Venta venta) {
        venta.setFecha(LocalDate.now().format(formatter));
        venta.setEstado("PAGADO");
        return ventaRepository.guardar(venta);
    }

    public List<Venta> listarVentasPorFecha(String fecha) {
        return ventaRepository.listarPorFecha(fecha);
    }

    public double obtenerTotalVentasHoy() {
        String hoy = LocalDate.now().format(formatter);
        return ventaRepository.obtenerTotalVentasPorFecha(hoy);
    }

    public Map<String, Object> obtenerReporteDiario(String fecha) {
        Map<String, Object> reporte = new HashMap<>();
        List<Venta> ventas = ventaRepository.listarPorFecha(fecha);
        
        double total = ventas.stream().mapToDouble(Venta::getMonto).sum();
        long cantidad = ventas.size();
        
        reporte.put("fecha", fecha);
        reporte.put("totalVentas", total);
        reporte.put("cantidadTransacciones", cantidad);
        reporte.put("ventas", ventas);
        
        return reporte;
    }

    public Map<String, Object> obtenerDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        String hoy = LocalDate.now().format(formatter);
        
        dashboard.put("ventasHoy", obtenerTotalVentasHoy());
        dashboard.put("pedidosActivos", pedidoService.listarPedidosActivos().size());
        dashboard.put("fecha", hoy);
        
        return dashboard;
    }
}