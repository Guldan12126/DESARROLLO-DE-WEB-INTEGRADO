package apf3.ChifaXinYan.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Model.Compra;
import apf3.ChifaXinYan.Model.DetalleCompra;
import apf3.ChifaXinYan.Model.Ingrediente;
import apf3.ChifaXinYan.Repository.CompraRepository;
import apf3.ChifaXinYan.Repository.IngredienteRepository;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final IngredienteRepository ingredienteRepository;

    public CompraService(CompraRepository compraRepository, IngredienteRepository ingredienteRepository) {
        this.compraRepository = compraRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public Compra registrarCompra(Compra compra) {
        if (compra.getDetalles() == null || compra.getDetalles().isEmpty()) {
            throw new RuntimeException("La compra debe tener al menos un detalle.");
        }

        double totalCalculado = 0;
        for (DetalleCompra detalle : compra.getDetalles()) {
            detalle.setCompra(compra);
            
            // Lógica de Negocio: Aumentar stock del ingrediente
            Ingrediente ing = ingredienteRepository.findById(detalle.getIngrediente().getId())
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado ID: " + detalle.getIngrediente().getId()));
            
            ing.setStock(ing.getStock() + detalle.getCantidad());
            ingredienteRepository.save(ing);
            
            totalCalculado += (detalle.getCantidad() * detalle.getPrecioUnitario());
        }
        
        compra.setTotal(totalCalculado);
        return compraRepository.save(compra);
    }

    @Transactional(readOnly = true)
    public List<Compra> listarTodas() {
        return compraRepository.findAll();
    }
}