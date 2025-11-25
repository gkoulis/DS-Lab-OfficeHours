package gr.hua.dit.officehours.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestApiClientConfig {

    // @future Get me from application properties!
    public static final String BASE_URL = "http://localhost:8081";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
