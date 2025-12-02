package gr.hua.dit.officehours.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

/**
 * JWT (JSON Web Token) service.
 *
 * <p>TODO fix deprecated methods
 */
@Service
public class JwtService {

    private final Key key;
    private final String issuer;
    private final String audience;
    private final long ttlMinutes; // time-to-live.

    public JwtService(@Value("${app.jwt.secret}") final String secret,
                      @Value("${app.jwt.issuer}") final String issuer,
                      @Value("${app.jwt.audience}") final String audience,
                      @Value("${app.jwt.ttl-minutes}") final long ttlMinutes) {
        if (secret == null) throw new NullPointerException();
        if (secret.isBlank()) throw new IllegalArgumentException();
        if (issuer == null) throw new NullPointerException();
        if (issuer.isBlank()) throw new IllegalArgumentException();
        if (audience == null) throw new NullPointerException();
        if (audience.isBlank()) throw new IllegalArgumentException();
        if (ttlMinutes <= 0) throw new IllegalArgumentException();
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.audience = audience;
        this.ttlMinutes = ttlMinutes;
    }

    public String issue(final String subject, final Collection<String> roles) {
        final Instant now = Instant.now();
        return Jwts.builder()
            .subject(subject)
            .issuer(this.issuer)
            .setAudience(this.audience)
            .claim("roles", roles)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(Duration.ofMinutes(this.ttlMinutes))))
            .signWith(this.key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims parse(final String token) {
        return Jwts.parser()
            .requireAudience(this.audience)
            .requireIssuer(this.issuer)
            .setSigningKey(this.key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
