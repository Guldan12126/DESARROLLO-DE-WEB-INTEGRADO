package apf3.ChifaXinYan.Controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import apf3.ChifaXinYan.Model.Compra;
import apf3.ChifaXinYan.Service.CompraService;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public ResponseEntity<List<Compra>> listar() {
        return ResponseEntity.ok(compraService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<Compra> registrar(@RequestBody Compra compra) {
        return new ResponseEntity<>(compraService.registrarCompra(compra), HttpStatus.CREATED);
    }
}