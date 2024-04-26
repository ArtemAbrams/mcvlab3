package org.example.mcvlab3;

import org.example.mcvlab3.model.Person;
import org.example.mcvlab3.repository.PersonRepository;
import org.example.mcvlab3.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PersonServiceTests {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void testSavePerson() {
        Person person = new Person(1L, "John Doe", "john@example.com", new HashSet<>());
        when(personRepository.saveAndFlush(person)).thenReturn(person);
        personService.save(person);
        verify(personRepository, times(1)).saveAndFlush(person);
    }

    @Test
    void testFindAllPersons() {
        when(personRepository.findAll()).thenReturn(
                Arrays.asList(new Person(1L, "John Doe", "john@example.com", new HashSet<>()),
                        new Person(2L, "Jane Doe", "jane@example.com", new HashSet<>())));
        List<Person> results = personService.findAll();
        assertEquals(2, results.size());
        verify(personRepository, times(1)).findAll();
    }

    @Test
    void testGetPersonById_Found() {
        Long personId = 1L;
        Person expectedPerson = new Person(personId, "John Doe", "john@example.com", new HashSet<>());
        when(personRepository.findById(personId)).thenReturn(Optional.of(expectedPerson));
        Person result = personService.getPersonById(personId);
        assertEquals(expectedPerson, result);
        verify(personRepository, times(1)).findById(personId);
    }

    @Test
    void testGetPersonById_NotFound_ThrowsException() {
        Long personId = 99L;
        when(personRepository.findById(personId))
                .thenThrow(new IllegalArgumentException("Person not found"));
        assertThrows(IllegalArgumentException.class, () -> personService.getPersonById(personId));
        verify(personRepository).findById(personId);
    }
    @Test
    void testDeletePerson() {
        Long personId = 1L;
        doNothing().when(personRepository).deleteById(personId);
        personService.delete(personId);
        verify(personRepository, times(1)).deleteById(personId);
    }
    @Test
    void testSavePerson_ValidData() {
        Person validPerson = new Person(1L, "John Doe", "john@example.com", new HashSet<>());
        when(personRepository.saveAndFlush(validPerson)).thenReturn(validPerson);
        personService.save(validPerson);
        verify(personRepository, times(1)).saveAndFlush(validPerson);
    }

    @Test
    void testSavePerson_InvalidData_ThrowsException() {
        Person invalidPerson = new Person(1L, "John", "noemail", new HashSet<>());
        assertThrows(IllegalArgumentException.class, () -> personService.save(invalidPerson));
        verify(personRepository, never()).saveAndFlush(invalidPerson);
    }

    @Test
    void testGetPersonById_NotFound_ThrowsException_2() {
        Long personId = 99L;
        when(personRepository.findById(personId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> personService.getPersonById(personId));
        verify(personRepository, times(1)).findById(personId);
    }

    @Test
    void testComplexScenario_MultipleSaves() {
        Person person1 = new Person(1L, "Jane Doe", "jane@example.com", new HashSet<>());
        Person person2 = new Person(2L, "Doe Jane", "doej@example.com", new HashSet<>());
        when(personRepository.saveAndFlush(person1)).thenReturn(person1);
        when(personRepository.saveAndFlush(person2)).thenReturn(person2);

        personService.save(person1);
        personService.save(person2);

        InOrder inOrder = inOrder(personRepository);
        inOrder.verify(personRepository).saveAndFlush(person1);
        inOrder.verify(personRepository).saveAndFlush(person2);
    }
}
