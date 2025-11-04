package gr.hua.dit.officehours.web.ui;

import gr.hua.dit.officehours.core.repository.PersonRepository;

import org.springframework.stereotype.Controller;

/**
 * UI controller for managing teacher/student registration.
 */
@Controller
public class RegistrationController {

    private final PersonRepository personRepository;

    public RegistrationController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // TODO Implement registration.
}
