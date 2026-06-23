package apf3.ChifaXinYan.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import apf3.ChifaXinYan.Model.Producto;
import apf3.ChifaXinYan.Service.ProductoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Carpeta donde se guardan las imágenes subidas
    private static final String UPLOAD_DIR = "uploads/productos/";

    // GET /api/productos - Listar todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    // GET /api/productos/activos - Listar solo activos
    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> listarActivos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    // GET /api/productos/categoria/{categoria} - Listar por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoria));
    }

    // GET /api/productos/buscar?nombre=chaufa&categoria=CHAUFA
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombreYCategoria(
            @RequestParam String nombre,
            @RequestParam String categoria) {
        return ResponseEntity.ok(productoService.buscarPorNombreYCategoria(nombre, categoria));
    }

    // GET /api/productos/stock-bajo?limite=20 - Productos con stock bajo
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Producto>> listarStockBajo(@RequestParam(defaultValue = "20") int limite) {
        return ResponseEntity.ok(productoService.listarStockBajo(limite));
    }

    // GET /api/productos/{id} - Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity.notFound().build();
    }

    // POST /api/productos - Crear producto (soporta multipart/form-data con imagen opcional)
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Producto> crearProducto(
            @RequestPart("producto") @Valid Producto producto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        if (imagen != null && !imagen.isEmpty()) {
            String imageUrl = guardarImagen(imagen);
            if (imageUrl != null) {
                producto.setImagenUrl(imageUrl);
            }
        }

        Producto nuevoProducto = productoService.crearProducto(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // PUT /api/productos/{id} - Actualizar producto (soporta multipart/form-data con imagen opcional)
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestPart("producto") @Valid Producto producto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        if (imagen != null && !imagen.isEmpty()) {
            String imageUrl = guardarImagen(imagen);
            if (imageUrl != null) {
                producto.setImagenUrl(imageUrl);
            }
        }

        Producto actualizado = productoService.actualizarProducto(id, producto);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // PUT /api/productos/{id}/stock?nuevoStock=50 - Actualizar stock
    @PutMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(@PathVariable Long id, @RequestParam int nuevoStock) {
        Producto actualizado = productoService.actualizarStock(id, nuevoStock);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /api/productos/{id} - Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarProducto(@PathVariable Long id) {
        boolean eliminado = productoService.eliminarProducto(id);
        Map<String, String> response = new HashMap<>();
        if (eliminado) {
            response.put("message", "Producto eliminado correctamente");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Producto no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Método auxiliar: guarda la imagen en disco y retorna la URL relativa
    private String guardarImagen(MultipartFile imagen) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String extension = "";
            String originalName = imagen.getOriginalFilename();
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, imagen.getBytes());
            return "/" + UPLOAD_DIR + fileName;
        } catch (IOException e) {
            return null;
        }
    }
}
