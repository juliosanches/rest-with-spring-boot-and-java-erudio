package br.com.erudio.repositories;

import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    private Person person0;

    @BeforeEach
    void setup() {
        // Given / Arrange
        person0 = new Person(
                "Leandro",
                "Costa",
                "leandro@erudio.com.br",
                "Minas Gerais",
                "Male");
    }

    @Test
    @DisplayName("Given Person Object when Save then Return Saved Person")
    void testGivenPersonObject_whenSave_thenReturnSavedPerson() {
        // Given / Arrange
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
        personRepository.save(person0);

        // When / Act
        personRepository.deleteById(person0.getId());
        Optional<Person> personOptional = personRepository.findById(person0.getId());

        // Then / Assert
        assertTrue(personOptional.isEmpty());
    }

    @Test
    @DisplayName("Given FirstName And LastName when FindJPQL then Return Person Object")
    void testGivenFirstNameAndLastName_whenFindJPQL_thenReturnPersonObject() {
        // Given / Arrange
        String firstName = "Leandro";
        String lastName = "Costa";
        personRepository.save(person0);

        // When / Act
        Person savedPerson = personRepository.findByJPQL(firstName, lastName);

        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }

    @Test
    @DisplayName("Given FirstName And LastName when FindJPQLNamedParameters then Return Person Object")
    void testGivenFirstNameAndLastName_whenFindJPQLNamedParameters_thenReturnPersonObject() {
        // Given / Arrange
        String firstName = "Leandro";
        String lastName = "Costa";
        personRepository.save(person0);

        // When / Act
        Person savedPerson = personRepository.findByJPQLNamedParameters(firstName, lastName);

        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }

    @Test
    @DisplayName("Given FirstName And LastName when FindNativeSQL then Return Person Object")
    void testGivenFirstNameAndLastName_whenFindNativeSQL_thenReturnPersonObject() {
        // Given / Arrange
        String firstName = "Leandro";
        String lastName = "Costa";
        personRepository.save(person0);

        // When / Act
        Person savedPerson = personRepository.findByNativeSQL(firstName, lastName);

        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }

    @Test
    @DisplayName("Given FirstName And LastName when FindNativeSQLWithNamedParameters then Return Person Object")
    void testGivenFirstNameAndLastName_whenFindNativeSQLWithNamedParameters_thenReturnPersonObject() {
        // Given / Arrange
        String firstName = "Leandro";
        String lastName = "Costa";
        personRepository.save(person0);

        // When / Act
        Person savedPerson = personRepository.findByNativeSQLWithNamedParameters(firstName, lastName);

        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }
}