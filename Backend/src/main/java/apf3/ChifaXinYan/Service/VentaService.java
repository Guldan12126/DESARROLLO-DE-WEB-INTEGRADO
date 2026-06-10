package apf3.ChifaXinYan.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Enum.EstadoPedido;
import apf3.ChifaXinYan.Enum.MetodoPago;
import apf3.ChifaXinYan.Model.Caja;
import apf3.ChifaXinYan.Model.Pedido;
import apf3.ChifaXinYan.Model.Venta;
import apf3.ChifaXinYan.Repository.PedidoRepository;
import apf3.ChifaXinYan.Repository.VentaRepository;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CajaService cajaService;

    @Transactional(readOnly = true)
    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Venta> listarPorFecha(LocalDateTime fecha) {
        LocalDateTime inicio = fecha.with(LocalTime.MIN);
        LocalDateTime fin = fecha.with(LocalTime.MAX);
        return ventaRepository.findByFechaBetween(inicio, fin);
    }

    @Transactional(readOnly = true)
    public List<Venta> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaBetween(inicio, fin);
    }

    @Transactional(readOnly = true)
    public Double obtenerTotalVentasPorRango(LocalDateTime inicio, LocalDateTime fin) {
        Double total = ventaRepository.obtenerTotalVentasPorRango(inicio, fin);
        return total != null ? total : 0.0;
    }

    @Transactional(readOnly = true)
    public Double obtenerVentasDelDia() {
        LocalDateTime inicio = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime fin = LocalDateTime.now().with(LocalTime.MAX);
        return obtenerTotalVentasPorRango(inicio, fin);
    }
    @Transactional(readOnly = true)
    public Double obtenerTotalVentasHoy() {
        return obtenerVentasDelDia();
    }

    @Transactional(rollbackFor = Exception.class)
    public Venta registrarVenta(Long pedidoId, MetodoPago metodoPago, Double montoRecibido, Long cajeroId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null) {
            throw new RuntimeException("Pedido no encontrado");
        }
        
        if (pedido.getEstado() != EstadoPedido.LISTO && pedido.getEstado() != EstadoPedido.ENTREGADO) {
            throw new RuntimeException("El pedido no está listo para pagar. Estado actual: " + pedido.getEstado());
        }
        
        // Crear la venta
        Venta venta = new Venta(pedido, metodoPago, montoRecibido, cajeroId);
        
        // Generar número de comprobante
        String numeroComprobante = "B001-" + String.format("%08d", ventaRepository.count() + 1);
        venta.setNumeroComprobante(numeroComprobante);
        
        Venta nuevaVenta = ventaRepository.save(venta);
        
        // Integración con Caja: Registrar ingreso automático en la caja abierta
        Caja cajaAbierta = cajaService.obtenerCajaAbierta()
                .orElseThrow(() -> new RuntimeException("Error: Debe abrir caja antes de registrar una venta."));
        
        cajaService.registrarMovimiento(cajaAbierta.getId(), "INGRESO", nuevaVenta.getMonto(), 
                "Venta: " + nuevaVenta.getNumeroComprobante() + " - Mesa: " + pedido.getIdMesa());

        // Actualizar estado del pedido a PAGADO
        pedido.setEstado(EstadoPedido.PAGADO);
        pedidoRepository.save(pedido);
        
        return nuevaVenta;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean anularVenta(Long id, Long cajeroId) {
        Venta venta = ventaRepository.findById(id).orElse(null);
        if (venta != null) {
            ventaRepository.delete(venta);
            
            // Integración con Caja: Registrar la anulación como un egreso de ajuste
            Caja cajaAbierta = cajaService.obtenerCajaAbierta()
                    .orElseThrow(() -> new RuntimeException("Error: No hay una caja abierta para procesar la anulación."));
            cajaService.registrarMovimiento(cajaAbierta.getId(), "EGRESO", venta.getMonto(), 
                    "Anulación de Venta: " + venta.getNumeroComprobante());

            // Revertir estado del pedido
            Pedido pedido = venta.getPedido();
            if (pedido != null) {
                pedido.setEstado(EstadoPedido.LISTO);
                pedidoRepository.save(pedido);
            }
            return true;
        }
        return false;
    }
}