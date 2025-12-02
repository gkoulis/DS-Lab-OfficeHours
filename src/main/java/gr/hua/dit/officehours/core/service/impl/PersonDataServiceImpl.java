package gr.hua.dit.officehours.core.service.impl;

import gr.hua.dit.officehours.core.model.Person;
import gr.hua.dit.officehours.core.repository.PersonRepository;
import gr.hua.dit.officehours.core.service.PersonDataService;
import gr.hua.dit.officehours.core.service.mapper.PersonMapper;
import gr.hua.dit.officehours.core.service.model.PersonView;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default implementation of {@link PersonDataService}.
 */
@Service
public class PersonDataServiceImpl implements PersonDataService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonDataServiceImpl(final PersonRepository personRepository,
                                 final PersonMapper personMapper) {
        if (personRepository == null) throw new NullPointerException();
        if (personMapper == null) throw new NullPointerException();
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public List<PersonView> getAllPeople() {
        final List<Person> personList = this.personRepository.findAll();
        final List<PersonView> personViewList = personList
            .stream()
            .map(this.personMapper::convertPersonToPersonView)
            .toList();
        return personViewList;
    }
}
