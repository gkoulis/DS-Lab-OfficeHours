package gr.hua.dit.officehours.core.repository;

import gr.hua.dit.officehours.core.model.Person;
import gr.hua.dit.officehours.core.model.PersonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByHuaIdStartsWithAndType(String prefix, PersonType personType);
}
