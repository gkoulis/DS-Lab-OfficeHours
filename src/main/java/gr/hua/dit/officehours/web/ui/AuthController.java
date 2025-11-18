package gr.hua.dit.officehours.web.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * UI controller for user authentication (login and logout).
 */
@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        /*
        if (AuthUtils.isAuthenticated(???)) {
            return "redirect:/profile";
        }
        */
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        /*
        if (AuthUtils.isAnonymous(???)) {
            return "redirect:/login";
        }
        */
        return "logout";
    }
}
