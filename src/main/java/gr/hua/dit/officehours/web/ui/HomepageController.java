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
        /*
        if (AuthUtils.isAuthenticated(???)) {
            return "redirect:/profile";
        }
        */
        return "homepage";
    }
}
