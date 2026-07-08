package apf3.ChifaXinYan.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf3.ChifaXinYan.Exception.ResourceNotFoundException;
import apf3.ChifaXinYan.Model.Categoria;
import apf3.ChifaXinYan.Model.Producto;
import apf3.ChifaXinYan.Repository.CategoriaRepository;
import apf3.ChifaXinYan.Repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService; 
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaService categoriaService, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // Se actualiza para usar findByCategoria_Nombre
    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoria(String nombreCategoria) {
        return productoRepository.findByCategoria_Nombre(nombreCategoria);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombreYCategoria(String nombre, String nombreCategoria) {
        return productoRepository.findByNombreContainingIgnoreCaseAndCategoria_Nombre(nombre, nombreCategoria);
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
    }

    @Transactional(readOnly = true)
    public List<Producto> listarStockBajo(int limite) {
        return productoRepository.findByStockLessThan(limite);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    // Método auxiliar para obtener una categoría gestionada
    private Categoria getManagedCategory(Categoria categoria) {
        if (categoria == null) {
            throw new RuntimeException("Error: La categoría del producto no puede ser nula.");
        }

        // Si la categoría entrante ya tiene un ID, intentamos obtenerla de la BD
        if (categoria.getId() != null) {
            return categoriaService.obtenerPorId(categoria.getId()); // Lanza excepción si no la encuentra
        }

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new RuntimeException("Error: El nombre de la categoría es obligatorio al crear una nueva categoría.");
        }

        // Si no tiene ID, intentamos buscarla por nombre. Si no existe, la creamos.
        return categoriaRepository.findByNombre(categoria.getNombre())
                .orElseGet(() -> categoriaService.crearCategoria(new Categoria(null, categoria.getNombre())));
    }

    @Transactional(rollbackFor = Exception.class)
    public Producto crearProducto(Producto producto) {
        // Aseguramos que la categoría sea una entidad gestionada
        Categoria managedCategoria = getManagedCategory(producto.getCategoria());
        producto.setCategoria(managedCategoria);
        return productoRepository.save(producto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        // Aseguramos que la categoría sea una entidad gestionada
        Categoria managedCategoria = getManagedCategory(productoActualizado.getCategoria());

        // Actualizamos solo los campos permitidos
        existente.setNombre(productoActualizado.getNombre());
        existente.setCategoria(managedCategoria);
        existente.setPrecio(productoActualizado.getPrecio());
        existente.setStock(productoActualizado.getStock());
        existente.setImagenUrl(productoActualizado.getImagenUrl());
        existente.setDescripcion(productoActualizado.getDescripcion());
        return productoRepository.save(existente);
    }

    @Transactional(rollbackFor = Exception.class)
    public Producto actualizarStock(Long id, int nuevoStock) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        
        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}