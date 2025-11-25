package gr.hua.dit.officehours.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for <strong>Testing</strong>.
 */
@Controller
public class TestController {

    // ΔΙΕΥΚΡΙΝΗΣΗ: Είναι controller για δοκιμές!
    // Δεν έχει καμία σχέση με το configuration/implementation του error handling.

    public TestController() {}

    /*
    @GetMapping(value = "/test/error/404")
    public String test() {
        return "error/404";
    }

    @GetMapping(value = "/test/error/error")
    public String testErrorError() {
        return "error/error";
    }
    */

    @GetMapping(value = "/test/error/NullPointerException")
    public String testErrorNullPointerException() {
        final Integer a = null;
        final int b = 0;
        final int c = a + b; // Throws NullPointerException.
        return null; // Unreachable.
    }

}
