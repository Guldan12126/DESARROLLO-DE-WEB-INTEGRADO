package apf1.ChifaXinYan.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WebController {
    
    @GetMapping("")
    public String index() {
        return "ChifaXinYan";
    }
    
    @GetMapping("menu")
    public String menu() {
        return "ChifaXinYan";
    }
    
    @GetMapping("dashboard")
    public String dashboard() {
        return "ChifaXinYan";
    }
}
