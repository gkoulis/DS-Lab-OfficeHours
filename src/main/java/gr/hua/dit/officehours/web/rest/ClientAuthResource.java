package gr.hua.dit.officehours.web.rest;

import gr.hua.dit.officehours.core.security.ClientDetailsService;
import gr.hua.dit.officehours.core.security.ClientDetails;
import gr.hua.dit.officehours.core.security.JwtService;

import gr.hua.dit.officehours.web.rest.model.ClientTokenRequest;
import gr.hua.dit.officehours.web.rest.model.ClientTokenResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller for authentication.
 */
@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientAuthResource {

    private final ClientDetailsService clientDetailsService;
    private final JwtService jwtService;

    public ClientAuthResource(final ClientDetailsService clientDetailsService, final JwtService jwtService) {
        if (clientDetailsService == null) throw new NullPointerException();
        if (jwtService == null) throw new NullPointerException();
        this.clientDetailsService = clientDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/client-tokens")
    public ClientTokenResponse clientToken(@RequestBody @Valid ClientTokenRequest clientTokenRequest) {
        final String clientId = clientTokenRequest.clientId();
        final String clientSecret = clientTokenRequest.clientSecret();

        // Step 1: Find and authenticate Client.
        final ClientDetails client = this.clientDetailsService.authenticate(clientId, clientSecret).orElse(null);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid client credentials");
        }

        // Step 2: Issue token.
        final String token = this.jwtService.issue("client:" + client.id(), client.roles());
        return new ClientTokenResponse(token, "Bearer", 60 * 60);
    }
}
