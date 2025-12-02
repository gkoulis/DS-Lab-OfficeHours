package gr.hua.dit.officehours.web.rest.model;

import gr.hua.dit.officehours.web.rest.ClientAuthResource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @see ClientAuthResource
 */
public record ClientTokenRequest(
    @NotNull @NotBlank String clientId,
    @NotNull @NotBlank String clientSecret
) {}
