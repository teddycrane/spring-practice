package com.teddycrane.springpractice.user.model;

import com.teddycrane.springpractice.error.InternalServerError;
import com.teddycrane.springpractice.error.NotAuthenticatedException;
import com.teddycrane.springpractice.user.User;
import com.teddycrane.springpractice.enums.UserSearchType;
import com.teddycrane.springpractice.enums.UserStatus;
import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.UserNotFoundError;
import com.teddycrane.springpractice.models.Either;
import com.teddycrane.springpractice.user.response.AuthenticationResponse;
import com.teddycrane.springpractice.user.response.PasswordResetResponse;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public interface IUserService {
	Collection<User> getAllUsers();

	User getUser(UUID id) throws UserNotFoundError;

	User createUser(String firstName, String lastName, String userName, String email, String password,
			Optional<UserType> type) throws DuplicateItemException, InternalServerError;

	AuthenticationResponse login(@Nullable String username, @Nullable String email, String password)
			throws NotAuthenticatedException, UserNotFoundError;

	User updateUser(UUID id, Optional<String> username, Optional<String> password, Optional<String> firstName,
			Optional<String> lastName, Optional<String> email, Optional<UserType> userType)
			throws IllegalAccessException, UserNotFoundError;

	PasswordResetResponse resetPassword(UUID id) throws UserNotFoundError;

	Collection<User> searchUsersByTypeOrRole(UserSearchType type, Either<UserStatus, UserType> searchValue)
			throws BadRequestException;

	Collection<User> searchUsersByPrimitiveValue(UserSearchType type, String value) throws BadRequestException;
}
