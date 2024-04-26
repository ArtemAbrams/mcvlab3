package org.example.mcvlab3.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.mcvlab3.model.Person;
import org.example.mcvlab3.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class PersonService {

    private PersonRepository personRepository;

    public void save(Person person) {
        if (!isValidPerson(person)) {
            throw new IllegalArgumentException("Invalid person data");
        }
        personRepository.saveAndFlush(person);
    }

    private boolean isValidPerson(Person person) {
        return person.getName() != null && !person.getName().isEmpty() &&
                person.getEmail() != null && person.getEmail().contains("@");
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}
