package gr.hua.dit.officehours.web.ui;

import gr.hua.dit.officehours.core.model.PersonType;
import gr.hua.dit.officehours.core.service.PersonService;
import gr.hua.dit.officehours.core.service.model.CreatePersonRequest;
import gr.hua.dit.officehours.core.service.model.CreatePersonResult;
import gr.hua.dit.officehours.core.service.model.PersonView;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * UI controller for managing teacher/student registration.
 */
@Controller
public class RegistrationController {

    private final PersonService personService;

    public RegistrationController(final PersonService personService) {
        if (personService == null) throw new NullPointerException();
        this.personService = personService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(final Model model) {
        // TODO if user is authenticated, redirect to tickets
        // Initial data for the form.
        final CreatePersonRequest createPersonRequest = new CreatePersonRequest(PersonType.STUDENT, "", "", "", "", "", "");
        model.addAttribute("createPersonRequest", createPersonRequest);
        return "register";
    }

    @PostMapping("/register")
    public String handleFormSubmission(
        @ModelAttribute("createPersonRequest") final CreatePersonRequest createPersonRequest,
        final Model model
    ) {
        // TODO if user is authenticated, redirect to tickets
        // TODO Validate form (email format, size, blank, etc)
        // TODO if form has errors, show the form (with pre-filled data)
        // TODO Try to create Person.
        // TODO Handle Person creation failure.
        // TODO Redirect to login.
        final CreatePersonResult createPersonResult = this.personService.createPerson(createPersonRequest);
        if (createPersonResult.created()) {
            return "redirect:/login"; // registration successful - redirect to login form (not yet ready)
        }
        model.addAttribute("createPersonRequest", createPersonRequest); // Pass the same form data.
        model.addAttribute("errorMessage", createPersonResult.reason()); // Show an error message!
        return "register";
    }
}
