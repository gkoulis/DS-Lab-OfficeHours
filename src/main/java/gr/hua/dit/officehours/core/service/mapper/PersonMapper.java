package gr.hua.dit.officehours.core.service.mapper;

import gr.hua.dit.officehours.core.model.Person;
import gr.hua.dit.officehours.core.service.model.PersonView;

import org.springframework.stereotype.Component;

/**
 * Mapper to convert {@link Person} to {@link PersonView}.
 */
@Component
public class PersonMapper {

    public PersonView convertPersonToPersonView(final Person person) {
        if (person == null) {
            return null;
        }
        final PersonView personView = new PersonView(
            person.getId(),
            person.getHuaId(),
            person.getFirstName(),
            person.getLastName(),
            person.getMobilePhoneNumber(),
            person.getEmailAddress(),
            person.getType()
        );
        return personView;
    }
}
