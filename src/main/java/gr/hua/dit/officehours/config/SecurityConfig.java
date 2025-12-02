package gr.hua.dit.officehours.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration.
 */
@Configuration
@EnableMethodSecurity // enables @PreAuthorize
public class SecurityConfig {

    // @future API Security (stateless - JWT based)

    /**
     * UI chain {@code "/**"} (stateful - cookie based).
     */
    @Bean
    @Order(2)
    public SecurityFilterChain uiChain(final HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**")
            // Το αφήνουμε ως σχόλιο προσωρινά... TODO configure.
            // .csrf(csrf -> csrf.ignoringRequestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .requestMatchers("/", "/login", "/register").permitAll() // Public
                .requestMatchers("/profile", "/logout").authenticated() // Private
                .anyRequest().permitAll() // the rest
            )
            .formLogin(form -> form
                .loginPage("/login") // custom login page (see login.html)
                .loginProcessingUrl("/login") // POST request target (handled by Spring Security)
                .defaultSuccessUrl("/profile", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // POST request target (handled by Spring Security)
                .logoutSuccessUrl("/login?logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
            )
            // Disable basic security.
            .httpBasic(basic -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
