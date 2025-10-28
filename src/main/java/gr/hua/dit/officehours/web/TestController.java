package gr.hua.dit.officehours.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for testing.
 */
@RestController
public class TestController {

    @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public String test() {
        return "Office Hours";
    }
}
