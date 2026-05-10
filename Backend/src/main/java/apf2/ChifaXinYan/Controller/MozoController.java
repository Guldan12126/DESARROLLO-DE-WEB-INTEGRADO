package apf1.ChifaXinYan.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mozo")
public class MozoController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "mozo/dashboard-mozo";
    }
}