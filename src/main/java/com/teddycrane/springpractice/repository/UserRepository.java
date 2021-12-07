package com.teddycrane.springpractice.repository;

import com.teddycrane.springpractice.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID>
{

	Collection<User> findByFirstNameAndLastName(String firstName, String lastName);
}
