package apf1.ChifaXinYan.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import apf1.ChifaXinYan.Model.Venta;

@Repository
public class VentaRepository {
    private final ConcurrentHashMap<Long, Venta> ventas = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Venta guardar(Venta venta) {
        if (venta.getId() == null) {
            venta.setId(idGenerator.getAndIncrement());
        }
        ventas.put(venta.getId(), venta);
        return venta;
    }

    public Venta buscarPorId(Long id) {
        return ventas.get(id);
    }

    public List<Venta> listarTodas() {
        List<Venta> resultado = new ArrayList<>();
        for (Venta v : ventas.values()) {
            resultado.add(v);
        }
        return resultado;
    }

    public List<Venta> listarPorFecha(String fecha) {
        List<Venta> resultado = new ArrayList<>();
        for (Venta venta : ventas.values()) {
            if (venta.getFecha().equals(fecha)) {
                resultado.add(venta);
            }
        }
        return resultado;
    }

    public List<Venta> listarPorRangoFechas(String fechaInicio, String fechaFin) {
        List<Venta> resultado = new ArrayList<>();
        for (Venta venta : ventas.values()) {
            String fecha = venta.getFecha();
            if (fecha.compareTo(fechaInicio) >= 0 && fecha.compareTo(fechaFin) <= 0) {
                resultado.add(venta);
            }
        }
        return resultado;
    }

    public double obtenerTotalVentasPorFecha(String fecha) {
        double total = 0;
        for (Venta venta : listarPorFecha(fecha)) {
            total += venta.getMonto();
        }
        return total;
    }
}