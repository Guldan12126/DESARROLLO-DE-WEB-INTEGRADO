package apf3.ChifaXinYan.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    // Usamos un mapa en memoria para simular la persistencia de descripciones 
    // mientras no se cree una tabla específica para Roles en la DB.
    private static final Map<String, String> descripciones = new HashMap<>();

    static {
        descripciones.put("ADMIN", "Gestión total del sistema, personal, inventario y reportes gerenciales.");
        descripciones.put("MOZO", "Atención de mesas, toma de pedidos y seguimiento de estados.");
        descripciones.put("CAJERO", "Control de ingresos, cobros presenciales y cierre de caja diario.");
        descripciones.put("COCINA", "Visualización de comandas en tiempo real y despacho de platos.");
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarRoles() {
        List<Map<String, Object>> roles = new ArrayList<>();

        roles.add(crearRol("ADMIN", Arrays.asList("Usuarios", "Productos", "Ventas", "Caja", "Reportes")));
        roles.add(crearRol("MOZO", Arrays.asList("Mesas", "Pedidos")));
        roles.add(crearRol("CAJERO", Arrays.asList("Caja", "Ventas", "Pedidos")));
        roles.add(crearRol("COCINA", Arrays.asList("Tablero Cocina", "Insumos")));

        return ResponseEntity.ok(roles);
    }

    @PutMapping("/{nombreRol}")
    public ResponseEntity<Map<String, String>> actualizarDescripcion(@PathVariable String nombreRol, @RequestBody Map<String, String> body) {
        String nuevaDesc = body.get("descripcion");
        if (descripciones.containsKey(nombreRol.toUpperCase())) {
            descripciones.put(nombreRol.toUpperCase(), nuevaDesc);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Descripción actualizada correctamente");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    private Map<String, Object> crearRol(String nombre, List<String> permisos) {
        Map<String, Object> rol = new HashMap<>();
        rol.put("nombre", nombre);
        rol.put("descripcion", descripciones.get(nombre));
        rol.put("permisos", permisos);
        return rol;
    }
}