package gr.hua.dit.officehours.web.rest.error;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;

/**
 * Provides global error handling for the REST API (/api/**). Returns JSON instead of HTML pages.
 */
@RestControllerAdvice(basePackages = "gr.hua.dit.officehours.web.rest")
@Order(1)
public class GlobalErrorHandlerRestControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorHandlerRestControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAnyError(final Exception exception,
                                                   final HttpServletRequest httpServletRequest) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (exception instanceof NoResourceFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (exception instanceof SecurityException) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else if (exception instanceof AuthorizationDeniedException) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else if (exception instanceof ResponseStatusException responseStatusException) {
            try {
                httpStatus = HttpStatus.valueOf(responseStatusException.getStatusCode().value());
            } catch (Exception ignored) {}
        }
        LOGGER.warn("REST error [{} {}] -> status={} cause={}: {}",
            httpServletRequest.getMethod(),
            httpServletRequest.getRequestURI(),
            httpStatus.value(),
            exception.getClass().getSimpleName(),
            exception.getMessage()
        );

        final ApiError apiError = new ApiError(
            Instant.now(),
            httpStatus.value(),
            httpStatus.getReasonPhrase(),
            exception.getMessage(),
            httpServletRequest.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(apiError);
    }
}
