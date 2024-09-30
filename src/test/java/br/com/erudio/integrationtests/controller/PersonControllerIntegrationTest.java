package br.com.erudio.integrationtests.controller;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerIntegrationTest  extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static Person person;

    @BeforeAll
    public static void setup() {
        // Given / Arrange
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        person = new Person(
                "Leandro",
                "Costa",
                "leandro@erudio.com.br",
                "Minas Gerais",
                "Male");
    }

    @DisplayName("JUnit integration given Person Object when Create one Person should Return a Person Object")
    @Test
    @Order(1)
    void integrationTestGivenPersonObject_when_CreateOnePerson_ShouldReturnAPersonObject() throws JsonProcessingException {
        String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person createdPerson = objectMapper.readValue(content, Person.class);
        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getEmail());

        assertTrue(createdPerson.getId() > 0);
        assertEquals("Leandro", createdPerson.getFirstName());
        assertEquals("Costa", createdPerson.getLastName());
        assertEquals("Minas Gerais", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertEquals("leandro@erudio.com.br", createdPerson.getEmail());
    }

    @DisplayName("JUnit integration given Person Object when Updated Person should Return a Updated Person Object")
    @Test
    @Order(2)
    void integrationTestGivenPersonObject_when_UpdateOnePerson_ShouldReturnAUpdatedPersonObject() throws JsonProcessingException {

        person.setFirstName("Leonardo");
        person.setEmail("leonardo@erudio.com.br");
        String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person updatedPerson = objectMapper.readValue(content, Person.class);
        person = updatedPerson;

        assertNotNull(updatedPerson);
        assertNotNull(updatedPerson.getId());
        assertNotNull(updatedPerson.getFirstName());
        assertNotNull(updatedPerson.getLastName());
        assertNotNull(updatedPerson.getAddress());
        assertNotNull(updatedPerson.getGender());
        assertNotNull(updatedPerson.getEmail());

        assertTrue(updatedPerson.getId() > 0);
        assertEquals("Leonardo", updatedPerson.getFirstName());
        assertEquals("Costa", updatedPerson.getLastName());
        assertEquals("Minas Gerais", updatedPerson.getAddress());
        assertEquals("Male", updatedPerson.getGender());
        assertEquals("leonardo@erudio.com.br", updatedPerson.getEmail());
    }

    @DisplayName("JUnit integration given Person Object when findById should Return a Updated Person Object")
    @Test
    @Order(3)
    void integrationTestGivenPersonObject_when_findById_ShouldReturnAUpdatedPersonObject() throws JsonProcessingException {

        String content = given().spec(specification)
                .pathParam("id", person.getId())
            .when()
                .get("{id}")
            .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();

        Person foundPerson = objectMapper.readValue(content, Person.class);

        assertNotNull(foundPerson);
        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());
        assertNotNull(foundPerson.getEmail());

        assertTrue(foundPerson.getId() > 0);
        assertEquals("Leonardo", foundPerson.getFirstName());
        assertEquals("Costa", foundPerson.getLastName());
        assertEquals("Minas Gerais", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
        assertEquals("leonardo@erudio.com.br", foundPerson.getEmail());
    }

    @DisplayName("JUnit integration given Person Object when findById should Return a Updated Person Object")
    @Test
    @Order(4)
    void integrationTest_when_findAll_ShouldReturnAPersonsList() throws JsonProcessingException {

        Person anotherPerson = new Person(
                "Gabriela",
                "Rodriguez",
                "gabi@erudio.com.br",
                "Uberlândia - Minas Gerais - Brasil",
                "Female");

        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(anotherPerson)
                .when()
                .post()
                .then()
                .statusCode(200);

        String content = given().spec(specification)
            .when()
                .get()
            .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();

        Person[] myArray = objectMapper.readValue(content, Person[].class);
        List<Person> people = List.of(myArray);

        Person foundPerson = people.get(0);

        assertNotNull(foundPerson);
        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());
        assertNotNull(foundPerson.getEmail());

        assertTrue(foundPerson.getId() > 0);
        assertEquals("Leonardo", foundPerson.getFirstName());
        assertEquals("Costa", foundPerson.getLastName());
        assertEquals("Minas Gerais", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
        assertEquals("leonardo@erudio.com.br", foundPerson.getEmail());

        Person foundPerson2 = people.get(1);

        assertNotNull(foundPerson2);
        assertNotNull(foundPerson2.getId());
        assertNotNull(foundPerson2.getFirstName());
        assertNotNull(foundPerson2.getLastName());
        assertNotNull(foundPerson2.getAddress());
        assertNotNull(foundPerson2.getGender());
        assertNotNull(foundPerson2.getEmail());

        assertTrue(foundPerson2.getId() > 0);
        assertEquals("Gabriela", foundPerson2.getFirstName());
        assertEquals("Rodriguez", foundPerson2.getLastName());
        assertEquals("Uberlândia - Minas Gerais - Brasil", foundPerson2.getAddress());
        assertEquals("Female", foundPerson2.getGender());
        assertEquals("gabi@erudio.com.br", foundPerson2.getEmail());
    }


    @DisplayName("JUnit integration given Person Object when Delete should Return no content")
    @Test
    @Order(5)
    void integrationTestGivenPersonObject_when_delete_ShouldReturnNoContent() throws JsonProcessingException {

        given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }
}