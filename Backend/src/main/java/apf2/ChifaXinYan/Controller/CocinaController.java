package apf2.ChifaXinYan.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cocina")
public class CocinaController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "cocina/dashboard-cocina";
    }
}