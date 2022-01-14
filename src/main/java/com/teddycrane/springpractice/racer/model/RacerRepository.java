package com.teddycrane.springpractice.racer.model;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.racer.Racer;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RacerRepository extends JpaRepository<Racer, UUID> {

  List<Racer> findByFirstNameAndLastName(String firstName, String lastName);

  Iterable<Racer> findByFirstNameContaining(String firstName);

  Iterable<Racer> findByLastNameContaining(String lastName);

  List<Racer> findByCategory(Category category);
}
