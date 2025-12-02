package gr.hua.dit.officehours.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.hua.dit.officehours.core.service.PersonDataService;
import gr.hua.dit.officehours.core.service.model.PersonView;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing {@code Person} resource.
 */
@RestController
@RequestMapping(value = "/api/v1/person", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonResource {

    private final PersonDataService personDataService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // ή Bean ακόμα καλύτερα.

    public PersonResource(final PersonDataService personDataService) {
        if (personDataService == null) throw new NullPointerException();
        this.personDataService = personDataService;
    }

    @GetMapping("")
    public List<PersonView> people() {
        final List<PersonView> personViewList = this.personDataService.getAllPeople();
        return personViewList;
    }

    // Σημείωση: Το Spring μετατρέπει τα java instances σε JSON. Παρακάτω, αν θέλαμε να το κάνουμε χειροκίνητα:
    // ΑΚΡΙΒΩΣ το ίδιο αποτέλεσμα. Κάπως έτσι λειτουργεί εσωτερικά το Spring.
    /*
    @GetMapping("/example")
    public String peopleManualJson() throws IOException {
        final List<PersonView> personViewList = this.personDataService.getAllPeople();
        final StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, personViewList);
        final String jsonString = stringWriter.toString();
        return jsonString;
    }
    */
}
