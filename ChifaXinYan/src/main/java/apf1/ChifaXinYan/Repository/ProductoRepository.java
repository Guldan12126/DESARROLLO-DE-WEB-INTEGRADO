package apf1.ChifaXinYan.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import apf1.ChifaXinYan.Model.Producto;

@Repository
public class ProductoRepository {
    private final ConcurrentHashMap<Long, Producto> productos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Datos iniciales - Menú del Chifa Xin Yan
    public ProductoRepository() {
        // Arroces
        guardar(new Producto(null, "Arroz Chaufa", "ARROZ", 28.50, 100, "https://picsum.photos/id/108/200/150"));
        guardar(new Producto(null, "Arroz Chaufa Especial", "ARROZ", 35.00, 80, "https://picsum.photos/id/109/200/150"));
        guardar(new Producto(null, "Arroz Chaufa de Mariscos", "ARROZ", 38.00, 60, "https://picsum.photos/id/110/200/150"));
        
        // Saltados
        guardar(new Producto(null, "Lomo Saltado", "SALTEADO", 32.00, 90, "https://picsum.photos/id/111/200/150"));
        guardar(new Producto(null, "Pollo Saltado", "SALTEADO", 28.00, 95, "https://picsum.photos/id/112/200/150"));
        guardar(new Producto(null, "Chancho Saltado", "SALTEADO", 30.00, 70, "https://picsum.photos/id/113/200/150"));
        
        // Sopas
        guardar(new Producto(null, "Wantán Frito", "SOPA", 18.00, 120, "https://picsum.photos/id/114/200/150"));
        guardar(new Producto(null, "Sopa Wantán", "SOPA", 22.00, 85, "https://picsum.photos/id/115/200/150"));
        guardar(new Producto(null, "Chupe de Camarones", "SOPA", 35.00, 50, "https://picsum.photos/id/116/200/150"));
        
        // Entradas
        guardar(new Producto(null, "Rollos Primavera", "ENTRADA", 15.00, 150, "https://picsum.photos/id/117/200/150"));
        guardar(new Producto(null, "Wantán Frito", "ENTRADA", 12.00, 160, "https://picsum.photos/id/118/200/150"));
        guardar(new Producto(null, "Siu Mai", "ENTRADA", 14.00, 140, "https://picsum.photos/id/119/200/150"));
        
        // Bebidas
        guardar(new Producto(null, "Coca Cola 400 ml", "BEBIDA", 4.00, 200, "/Productos/Bebidas/Coca cola Clasica.png"));
        guardar(new Producto(null, "Coca Cola 1 L", "BEBIDA", 7.00, 200, "https://picsum.photos/id/121/200/150"));
        guardar(new Producto(null, "Kola Amarilla 500 ml", "BEBIDA", 3.00, 150, "https://picsum.photos/id/122/200/150"));
        guardar(new Producto(null, "Jarra Limonada con Hierba Luisa", "BEBIDA", 27.00, 150, "https://picsum.photos/id/122/200/150"));

        
        // Postres
        guardar(new Producto(null, "Suspiro Limeño", "POSTRE", 12.00, 60, "https://picsum.photos/id/123/200/150"));
        guardar(new Producto(null, "Tres Leches", "POSTRE", 10.00, 70, "https://picsum.photos/id/124/200/150"));
    }

    public Producto guardar(Producto producto) {
        if (producto.getId() == null) {
            producto.setId(idGenerator.getAndIncrement());
        }
        productos.put(producto.getId(), producto);
        return producto;
    }

    public Producto buscarPorId(Long id) {
        return productos.get(id);
    }

    public List<Producto> listarTodos() {
        return new ArrayList<>(productos.values());
    }

    public List<Producto> listarPorCategoria(String categoria) {
        return productos.values().stream()
                .filter(p -> p.getCategoria().equals(categoria))
                .collect(Collectors.toList());
    }

    public List<Producto> listarStockBajo(int limite) {
        return productos.values().stream()
                .filter(p -> p.getStock() <= limite)
                .collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        productos.remove(id);
    }

    public void actualizarStock(Long id, int nuevoStock) {
        Producto producto = productos.get(id);
        if (producto != null) {
            producto.setStock(nuevoStock);
            productos.put(id, producto);
        }
    }
}
