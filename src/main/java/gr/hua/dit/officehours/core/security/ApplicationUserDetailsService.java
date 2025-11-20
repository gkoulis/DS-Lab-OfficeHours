package gr.hua.dit.officehours.core.security;

import gr.hua.dit.officehours.core.model.Person;
import gr.hua.dit.officehours.core.repository.PersonRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of Spring's {@code UserDetailsService} for providing application users.
 */
@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public ApplicationUserDetailsService(final PersonRepository personRepository) {
        if (personRepository == null) throw new NullPointerException();
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (username == null) throw new NullPointerException();
        if (username.isBlank()) throw new IllegalArgumentException();
        final Person person = this.personRepository
            .findByEmailAddressIgnoreCase(username)
            .orElse(null);
        if (person == null) {
            throw new UsernameNotFoundException("person with emailAddress" + username + " does not exist");
        }
        return new ApplicationUserDetails(
            person.getId(),
            person.getEmailAddress(),
            person.getPasswordHash(),
            person.getType()
        );

        // One-line alternative:
        /*
        return this.personRepository.findByEmailAddressIgnoreCase(username.strip())
            .map(person -> new ApplicationUserDetails(
                    person.getId(),
                    person.getEmailAddress(),
                    person.getPasswordHash(),
                    person.getType())
            )
            .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
        */
    }
}
