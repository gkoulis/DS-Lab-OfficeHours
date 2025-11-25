package gr.hua.dit.officehours.web.ui;

import gr.hua.dit.officehours.core.security.CurrentUser;
import gr.hua.dit.officehours.core.security.CurrentUserProvider;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Provides specific controllers {@link org.springframework.ui.Model} with the current user.
 */
@ControllerAdvice(basePackageClasses = { ProfileController.class, TicketController.class })
public class CurrentUserControllerAdvice {

    private final CurrentUserProvider currentUserProvider;

    public CurrentUserControllerAdvice(final CurrentUserProvider currentUserProvider) {
        if (currentUserProvider == null) throw new NullPointerException();
        this.currentUserProvider = currentUserProvider;
    }

    @ModelAttribute("me")
    public CurrentUser addCurrentUserToModel() {
        return this.currentUserProvider.getCurrentUser().orElse(null);
    }
}
