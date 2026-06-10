package apf3.ChifaXinYan.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import apf3.ChifaXinYan.Model.DetallePedido;
import apf3.ChifaXinYan.Model.Pedido;
import apf3.ChifaXinYan.Service.PedidoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
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

    @PostMapping("/{id}/detalle")
    public ResponseEntity<Pedido> agregarDetalle(
            @PathVariable Long id, 
            @Valid @RequestBody DetallePedido detalle) {
        
        // Validar que el producto y su ID estén presentes en el detalle
        if (detalle.getProducto() == null || detalle.getProducto().getId() == null) {
            throw new IllegalArgumentException("Error: El detalle del pedido debe especificar un producto válido.");
        }
        
        Pedido actualizado = pedidoService.agregarDetalle(
                id, 
                detalle.getProducto().getId(), 
                detalle.getCantidad()
        );
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