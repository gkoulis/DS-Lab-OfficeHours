package gr.hua.dit.officehours.core.port.impl;

import gr.hua.dit.officehours.config.RestApiClientConfig;
import gr.hua.dit.officehours.core.port.PhoneNumberPort;
import gr.hua.dit.officehours.core.port.impl.dto.PhoneNumberValidationResult;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Default implementation of {@link PhoneNumberPort}. It uses the NOC external service.
 */
@Service
public class PhoneNumberPortImpl implements PhoneNumberPort {

    private final RestTemplate restTemplate;

    public PhoneNumberPortImpl(final RestTemplate restTemplate) {
        if (restTemplate == null) throw new NullPointerException();
        this.restTemplate = restTemplate;
    }

    @Override
    public PhoneNumberValidationResult validate(final String rawPhoneNumber) {
        if (rawPhoneNumber == null) throw new NullPointerException();
        if (rawPhoneNumber.isBlank()) throw new IllegalArgumentException();

        // HTTP Request
        // --------------------------------------------------

        final String baseUrl = RestApiClientConfig.BASE_URL;
        final String url = baseUrl + "/api/v1/phone-numbers/" + rawPhoneNumber + "/validations";
        final ResponseEntity<PhoneNumberValidationResult> response
            = this.restTemplate.getForEntity(url, PhoneNumberValidationResult.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            final PhoneNumberValidationResult phoneNumberValidationResult = response.getBody();
            if (phoneNumberValidationResult == null) throw new NullPointerException();
            return phoneNumberValidationResult;
        }

        throw new RuntimeException("External service responded with " + response.getStatusCode());
    }
}
