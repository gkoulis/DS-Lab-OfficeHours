package gr.hua.dit.officehours.core.service.model;

import gr.hua.dit.officehours.core.model.PersonType;
import gr.hua.dit.officehours.core.service.PersonBusinessLogicService;

/**
 * General view of {@link gr.hua.dit.officehours.core.model.Person} entity.
 *
 * @see gr.hua.dit.officehours.core.model.Person
 * @see PersonBusinessLogicService
 */
public record PersonView(
    long id,
    String huaId,
    String firstName,
    String lastName,
    String mobilePhoneNumber,
    String emailAddress,
    PersonType type
) {

    public String fullName() {
        return this.firstName + " " + this.lastName;
    }
}
