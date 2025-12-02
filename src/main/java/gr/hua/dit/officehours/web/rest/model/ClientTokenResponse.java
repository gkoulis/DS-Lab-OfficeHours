package gr.hua.dit.officehours.web.rest.model;

import gr.hua.dit.officehours.web.rest.ClientAuthResource;

/**
 * @see ClientAuthResource
 */
public record ClientTokenResponse(
    String accessToken,
    String tokenType,
    long expiresIn
) {}
