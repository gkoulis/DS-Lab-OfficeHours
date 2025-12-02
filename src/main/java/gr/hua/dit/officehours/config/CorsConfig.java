package gr.hua.dit.officehours.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration.
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @SuppressWarnings("NullableProblems")
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/api/v1/**")
                    .allowedOrigins("http://localhost")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(false)
                    .maxAge(3600);
            }
        };
    }
}
