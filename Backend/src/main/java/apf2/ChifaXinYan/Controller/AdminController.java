package apf1.ChifaXinYan.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Dashboard
    @GetMapping("/dashboard-admin")
    public String dashboardAdmin() {
        return "admin/dashboard-admin";
    }

    // ========== USUARIOS ==========
    @GetMapping("/usuarios/lista")
    public String listaUsuarios() {
        return "admin/usuarios/lista";
    }

    @GetMapping("/usuarios/crear")
    public String crearUsuario() {
        return "admin/usuarios/crear";
    }

    // ✅ CORREGIDO: Con parámetro ID
    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        model.addAttribute("usuarioId", id);
        return "admin/usuarios/editar";
    }

    // ✅ CORREGIDO: Con parámetro ID
    @GetMapping("/usuarios/ver/{id}")
    public String verUsuario(@PathVariable Long id, Model model) {
        model.addAttribute("usuarioId", id);
        return "admin/usuarios/ver";
    }

    @GetMapping("/usuarios/roles")
    public String rolesUsuario() {
        return "admin/usuarios/roles";
    }

    // ========== PRODUCTOS ==========
    @GetMapping("/productos/lista")
    public String listaProductos() {
        return "admin/productos/lista";
    }

    @GetMapping("/productos/crear")
    public String crearProducto() {
        return "admin/productos/crear";
    }

    // ✅ CORREGIDO
    @GetMapping("/productos/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        model.addAttribute("productoId", id);
        return "admin/productos/editar";
    }

    // ✅ CORREGIDO
    @GetMapping("/productos/ver/{id}")
    public String verProducto(@PathVariable Long id, Model model) {
        model.addAttribute("productoId", id);
        return "admin/productos/ver";
    }

    @GetMapping("/productos/categorias")
    public String categoriasProducto() {
        return "admin/productos/categorias";
    }

    @GetMapping("/productos/stock")
    public String stockProducto() {
        return "admin/productos/stock";
    }

    // ========== MESAS ==========
    @GetMapping("/mesas/lista")
    public String listaMesas() {
        return "admin/mesas/lista";
    }

    @GetMapping("/mesas/crear")
    public String crearMesa() {
        return "admin/mesas/crear";
    }

    // ✅ CORREGIDO
    @GetMapping("/mesas/editar/{id}")
    public String editarMesa(@PathVariable Long id, Model model) {
        model.addAttribute("mesaId", id);
        return "admin/mesas/editar";
    }

    @GetMapping("/mesas/mapa")
    public String mapaMesas() {
        return "admin/mesas/mapa";
    }

    // ========== PEDIDOS ==========
    @GetMapping("/pedidos/lista")
    public String listaPedidos() {
        return "admin/pedidos/lista";
    }

    // ✅ CORREGIDO
    @GetMapping("/pedidos/ver/{id}")
    public String verPedido(@PathVariable Long id, Model model) {
        model.addAttribute("pedidoId", id);
        return "admin/pedidos/ver";
    }

    @GetMapping("/pedidos/nuevo")
    public String nuevoPedido() {
        return "admin/pedidos/nuevo";
    }

    @GetMapping("/pedidos/historial")
    public String historialPedidos() {
        return "admin/pedidos/historial";
    }

    // ========== VENTAS ==========
    @GetMapping("/ventas/lista")
    public String listaVentas() {
        return "admin/ventas/lista";
    }

    // ✅ CORREGIDO
    @GetMapping("/ventas/ver/{id}")
    public String verVenta(@PathVariable Long id, Model model) {
        model.addAttribute("ventaId", id);
        return "admin/ventas/ver";
    }

    // ✅ CORREGIDO
    @GetMapping("/ventas/detalle/{id}")
    public String detalleVenta(@PathVariable Long id, Model model) {
        model.addAttribute("ventaId", id);
        return "admin/ventas/detalle";
    }

    // ========== REPORTES ==========
    @GetMapping("/reportes/ventas-diarias")
    public String reporteVentasDiarias() {
        return "admin/reportes/ventas-diarias";
    }

    @GetMapping("/reportes/ventas-semanales")
    public String reporteVentasSemanales() {
        return "admin/reportes/ventas-semanales";
    }

    @GetMapping("/reportes/ventas-mensuales")
    public String reporteVentasMensuales() {
        return "admin/reportes/ventas-mensuales";
    }

    @GetMapping("/reportes/productos-vendidos")
    public String reporteProductosVendidos() {
        return "admin/reportes/productos-vendidos";
    }

    @GetMapping("/reportes/mesas-mas-usadas")
    public String reporteMesasMasUsadas() {
        return "admin/reportes/mesas-mas-usadas";
    }
}