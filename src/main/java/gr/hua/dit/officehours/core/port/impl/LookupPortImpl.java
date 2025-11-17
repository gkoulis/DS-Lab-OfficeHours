package gr.hua.dit.officehours.core.port.impl;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gr.hua.dit.officehours.config.RestApiClientConfig;
import gr.hua.dit.officehours.core.model.PersonType;
import gr.hua.dit.officehours.core.port.LookupPort;
import gr.hua.dit.officehours.core.port.impl.dto.LookupResult;

/**
 * Default implementation of {@link LookupPort}. It uses the NOC external service.
 */
@Service
public class LookupPortImpl implements LookupPort {

    private final RestTemplate restTemplate;

    public LookupPortImpl(final RestTemplate restTemplate) {
        if (restTemplate == null) throw new NullPointerException();
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<PersonType> lookup(final String huaId) {
        if (huaId == null) throw new NullPointerException();
        if (huaId.isBlank()) throw new IllegalArgumentException();

        // HTTP Request
        // --------------------------------------------------

        final String baseUrl = RestApiClientConfig.BASE_URL;
        final String url = baseUrl + "/api/v1/lookups/" + huaId;
        final ResponseEntity<LookupResult> response = this.restTemplate.getForEntity(url, LookupResult.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            final LookupResult lookupResult = response.getBody();
            if (lookupResult == null) throw new NullPointerException();
            return Optional.ofNullable(lookupResult.type());
        }

        throw new RuntimeException("External service responded with " + response.getStatusCode());
    }
}
