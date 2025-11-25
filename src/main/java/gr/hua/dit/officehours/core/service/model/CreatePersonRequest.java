package gr.hua.dit.officehours.core.service.model;

import gr.hua.dit.officehours.core.model.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for requesting the creation (registration) of a Person.
 */
public record CreatePersonRequest(
    @NotNull PersonType type,
    @NotNull @NotBlank @Size(max = 20) String huaId,
    @NotNull @NotBlank @Size(max = 100) String firstName,
    @NotNull @NotBlank @Size(max = 100) String lastName,
    @NotNull @NotBlank @Size(max = 100) @Email String emailAddress,
    @NotNull @NotBlank @Size(max = 18) String mobilePhoneNumber,
    @NotNull @NotBlank @Size(min = 4, max = 24) String rawPassword
) {}
