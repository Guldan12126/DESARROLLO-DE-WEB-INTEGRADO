package apf2.ChifaXinYan.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apf2.ChifaXinYan.Enum.EstadoPedido;
import apf2.ChifaXinYan.Model.DetallePedido;
import apf2.ChifaXinYan.Model.Pedido;
import apf2.ChifaXinYan.Service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

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
        return ResponseEntity.ok(pedidoService.listarPorEstado(EstadoPedido.fromString(estado)));
    }

    @GetMapping("/mesa/{idMesa}")
    public ResponseEntity<List<Pedido>> listarPorMesa(@PathVariable Long idMesa) {
        return ResponseEntity.ok(pedidoService.listarPorMesa(idMesa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.obtenerPorId(id);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
        Pedido nuevo = pedidoService.crearPedido(pedido);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/detalle")
    public ResponseEntity<Pedido> agregarDetalle(@PathVariable Long id, @RequestBody DetallePedido detalle) {
        Pedido actualizado = pedidoService.agregarDetalle(id, detalle);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        // Convertir String a EstadoPedido y luego a String para el service
        EstadoPedido estadoEnum = EstadoPedido.fromString(estado);
        Pedido actualizado = pedidoService.actualizarEstado(id, estadoEnum.name()); // ← pasar .name()
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarPedido(@PathVariable Long id) {
        boolean eliminado = pedidoService.eliminarPedido(id);
        Map<String, String> response = new HashMap<>();
        if (eliminado) {
            response.put("message", "Pedido eliminado correctamente");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Pedido no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}