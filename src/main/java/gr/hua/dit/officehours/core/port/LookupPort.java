package gr.hua.dit.officehours.core.port;

import java.util.Optional;

import gr.hua.dit.officehours.core.model.PersonType;

/**
 * Port to external service for managing lookups.
 */
public interface LookupPort {

    Optional<PersonType> lookup(final String huaId);
}
