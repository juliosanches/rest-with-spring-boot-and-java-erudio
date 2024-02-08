package br.com.erudio.repositories;

import br.com.erudio.model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DisplayName("Given Person Object when Save then Return Saved Person")
    void testGivenPersonObject_whenSave_thenReturnSavedPerson() {
        // Given / Arrange
        Person person0 = new Person(
                "Leandro",
                "Costa",
                "leandro@erudio.com.br",
                "Minas Gerais",
                "Male");
        // When / Act
        Person savedPerson = personRepository.save(person0);

        // Then / Assert
        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }
    @Test
    @DisplayName("Given Person List when FindAll then Return Person List")
    void testGivenPersonList_whenFindAll_thenReturnPersonList() {
        // Given / Arrange
        Person person0 = new Person(
                "Leandro",
                "Costa",
                "leandro@erudio.com.br",
                "Minas Gerais",
                "Male");
        Person person1 = new Person(
                "Maria",
                "Costa",
                "maria@erudio.com.br",
                "Minas Gerais",
                "Female");

        personRepository.save(person0);
        personRepository.save(person1);

        // When / Act
        List<Person> personList = personRepository.findAll();

        // Then / Assert
        assertNotNull(personList);
        assertEquals(2, personList.size());
    }
    @Test
    @DisplayName("Given Person Object when FindById then Return Person Object")
    void testGivenPersonObject_whenFindById_thenReturnPersonObject() {
        // Given / Arrange
        Person person0 = new Person(
                "Leandro",
                "Costa",
                "leandro@erudio.com.br",
                "Minas Gerais",
                "Male");
        personRepository.save(person0);

        // When / Act
        Person savedPerson = personRepository.findById(person0.getId()).get();

        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(person0.getId(), savedPerson.getId());
    }
    @Test
    @DisplayName("Given Person Object when FindByEmail then Return Person Object")
    void testGivenPersonObject_whenFindByEmail_thenReturnPersonObject() {
        // Given / Arrange
        Person person0 = new Person(
                "Leandro",
                "Costa",
                "leandro@erudio.com.br",
                "Minas Gerais",
                "Male");
        personRepository.save(person0);

        // When / Act
        Person savedPerson = personRepository.findByEmail(person0.getEmail()).get();

        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(person0.getEmail(), savedPerson.getEmail());
    }

    @Test
    @DisplayName("Given Person Object when Update Person then Return Updated Person Object")
    void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {
        // Given / Arrange
        Person person0 = new Person(
                "Leandro",
                "Costa",
                "leandro@erudio.com.br",
                "Minas Gerais",
                "Male");
        personRepository.save(person0);

        // When / Act
        Person savedPerson = personRepository.findById(person0.getId()).get();
        savedPerson.setFirstName("José");
        savedPerson.setEmail("jose@email.com");

        Person updatedPerson = personRepository.save(savedPerson);

        // Then / Assert
        assertNotNull(updatedPerson);
        assertEquals("José", updatedPerson.getFirstName());
        assertEquals("jose@email.com", updatedPerson.getEmail());
    }

    @Test
    @DisplayName("Given Person Object when Delete then Remove Person")
    void testGivenPersonObject_whenDelete_thenRemovePerson() {
        // Given / Arrange
        Person person0 = new Person(
                "Leandro",
                "Costa",
                "leandro@erudio.com.br",
                "Minas Gerais",
                "Male");
        personRepository.save(person0);

        // When / Act
        personRepository.deleteById(person0.getId());
        Optional<Person> personOptional = personRepository.findById(person0.getId());

        // Then / Assert
        assertTrue(personOptional.isEmpty());
    }
}