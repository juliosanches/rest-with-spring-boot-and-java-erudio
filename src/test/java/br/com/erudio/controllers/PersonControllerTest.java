package br.com.erudio.controllers;

import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.model.Person;
import br.com.erudio.services.PersonServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PersonServices service;

    private Person person;

    @BeforeEach
    void setup() {
        // Given / Arrange
        person = new Person(
                "Leandro",
                "Costa",
                "leandro@erudio.com.br",
                "Uberlândia - Minas Gerais - Brasil",
                "Male");
    }

    @Test
    @DisplayName("Given Person Object When Create Person Then Return Saved Person")
    void testGivenPersonObject_WhenCreatePerson_ThenReturnSavedPerson() throws Exception {
        // Given / Arrange
        given(service.create(any(Person.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // When / Act
        ResultActions response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person)));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())))
        ;
    }
    @Test
    @DisplayName("Given List Of Persons When FindAl Persons Then Return Persons List")
    void testGivenListOfPersons_WhenFindAllPersons_ThenReturnPersonsList() throws Exception {
        // Given / Arrange
        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(new Person(
                "Maria",
                "Costa",
                "maria@erudio.com.br",
                "Minas Gerais",
                "Female"));

        given(service.findAll()).willReturn(persons);

        // When / Act
        ResultActions response = mockMvc.perform(get("/person"));

        // Then / Assert
        response
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(persons.size())))
        ;
    }

    @Test
    @DisplayName("Given Person Object When Create Person Then Return Saved Person")
    void testGivenPersonId_WhenFindById_ThenReturnPersonObject() throws Exception {
        // Given / Arrange
        long personId = 1L;
        given(service.findById(personId)).willReturn(person);

        // When / Act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())))
        ;
    }
    @Test
    @DisplayName("Given Invalid PersonId When FindById Then Return Not Found")
    void testGivenInvalidPersonId_WhenFindById_ThenReturnNotFound() throws Exception {
        // Given / Arrange
        long personId = 1L;
        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);

        // When / Act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("Given Updated Person When Update Then Return Updated Person Object")
    void testGivenUpdatedPerson_WhenUpdate_ThenReturnUpdatedPersonObject() throws Exception {
        // Given / Arrange
        long personId = 1L;
//        given(service.findById(personId)).willReturn(person);
        given(service.update(any(Person.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // When / Act
        Person updatedPerson = new Person(
                "Maria",
                "Costa",
                "maria@erudio.com.br",
                "Minas Gerais",
                "Female");
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedPerson.getEmail())))
        ;
    }
    @Test
    @DisplayName("Given Nonexistent Person When Update Then Return NotFound")
    void testGivenNonexistentPerson_WhenUpdate_ThenReturnNotFound() throws Exception { //FIXME Esse teste está errado!!
        // Given / Arrange
        long personId = 1L;
//        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
        given(service.update(any(Person.class)))
                .willAnswer(invocation -> invocation.getArgument(1));

        // When / Act
        Person updatedPerson = new Person(
                "Maria",
                "Costa",
                "maria@erudio.com.br",
                "Minas Gerais",
                "Female");
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("Given PersonId When Delete Then Return NoContent")
    void testGivenPersonId_WhenDelete_ThenReturnNoContent() throws Exception {
        // Given / Arrange
        long personId = 1L;
//        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
        given(service.update(any(Person.class)))
                .willAnswer(invocation -> invocation.getArgument(1));

        // When / Act
        Person updatedPerson = new Person(
                "Maria",
                "Costa",
                "maria@erudio.com.br",
                "Minas Gerais",
                "Female");
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isNotFound())
        ;
    }
}