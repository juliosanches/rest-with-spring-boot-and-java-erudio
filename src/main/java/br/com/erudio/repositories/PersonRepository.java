package br.com.erudio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.erudio.model.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);
}