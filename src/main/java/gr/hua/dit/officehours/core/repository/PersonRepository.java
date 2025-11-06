package gr.hua.dit.officehours.core.repository;

import gr.hua.dit.officehours.core.model.Person;
import gr.hua.dit.officehours.core.model.PersonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Person} entity.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByHuaId(final String huaId);

    Optional<Person> findByEmailAddressIgnoreCase(final String emailAddress);

    List<Person> findAllByTypeOrderByLastName(final PersonType type);

    boolean existsByEmailAddressIgnoreCase(final String emailAddress);

    boolean existsByMobilePhoneNumber(final String mobilePhoneNumber);

    boolean existsByHuaId(final String huaId);
}
