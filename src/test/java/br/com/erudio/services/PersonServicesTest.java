package br.com.erudio.services;

import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonServices services;

    private Person person0;

    @BeforeEach
    void setup() {
        // Given / Arrange
        person0 = new Person(
                "Leandro",
                "Costa",
                "leandro@erudio.com.br",
                "Uberlândia - Minas Gerais - Brasil",
                "Male");
    }

    @Test
    @DisplayName("Given Person Object When Save Person then Return Person Object")
    void testGivenPersonObject_WhenSavePerson_thenReturnPersonObject() {
        // Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(repository.save(person0)).willReturn(person0);

        // When / Act
        Person savedPerson = services.create(person0);

        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Leandro", savedPerson.getFirstName());

    }
    @Test
    @DisplayName("Given Existing Email When Save Person then Throws Exception")
    void testGivenExistingEmail_WhenSavePerson_thenThrowsException() {
        // Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.of(person0));

        // When / Act
        assertThrows(ResourceNotFoundException.class,
                () -> services.create(person0));

        // Then / Assert
        verify(repository, never()).save(any(Person.class));

    }
    @Test
    @DisplayName("Given Persons List When FindAll Persons then Return Persons List")
    void testGivenPersonsList_WhenFindAllPersons_thenReturnPersonsList() {
        // Given / Arrange
        Person person1 = new Person(
                "Maria",
                "Costa",
                "maria@erudio.com.br",
                "Montes Verdes - Minas Gerais - Brasil",
                "Female");

        given(repository.findAll()).willReturn(List.of(person0, person1));

        // When / Act
        List<Person> personsList = services.findAll();

        // Then / Assert
        assertNotNull(personsList);
        assertEquals(2, personsList.size());

    }
    @Test
    @DisplayName("Given Empty Persons List When FindAll Persons then Return Empty Persons List")
    void testGivenEmptyPersonsList_WhenFindAllPersons_thenReturnEmptyPersonsList() {
        // Given / Arrange
        given(repository.findAll()).willReturn(Collections.emptyList());

        // When / Act
        List<Person> personsList = services.findAll();

        // Then / Assert
        assertTrue(personsList.isEmpty());
        assertEquals(0, personsList.size());

    }

    @Test
    @DisplayName("Given Person Id When FindById then Return Person Object")
    void testGivenPersonId_WhenFindById_thenReturnPersonObject() {
        // Given / Arrange
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));

        // When / Act
        Person savedPerson = services.findById(1L);

        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Leandro", savedPerson.getFirstName());

    }
    @Test
    @DisplayName("Given Person Object When Update Person then Return Updated Person Object")
    void testGivenPersonObject_WhenUpdatePerson_thenReturnUpdatedPersonObject() {
        // Given / Arrange
        person0.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));

        person0.setFirstName("Leonard");
        person0.setEmail("leonard@bigbang.com");

        given(repository.save(any(Person.class))).willReturn(person0);

        // When / Act
        Person updatedPerson = services.update(person0);

        // Then / Assert
        assertNotNull(updatedPerson);
        assertEquals("Leonard", updatedPerson.getFirstName());
        assertEquals("leonard@bigbang.com", updatedPerson.getEmail());

    }
    @Test
    @DisplayName("Given PersonId When Delete Person then Do Nothing")
    void testGivenPersonId_WhenDeletePerson_thenDoNothing() {
        // Given / Arrange
        person0.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));
        willDoNothing().given(repository).delete(person0);

        // When / Act
        services.delete(person0.getId());

        // Then / Assert
        verify(repository, times(1)).delete(person0);
    }

}