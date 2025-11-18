package gr.hua.dit.officehours.core.security;

import gr.hua.dit.officehours.core.model.PersonType;

/**
 * @see CurrentUserProvider
 */
public record CurrentUser(long id, String emailAddress, PersonType type) {}
