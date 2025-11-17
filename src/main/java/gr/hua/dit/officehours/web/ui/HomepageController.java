package gr.hua.dit.officehours.web.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * UI controller for managing homepage.
 */
@Controller
public class HomepageController {

    @GetMapping("/")
    public String showHomepage() {
        return "homepage";
    }
}
