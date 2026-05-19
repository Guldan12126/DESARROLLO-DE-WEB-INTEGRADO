package apf2.ChifaXinYan.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import apf2.ChifaXinYan.Model.Usuario;
import apf2.ChifaXinYan.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        System.out.println("Intentando login: " + username);

        Usuario usuario = usuarioService.login(username, password);

        if (usuario != null) {
            session.setAttribute("userId", usuario.getId());
            session.setAttribute("nombre", usuario.getNombre());
            session.setAttribute("email", usuario.getEmail());
            session.setAttribute("rol", usuario.getRol());

            System.out.println("Login exitoso: " + usuario.getNombre() + " - Rol: " + usuario.getRol());

            switch (usuario.getRol()) {
                case "ADMIN":
                    return "redirect:/admin/dashboard-admin";
                case "MOZO":
                    return "redirect:/mozo/dashboard-mozo";
                case "CAJERO":
                    return "redirect:/cajero/dashboard-cajero";
                case "COCINA":
                    return "redirect:/cocina/dashboard-cocina";
                default:
                    return "redirect:/login?error";
            }
        }

        System.out.println("Login fallido para: " + username);
        return "redirect:/login?error";
    }

    // ✅ NUEVO: Cerrar sesión
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destruye la sesión
        return "redirect:/login";
    }
}