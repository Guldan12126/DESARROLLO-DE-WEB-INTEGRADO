package apf3.ChifaXinYan.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Model.Categoria;
import apf3.ChifaXinYan.Repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Categoría con ID " + id + " no encontrada."));
    }

    @Transactional(rollbackFor = Exception.class)
    public Categoria crearCategoria(Categoria categoria) {
        // Validación de seguridad: evitar nombres duplicados
        if (categoriaRepository.findByNombre(categoria.getNombre()).isPresent()) {
            throw new RuntimeException("Error: Ya existe una categoría con el nombre '" + categoria.getNombre() + "'.");
        }
        return categoriaRepository.save(categoria);
    }

    @Transactional(rollbackFor = Exception.class)
    public Categoria actualizarCategoria(Long id, Categoria datosActualizados) {
        Categoria categoria = obtenerPorId(id);

        // Validación de seguridad: verificar que el nuevo nombre no esté en uso por otra categoría
        Optional<Categoria> existente = categoriaRepository.findByNombre(datosActualizados.getNombre());
        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new RuntimeException("Error: El nombre '" + datosActualizados.getNombre() + "' ya pertenece a otra categoría.");
        }

        categoria.setNombre(datosActualizados.getNombre());
        categoria.setDescripcion(datosActualizados.getDescripcion());
        categoria.setActivo(datosActualizados.isActivo());

        return categoriaRepository.save(categoria);
    }

    @Transactional(rollbackFor = Exception.class)
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Error: No se puede eliminar. Categoría no encontrada.");
        }
        categoriaRepository.deleteById(id);
    }
}