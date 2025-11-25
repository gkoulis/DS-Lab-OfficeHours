package gr.hua.dit.officehours.core.port.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gr.hua.dit.officehours.config.RestApiClientConfig;
import gr.hua.dit.officehours.core.port.SmsNotificationPort;
import gr.hua.dit.officehours.core.port.impl.dto.SendSmsRequest;
import gr.hua.dit.officehours.core.port.impl.dto.SendSmsResult;

/**
 * Default implementation of {@link SmsNotificationPort}. It uses the NOC external service.
 */
@Service
public class SmsNotificationPortImpl implements SmsNotificationPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationPortImpl.class);

    private static final boolean ACTIVE = false; // @future Get from application properties.

    private final RestTemplate restTemplate;

    public SmsNotificationPortImpl(final RestTemplate restTemplate) {
        if (restTemplate == null) throw new NullPointerException();
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean sendSms(final String e164, final String content) {
        if (e164 == null) throw new NullPointerException();
        if (e164.isBlank()) throw new IllegalArgumentException();
        if (content == null) throw new NullPointerException();
        if (content.isBlank()) throw new IllegalArgumentException();

        // --------------------------------------------------

        if (!ACTIVE) {
            LOGGER.warn("SMS Notification is not active");
            return true;
        }

        // --------------------------------------------------

        if (e164.startsWith("+30692") || e164.startsWith("+30690000")) {
            LOGGER.warn("Not allocated E164 {}. Aborting...", e164);
            return true;
        }

        // Headers
        // --------------------------------------------------

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // Payload
        // --------------------------------------------------

        final SendSmsRequest body = new SendSmsRequest(e164, content);

        // Alternative: (Spring speaks JSON!!! Search for ObjectMapper!)
        // final Map<String, Object> body = Map.of("e164", e164, "body", content);

        // HTTP Request
        // --------------------------------------------------

        final String baseUrl = RestApiClientConfig.BASE_URL;
        final String url = baseUrl + "/api/v1/sms";
        final HttpEntity<SendSmsRequest> entity = new HttpEntity<>(body, httpHeaders);
        final ResponseEntity<SendSmsResult> response = this.restTemplate.postForEntity(url, entity, SendSmsResult.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            final SendSmsResult sendSmsResult = response.getBody();
            if (sendSmsResult == null) throw new NullPointerException();
            return sendSmsResult.sent();
        }

        throw new RuntimeException("External service responded with " + response.getStatusCode());
    }
}
