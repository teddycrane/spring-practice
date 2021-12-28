package com.teddycrane.springpractice.user.model;

import com.teddycrane.springpractice.enums.UserStatus;
import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID>
{

	Collection<User> findByFirstNameAndLastName(String firstName, String lastName);

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	Collection<User> findByFirstName(String firstName);

	Collection<User> findByLastName(String lastName);

	Collection<User> findByType(UserType type);

	Collection<User> findByStatus(UserStatus status);
}
