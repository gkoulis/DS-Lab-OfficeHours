package gr.hua.dit.officehours.web.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Provides global error handling and custom error templates.
 */
@ControllerAdvice
public class GlobalErrorHandlerControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorHandlerControllerAdvice.class);

    @ExceptionHandler(Exception.class) // all exceptions inherit Exception.class
    public String handleAnyError(final Exception exception,
                                 final HttpServletRequest httpServletRequest,
                                 final HttpServletResponse httpServletResponse,
                                 final Model model) {
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("path", httpServletRequest.getRequestURI());
        if (exception instanceof NoResourceFoundException) {
            httpServletResponse.setStatus(404);
            return "error/404";
        } else if (exception instanceof ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatusCode().value() == 404) {
                httpServletResponse.setStatus(404);
                return "error/404";
            }
        }
        LOGGER.warn("Handling exception {} {}", exception.getClass(), exception.getMessage());
        return "error/error";
    }
}
