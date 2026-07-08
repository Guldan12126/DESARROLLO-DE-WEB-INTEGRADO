package apf1.ChifaXinYan.Controller;

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

import apf1.ChifaXinYan.Model.DetallePedido;
import apf1.ChifaXinYan.Model.Pedido;
import apf1.ChifaXinYan.Service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // GET /api/pedidos - Listar todos los pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarPedidosPorEstado(null));
    }

    // GET /api/pedidos?estado=PENDIENTE - Filtrar por estado
    @GetMapping("/filtrar")
    public ResponseEntity<List<Pedido>> listarPorEstado(@RequestParam(required = false) String estado) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorEstado(estado));
    }

    // GET /api/pedidos/activos - Pedidos activos (no entregados ni cancelados)
    @GetMapping("/activos")
    public ResponseEntity<List<Pedido>> listarActivos() {
        return ResponseEntity.ok(pedidoService.listarPedidosActivos());
    }

    // GET /api/pedidos/cocina - Pedidos para cocina (PENDIENTE y EN_COCINA)
    @GetMapping("/cocina")
    public ResponseEntity<List<Pedido>> listarParaCocina() {
        // Para la cocina: ver pedidos pendientes y en preparación
        List<Pedido> pendientes = pedidoService.listarPedidosPorEstado("PENDIENTE");
        List<Pedido> enCocina = pedidoService.listarPedidosPorEstado("EN_COCINA");
        pendientes.addAll(enCocina);
        return ResponseEntity.ok(pendientes);
    }

    // GET /api/pedidos/{id} - Obtener pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.obtenerPorId(id);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        }
        return ResponseEntity.notFound().build();
    }

    // POST /api/pedidos - Crear nuevo pedido (Mozo)
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoService.crearPedido(pedido);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    // POST /api/pedidos/{id}/detalles - Agregar detalle a pedido (Mozo)
    @PostMapping("/{id}/detalles")
    public ResponseEntity<Pedido> agregarDetalle(@PathVariable Long id, @RequestBody DetallePedido detalle) {
        Pedido pedido = pedidoService.agregarDetalle(id, detalle);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        }
        return ResponseEntity.badRequest().build();
    }

    // PUT /api/pedidos/{id}/estado?estado=EN_COCINA - Actualizar estado (Mozo/Cocina)
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Pedido pedido = pedidoService.actualizarEstado(id, estado);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /api/pedidos/{id} - Cancelar pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancelarPedido(@PathVariable Long id) {
        boolean cancelado = pedidoService.cancelarPedido(id);
        Map<String, String> response = new HashMap<>();
        if (cancelado) {
            response.put("message", "Pedido cancelado correctamente");
            return ResponseEntity.ok(response);
        }
        response.put("message", "No se pudo cancelar el pedido");
        return ResponseEntity.badRequest().body(response);
    }
}