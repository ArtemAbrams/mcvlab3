package org.example.mcvlab3.repository;

import org.example.mcvlab3.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
