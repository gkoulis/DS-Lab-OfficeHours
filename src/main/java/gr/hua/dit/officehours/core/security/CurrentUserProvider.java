package gr.hua.dit.officehours.core.security;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Component for providing the current user.
 *
 * @see CurrentUser
 */
@Component
public final class CurrentUserProvider {

    public Optional<CurrentUser> getCurrentUser() {
        return Optional.empty(); // TODO Implement.
    }
}
