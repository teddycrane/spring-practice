package com.teddycrane.springpractice.user.model;

import com.teddycrane.springpractice.user.User;
import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.UserNotFoundError;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public interface IUserService
{
	Collection<User> getAllUsers();

	User getUser(UUID id) throws UserNotFoundError;

	User createUser(String firstName, String lastName, Optional<UserType> type) throws DuplicateItemException;
}
