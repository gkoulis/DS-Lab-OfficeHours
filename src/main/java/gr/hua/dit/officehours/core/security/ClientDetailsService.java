package gr.hua.dit.officehours.core.security;

import java.util.Optional;

/**
 * Service for managing REST API (integration) {@code Client}.
 */
public interface ClientDetailsService {

    Optional<ClientDetails> authenticate(final String id, final String secret);
}
