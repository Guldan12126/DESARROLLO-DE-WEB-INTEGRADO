package apf1.ChifaXinYan.Service;

import apf1.ChifaXinYan.Model.Producto;
import apf1.ChifaXinYan.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.listarTodos();
    }

    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.listarPorCategoria(categoria);
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.buscarPorId(id);
    }

    public Producto crearProducto(Producto producto) {
        return productoRepository.guardar(producto);
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto existente = productoRepository.buscarPorId(id);
        if (existente != null) {
            existente.setNombre(productoActualizado.getNombre());
            existente.setCategoria(productoActualizado.getCategoria());
            existente.setPrecio(productoActualizado.getPrecio());
            existente.setStock(productoActualizado.getStock());
            existente.setImagenUrl(productoActualizado.getImagenUrl());
            return productoRepository.guardar(existente);
        }
        return null;
    }

    public Producto actualizarStock(Long id, int nuevoStock) {
        Producto producto = productoRepository.buscarPorId(id);
        if (producto != null) {
            producto.setStock(nuevoStock);
            return productoRepository.guardar(producto);
        }
        return null;
    }

    public List<Producto> listarStockBajo(int limite) {
        return productoRepository.listarStockBajo(limite);
    }

    public boolean eliminarProducto(Long id) {
        Producto producto = productoRepository.buscarPorId(id);
        if (producto != null) {
            productoRepository.eliminar(id);
            return true;
        }
        return false;
    }
}
