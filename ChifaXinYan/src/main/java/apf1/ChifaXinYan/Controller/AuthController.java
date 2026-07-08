package apf1.ChifaXinYan.Controller;

import apf1.ChifaXinYan.Model.Usuario;
import apf1.ChifaXinYan.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint REST para login desde el frontend.
     *
     * Flujo:
     * 1. Recibe {email, password} en el body
     * 2. Delega la verificación a Spring Security (AuthenticationManager)
     * 3. Spring Security usa BCrypt para comparar la contraseña con la BD
     * 4. Si es válido, devuelve los datos del usuario; si no, devuelve 401
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            // Autenticar con Spring Security (verifica contra la BD + BCrypt)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Guardar la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Obtener los datos completos del usuario desde la BD
            Usuario usuario = usuarioService.obtenerPorEmail(email)
                    .orElseThrow();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Autenticación exitosa");
            response.put("nombre", usuario.getNombre());
            response.put("email", usuario.getEmail());
            response.put("rol", usuario.getRol());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Email o contraseña incorrectos");
            return ResponseEntity.status(401).body(error);
        }
    }

    /**
     * Registro de nuevo usuario (solo accesible por ADMIN en producción).
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Usuario nuevoUsuario) {
        if (usuarioService.obtenerPorEmail(nuevoUsuario.getEmail()).isPresent()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Ya existe un usuario con ese email");
            return ResponseEntity.status(409).body(error);
        }

        Usuario creado = usuarioService.crearUsuario(nuevoUsuario);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Usuario registrado correctamente");
        response.put("id", creado.getId());
        response.put("email", creado.getEmail());
        response.put("rol", creado.getRol());
        return ResponseEntity.status(201).body(response);
    }
}
