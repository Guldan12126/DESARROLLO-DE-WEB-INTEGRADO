package apf2.ChifaXinYan.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apf2.ChifaXinYan.Model.Producto;
import apf2.ChifaXinYan.Repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarStockBajo(int limite) {
        return productoRepository.findByStockLessThan(limite);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    @Transactional(rollbackFor = Exception.class)
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto existente = productoRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNombre(productoActualizado.getNombre());
            existente.setCategoria(productoActualizado.getCategoria());
            existente.setPrecio(productoActualizado.getPrecio());
            existente.setStock(productoActualizado.getStock());
            existente.setImagenUrl(productoActualizado.getImagenUrl());
            existente.setDescripcion(productoActualizado.getDescripcion());
            return productoRepository.save(existente);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Producto actualizarStock(Long id, int nuevoStock) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            producto.setStock(nuevoStock);
            return productoRepository.save(producto);
        }
        return null;
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