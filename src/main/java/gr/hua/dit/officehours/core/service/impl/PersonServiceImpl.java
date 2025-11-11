package gr.hua.dit.officehours.core.service.impl;

import gr.hua.dit.officehours.core.model.Person;
import gr.hua.dit.officehours.core.model.PersonType;
import gr.hua.dit.officehours.core.repository.PersonRepository;
import gr.hua.dit.officehours.core.service.PersonService;
import gr.hua.dit.officehours.core.service.mapper.PersonMapper;
import gr.hua.dit.officehours.core.service.model.CreatePersonRequest;
import gr.hua.dit.officehours.core.service.model.CreatePersonResult;
import gr.hua.dit.officehours.core.service.model.PersonView;

import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link PersonService}.
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonServiceImpl(final PersonRepository personRepository,
                             final PersonMapper personMapper) {
        if (personRepository == null) throw new NullPointerException();
        if (personMapper == null) throw new NullPointerException();

        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public CreatePersonResult createPerson(final CreatePersonRequest createPersonRequest) {
        if (createPersonRequest == null) throw new NullPointerException();

        // Unpack (we assume valid `CreatePersonRequest` instance)
        // --------------------------------------------------

        final PersonType type = createPersonRequest.type();
        final String huaId = createPersonRequest.huaId().strip(); // remove whitespaces
        final String firstName = createPersonRequest.firstName().strip();
        final String lastName = createPersonRequest.lastName().strip();
        final String emailAddress = createPersonRequest.emailAddress().strip();
        final String mobilePhoneNumber = createPersonRequest.mobilePhoneNumber().strip();
        final String rawPassword = createPersonRequest.rawPassword();

        // Basic email address validation.
        // --------------------------------------------------

        if (!emailAddress.endsWith("@hua.gr")) {
            return CreatePersonResult.fail("Only academic email addresses (@hua.gr) are allowed");
        }

        // --------------------------------------------------

        // TODO huaId must be unique
        // TODO emailAddress must be unique
        // TODO mobilePhoneNumber must be unique

        // --------------------------------------------------

        // TODO use external service to validate huaId

        // --------------------------------------------------

        final String hashedPassword = rawPassword; // TODO Implement: encode password.

        // Instantiate person.
        // --------------------------------------------------

        Person person = new Person();
        person.setId(null); // auto generated
        person.setHuaId(huaId);
        person.setType(type);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmailAddress(emailAddress);
        person.setMobilePhoneNumber(mobilePhoneNumber);
        person.setPasswordHash(hashedPassword);
        person.setCreatedAt(null); // auto generated.

        // Persist person (save/insert to database)
        // --------------------------------------------------

        person = this.personRepository.save(person);

        // --------------------------------------------------

        // TODO user external service to notify person.

        // Map `Person` to `PersonView`.
        // --------------------------------------------------

        final PersonView personView = this.personMapper.convertPersonToPersonView(person);

        // --------------------------------------------------

        return CreatePersonResult.success(personView);
    }
}
