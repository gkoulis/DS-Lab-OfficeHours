package gr.hua.dit.officehours.core.service;

import gr.hua.dit.officehours.core.model.PersonType;
import gr.hua.dit.officehours.core.service.model.CreatePersonRequest;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Initializes application.
 */
@Service
public class InitializationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializationService.class);

    private final PersonService personService;
    private final AtomicBoolean initialized;

    public InitializationService(final PersonService personService) {
        if (personService == null) throw new NullPointerException();
        this.personService = personService;
        this.initialized = new AtomicBoolean(false);
    }

    @PostConstruct
    public void populateDatabaseWithInitialData() {
        final boolean alreadyInitialized = this.initialized.getAndSet(true);
        if (alreadyInitialized) {
            LOGGER.warn("Database initialization skipped: initial data has already been populated.");
            return;
        }
        LOGGER.info("Starting database initialization with initial data...");
        final List<CreatePersonRequest> createPersonRequestList = List.of(
            // User with ID 1
            new CreatePersonRequest(
                PersonType.TEACHER,
                "t0001",
                "Dimitris",
                "Gkoulis",
                "gkoulis@hua.gr",
                "+306900000000",
                "1234"
            ),
            // User with ID 2
            new CreatePersonRequest(
                PersonType.STUDENT,
                "it2023001",
                "Test 1",
                "Test 1",
                "it2023001@hua.gr",
                "+306900000001",
                "1234"
            ),
            // User with ID 3
            new CreatePersonRequest(
                PersonType.STUDENT,
                "it2023002",
                "Test 2",
                "Test 2",
                "it2023002@hua.gr",
                "+306900000002",
                "1234"
            )
        );
        for (final var createPersonRequest : createPersonRequestList) {
            this.personService.createPerson(createPersonRequest, false); // do not send SMS
        }
        LOGGER.info("Database initialization completed successfully.");
    }
}
