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
        // TODO If user is authenticated, redirect to default view.
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        // TODO If user is not authenticated, redirect to login.
        return "logout";
    }
}
