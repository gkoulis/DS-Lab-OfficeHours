package gr.hua.dit.officehours.web.rest;

import gr.hua.dit.officehours.core.service.PersonDataService;
import gr.hua.dit.officehours.core.service.model.PersonView;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing {@code Person} resource.
 */
@RestController
@RequestMapping("/api/v1/person")
public class PersonResource {

    private final PersonDataService personDataService;

    public PersonResource(final PersonDataService personDataService) {
        if (personDataService == null) throw new NullPointerException();
        this.personDataService = personDataService;
    }

    @GetMapping("")
    public List<PersonView> people() {
        final List<PersonView> personViewList = this.personDataService.getAllPeople();
        return personViewList;
    }
}
