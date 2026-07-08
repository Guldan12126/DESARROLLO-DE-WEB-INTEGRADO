package apf3.ChifaXinYan.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Model.Receta;
import apf3.ChifaXinYan.Repository.RecetaRepository;

@Service
public class RecetaService {

    private final RecetaRepository recetaRepository;

    public RecetaService(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @Transactional(readOnly = true)
    public List<Receta> listarPorProducto(Long productoId) {
        return recetaRepository.findByProductoId(productoId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Receta asignarIngrediente(Receta receta) {
        if (receta.getProducto() == null || receta.getIngrediente() == null) {
            throw new RuntimeException("Debe especificar producto e ingrediente para la receta.");
        }
        return recetaRepository.save(receta);
    }

    @Transactional(rollbackFor = Exception.class)
    public void eliminar(Long id) {
        recetaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Receta> listarTodas() {
        return recetaRepository.findAll();
    }
}