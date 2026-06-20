package apf3.ChifaXinYan.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apf3.ChifaXinYan.Dto.NotificacionDto;
import apf3.ChifaXinYan.Service.NotificacionService;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public ResponseEntity<List<NotificacionDto>> listar(@RequestParam String rol) {
        return ResponseEntity.ok(notificacionService.listarPorRol(rol));
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<List<NotificacionDto>> listarNoLeidas(@RequestParam String rol) {
        return ResponseEntity.ok(notificacionService.listarNoLeidasPorRol(rol));
    }

    @PutMapping("/marcar-leidas")
    public ResponseEntity<Map<String, String>> marcarComoLeidas(@RequestParam String rol) {
        notificacionService.marcarComoLeidas(rol);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Notificaciones marcadas como leidas");
        return ResponseEntity.ok(response);
    }
}
