package gr.hua.dit.officehours.core.service;

import gr.hua.dit.officehours.core.service.model.CreatePersonRequest;
import gr.hua.dit.officehours.core.service.model.CreatePersonResult;

/**
 * Service for managing {@link gr.hua.dit.officehours.core.model.Person}.
 */
public interface PersonService {

    CreatePersonResult createPerson(final CreatePersonRequest createPersonRequest);
}
