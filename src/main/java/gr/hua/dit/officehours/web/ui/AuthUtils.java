package gr.hua.dit.officehours.web.ui;

/**
 * Authentication utilities for controllers.
 */
final class AuthUtils {

    private AuthUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isAuthenticated(final Object auth) {
        return true; // TODO Implement.
    }

    public static boolean isAnonymous(final Object auth) {
        return false; // TODO Implement.
    }
}
