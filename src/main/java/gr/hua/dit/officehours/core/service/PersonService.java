package gr.hua.dit.officehours.core.service;

import gr.hua.dit.officehours.core.service.model.CreatePersonRequest;
import gr.hua.dit.officehours.core.service.model.PersonView;

/**
 * Service for managing {@link gr.hua.dit.officehours.core.model.Person}.
 */
public interface PersonService {

    PersonView createPerson(final CreatePersonRequest createPersonRequest);
}
