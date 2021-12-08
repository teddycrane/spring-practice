package com.teddycrane.springpractice.user.model;

import com.teddycrane.springpractice.error.InternalServerError;
import com.teddycrane.springpractice.error.NotAuthenticatedException;
import com.teddycrane.springpractice.user.User;
import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.UserNotFoundError;
import com.teddycrane.springpractice.user.response.AuthenticationResponse;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public interface IUserService
{
	Collection<User> getAllUsers();

	User getUser(UUID id) throws UserNotFoundError;

	User createUser(String firstName, String lastName, String userName, String password, Optional<UserType> type) throws DuplicateItemException, InternalServerError;

	AuthenticationResponse login(String username, String password) throws NotAuthenticatedException, UserNotFoundError;
}