package gr.hua.dit.officehours.web;

import gr.hua.dit.officehours.core.model.Person;
import gr.hua.dit.officehours.core.model.PersonType;
import gr.hua.dit.officehours.core.repository.PersonRepository;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for testing.
 */
@RestController
public class TestController {

    private final PersonRepository personRepository;

    public TestController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public String test() {
        for (int i=0; i<100; i++) {
            Person person1 = new Person();
            person1.setId(null);
            person1.setHuaId("it2023-" + i);
            person1.setFirstName("FN " + i);
            person1.setLastName("LN" + i);
            person1.setEmailAddress("gkoulis@gmail.com");
            person1.setMobilePhoneNumber("+306900000000");
            person1.setType(PersonType.STUDENT);
            person1.setPasswordHash("<hash>");

            person1 = this.personRepository.save(person1);
        }

        final var people = this.personRepository
            .findByHuaIdStartsWithAndType("it2023", PersonType.STUDENT);

        final String stringToServe = String.join(
            "\n", people.stream().map(Person::toString).toList());

        return stringToServe;
    }
}
