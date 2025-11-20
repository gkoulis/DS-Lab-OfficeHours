package gr.hua.dit.officehours.core.service.impl;

import gr.hua.dit.officehours.core.model.Person;
import gr.hua.dit.officehours.core.model.PersonType;
import gr.hua.dit.officehours.core.port.LookupPort;
import gr.hua.dit.officehours.core.port.PhoneNumberPort;
import gr.hua.dit.officehours.core.port.SmsNotificationPort;
import gr.hua.dit.officehours.core.port.impl.dto.PhoneNumberValidationResult;
import gr.hua.dit.officehours.core.repository.PersonRepository;
import gr.hua.dit.officehours.core.service.PersonService;
import gr.hua.dit.officehours.core.service.mapper.PersonMapper;
import gr.hua.dit.officehours.core.service.model.CreatePersonRequest;
import gr.hua.dit.officehours.core.service.model.CreatePersonResult;
import gr.hua.dit.officehours.core.service.model.PersonView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link PersonService}.
 */
@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final PhoneNumberPort phoneNumberPort;
    private final LookupPort lookupPort;
    private final SmsNotificationPort smsNotificationPort;

    public PersonServiceImpl(final PasswordEncoder passwordEncoder,
                             final PersonRepository personRepository,
                             final PersonMapper personMapper,
                             final PhoneNumberPort phoneNumberPort,
                             final LookupPort lookupPort,
                             final SmsNotificationPort smsNotificationPort) {
        if (passwordEncoder == null) throw new NullPointerException();
        if (personRepository == null) throw new NullPointerException();
        if (personMapper == null) throw new NullPointerException();
        if (phoneNumberPort == null) throw new NullPointerException();
        if (lookupPort == null) throw new NullPointerException();
        if (smsNotificationPort == null) throw new NullPointerException();

        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.phoneNumberPort = phoneNumberPort;
        this.lookupPort = lookupPort;
        this.smsNotificationPort = smsNotificationPort;
    }

    @Override
    public CreatePersonResult createPerson(final CreatePersonRequest createPersonRequest, final boolean notify) {
        if (createPersonRequest == null) throw new NullPointerException();

        // Unpack (we assume valid `CreatePersonRequest` instance)
        // --------------------------------------------------

        final PersonType type = createPersonRequest.type();
        final String huaId = createPersonRequest.huaId().strip(); // remove whitespaces
        final String firstName = createPersonRequest.firstName().strip();
        final String lastName = createPersonRequest.lastName().strip();
        final String emailAddress = createPersonRequest.emailAddress().strip();
        String mobilePhoneNumber = createPersonRequest.mobilePhoneNumber().strip();
        final String rawPassword = createPersonRequest.rawPassword();

        // Basic email address validation.
        // --------------------------------------------------

        if (!emailAddress.endsWith("@hua.gr")) {
            return CreatePersonResult.fail("Only academic email addresses (@hua.gr) are allowed");
        }

        // Advanced mobile phone number validation.
        // --------------------------------------------------

        final PhoneNumberValidationResult phoneNumberValidationResult
            = this.phoneNumberPort.validate(mobilePhoneNumber);
        if (!phoneNumberValidationResult.isValidMobile()) {
            return CreatePersonResult.fail("Mobile Phone Number is not valid");
        }
        mobilePhoneNumber = phoneNumberValidationResult.e164();

        // --------------------------------------------------

        if (this.personRepository.existsByHuaIdIgnoreCase(huaId)) {
            return CreatePersonResult.fail("HUA ID already registered");
        }

        if (this.personRepository.existsByEmailAddressIgnoreCase(emailAddress)) {
            return CreatePersonResult.fail("Email Address already registered");
        }

        if (this.personRepository.existsByMobilePhoneNumber(mobilePhoneNumber)) {
            return CreatePersonResult.fail("Mobile Phone Number already registered");
        }

        // --------------------------------------------------

        final PersonType personType_lookup = this.lookupPort.lookup(huaId).orElse(null);
        if (personType_lookup == null) {
            return CreatePersonResult.fail("Invalid HUA ID");
        }
        if (personType_lookup != type) {
            return CreatePersonResult.fail("The provided person type does not match the actual one");
        }

        // --------------------------------------------------

        final String hashedPassword = this.passwordEncoder.encode(rawPassword);

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

        if (notify) {
            final String content = String.format(
                "You have successfully registered for the Office Hours application. " +
                    "Use your email (%s) to log in.", emailAddress);
            final boolean sent = this.smsNotificationPort.sendSms(mobilePhoneNumber, content);
            if (!sent) {
                LOGGER.warn("SMS send to {} failed!", mobilePhoneNumber);
            }
        }

        // Map `Person` to `PersonView`.
        // --------------------------------------------------

        final PersonView personView = this.personMapper.convertPersonToPersonView(person);

        // --------------------------------------------------

        return CreatePersonResult.success(personView);
    }
}
