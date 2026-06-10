package apf3.ChifaXinYan.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Model.Caja;
import apf3.ChifaXinYan.Model.MovimientoCaja;
import apf3.ChifaXinYan.Model.Usuario;
import apf3.ChifaXinYan.Repository.CajaRepository;
import apf3.ChifaXinYan.Repository.MovimientoCajaRepository;
import apf3.ChifaXinYan.Repository.UsuarioRepository;

@Service
public class CajaService {

    private final CajaRepository cajaRepository;
    private final MovimientoCajaRepository movimientoCajaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CajaService(CajaRepository cajaRepository, MovimientoCajaRepository movimientoCajaRepository, UsuarioRepository usuarioRepository) {
        this.cajaRepository = cajaRepository;
        this.movimientoCajaRepository = movimientoCajaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Caja abrirCaja(Long usuarioId, double montoApertura) {
        // Verificar si ya hay una caja abierta
        if (cajaRepository.findByEstado("ABIERTA").isPresent()) {
            throw new RuntimeException("Ya existe una caja abierta.");
        }

        Usuario cajero = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado con ID: " + usuarioId));

        Caja nuevaCaja = new Caja();
        nuevaCaja.setUsuario(cajero);
        nuevaCaja.setMontoApertura(montoApertura);
        nuevaCaja.setFechaApertura(LocalDateTime.now());
        nuevaCaja.setEstado("ABIERTA");
        return cajaRepository.save(nuevaCaja);
    }

    @Transactional
    public Caja cerrarCaja(Long cajaId, double montoCierre) {
        Caja caja = cajaRepository.findById(cajaId)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada con ID: " + cajaId));

        if (!caja.getEstado().equals("ABIERTA")) {
            throw new RuntimeException("La caja no está abierta para ser cerrada.");
        }

        caja.setMontoCierre(montoCierre);
        caja.setFechaCierre(LocalDateTime.now());
        caja.setEstado("CERRADA");
        return cajaRepository.save(caja);
    }

    @Transactional
    public MovimientoCaja registrarMovimiento(Long cajaId, String tipo, double monto, String descripcion) {
        Caja caja = cajaRepository.findById(cajaId)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada con ID: " + cajaId));

        MovimientoCaja movimiento = new MovimientoCaja();
        movimiento.setCaja(caja);
        movimiento.setTipo(tipo); // "INGRESO" o "EGRESO"
        movimiento.setMonto(monto);
        movimiento.setDescripcion(descripcion);
        return movimientoCajaRepository.save(movimiento);
    }

    @Transactional(readOnly = true)
    public Optional<Caja> obtenerCajaAbierta() {
        return cajaRepository.findByEstado("ABIERTA");
    }

    @Transactional(readOnly = true)
    public List<Caja> listarTodasLasCajas() {
        return cajaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public double calcularBalanceEsperado(Long cajaId) {
        Caja caja = cajaRepository.findById(cajaId)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada con ID: " + cajaId));

        List<MovimientoCaja> movimientos = movimientoCajaRepository.findByCajaId(cajaId);

        double ingresos = movimientos.stream()
                .filter(m -> "INGRESO".equals(m.getTipo()))
                .mapToDouble(MovimientoCaja::getMonto)
                .sum();

        double egresos = movimientos.stream()
                .filter(m -> "EGRESO".equals(m.getTipo()))
                .mapToDouble(MovimientoCaja::getMonto)
                .sum();

        return caja.getMontoApertura() + ingresos - egresos;
    }
}