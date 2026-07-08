package apf3.ChifaXinYan.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apf3.ChifaXinYan.Enum.EstadoPedido;
import apf3.ChifaXinYan.Model.Pedido;
import apf3.ChifaXinYan.Service.PedidoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Pedido>> listarActivos() {
        return ResponseEntity.ok(pedidoService.listarPedidosActivos());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pedidoService.listarPorEstado(EstadoPedido.valueOf(estado.toUpperCase())));
    }

    @GetMapping("/mesa/{idMesa}")
    public ResponseEntity<List<Pedido>> listarPorMesa(@PathVariable Long idMesa) {
        return ResponseEntity.ok(pedidoService.listarPorMesa(idMesa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@Valid @RequestBody Pedido pedido) {
        Pedido nuevo = pedidoService.crearPedido(pedido);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    /**
     * Agrega un detalle (producto + cantidad) a un pedido existente.
     * Recibe un Map genérico en lugar de una entidad JPA para evitar que Jackson
     * cree instancias transitorias de DetallePedido/Pedido/Producto que
     * contaminen el contexto de persistencia de Hibernate.
     * 
     * Formatos aceptados:
     *   { "productoId": 1, "cantidad": 2 }
     *   { "producto": { "id": 1 }, "cantidad": 2 }
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/{id}/detalle")
    public ResponseEntity<Pedido> agregarDetalle(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        // Extraer productoId desde cualquiera de los dos formatos
        Long productoId;
        Object productoObj = body.get("producto");
        if (productoObj instanceof Map) {
            Object idObj = ((Map<String, Object>) productoObj).get("id");
            if (idObj == null) {
                throw new IllegalArgumentException("Error: El detalle del pedido debe especificar un producto válido (producto.id).");
            }
            productoId = Long.valueOf(idObj.toString());
        } else if (body.containsKey("productoId")) {
            productoId = Long.valueOf(body.get("productoId").toString());
        } else {
            throw new IllegalArgumentException("Error: El detalle del pedido debe especificar un producto válido (productoId o producto.id).");
        }

        int cantidad = body.containsKey("cantidad")
                ? Integer.parseInt(body.get("cantidad").toString())
                : 0;

        if (cantidad < 1) {
            throw new IllegalArgumentException("Error: La cantidad debe ser al menos 1.");
        }

        Pedido actualizado = pedidoService.agregarDetalle(id, productoId, cantidad);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/estado") // Usamos PATCH para actualizaciones parciales
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Pedido actualizado = pedidoService.actualizarEstado(id, estado);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}