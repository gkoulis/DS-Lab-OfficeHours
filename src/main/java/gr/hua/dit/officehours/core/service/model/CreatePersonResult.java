package gr.hua.dit.officehours.core.service.model;

import gr.hua.dit.officehours.core.service.impl.PersonBusinessLogicServiceImpl;

/**
 * CreatePersonResult DTO.
 *
 * @see PersonBusinessLogicServiceImpl#createPerson(CreatePersonRequest)
 */
public record CreatePersonResult(
    boolean created,
    String reason,
    PersonView personView
) {

    public static CreatePersonResult success(final PersonView personView) {
        if (personView == null) throw new NullPointerException();
        return new CreatePersonResult(true, null, personView);
    }

    public static CreatePersonResult fail(final String reason) {
        if (reason == null) throw new NullPointerException();
        if (reason.isBlank()) throw new IllegalArgumentException();
        return new CreatePersonResult(false, reason, null);
    }
}
