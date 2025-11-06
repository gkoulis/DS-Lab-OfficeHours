package gr.hua.dit.officehours.web.ui;

import gr.hua.dit.officehours.core.model.Person;
import gr.hua.dit.officehours.core.model.PersonType;
import gr.hua.dit.officehours.core.repository.PersonRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * UI controller for managing teacher/student registration.
 */
@Controller
public class RegistrationController {

    private final PersonRepository personRepository;

    public RegistrationController(final PersonRepository personRepository) {
        if (personRepository == null) throw new NullPointerException();
        this.personRepository = personRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(final Model model) {
        // todo if user is auth, redirect to default view.
        final Person person = new Person(
            null,
            "it2023",
            "", "",
            "", "",
            PersonType.STUDENT,
            "",
            null
        );
        model.addAttribute("person", person);
        return "register";
    }

    @PostMapping("/register")
    public String handleFormSubmission(@ModelAttribute("person") Person person) {
        // TODO if form has errors, show form + errors
        // TODO if form is okay, store person, redirect.

        final String emailAddress = person.getEmailAddress();
        final String mobilePhoneNumber = person.getMobilePhoneNumber();
        final String huaId = person.getHuaId();

        if (this.personRepository.existsByEmailAddressIgnoreCase(emailAddress)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email address already exists!");
        }

        if (this.personRepository.existsByMobilePhoneNumber(mobilePhoneNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mobile phone number already exists");
        }

        if (this.personRepository.existsByHuaId(huaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "HUA ID already exists");
        }

        person = this.personRepository.save(person);
        System.out.println(person.toString());
        return "register";
    }
}
