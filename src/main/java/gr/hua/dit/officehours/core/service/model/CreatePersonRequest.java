package gr.hua.dit.officehours.core.service.model;

import gr.hua.dit.officehours.core.model.PersonType;

/**
 * DTO for requesting the creation (registration) of a Person.
 */
public record CreatePersonRequest(
    PersonType type,
    String huaId,
    String firstName,
    String lastName,
    String emailAddress,
    String mobilePhoneNumber,
    String rawPassword
) {}
