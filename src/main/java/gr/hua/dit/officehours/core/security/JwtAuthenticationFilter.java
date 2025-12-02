package gr.hua.dit.officehours.core.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * JWT authentication filter.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;

    public JwtAuthenticationFilter(final JwtService jwtService) {
        if (jwtService == null) throw new NullPointerException();
        this.jwtService = jwtService;
    }

    private void writeError(final HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"invalid_token\"}");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        final String path = request.getServletPath();
        if (path.equals("/api/v1/auth/client-tokens")) return true;
        return !path.startsWith("/api/v1");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        // No header or not Bearer? -> Let the request continue unauthenticated.
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorizationHeader.substring(7);
        try {
            final Claims claims = this.jwtService.parse(token);
            final String subject = claims.getSubject();
            final Collection<String> roles = (Collection<String>) claims.get("roles");

            // Convert String to GrantedAuthority
            final var authorities =
                roles == null
                    ? List.<GrantedAuthority>of() // empty list
                    : roles.stream().map(role ->
                        new SimpleGrantedAuthority("ROLE_" + role)).toList();

            // Create User.
            final User principal = new User(subject, "", authorities);
            final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception ex) {
            // Invalid Token or Internal error.
            LOGGER.warn("JwtAuthenticationFilter failed", ex);
            this.writeError(response);
            return; // stop here, i.e, next filters are ignored.
        }

        filterChain.doFilter(request, response); // next filter.
    }
}
