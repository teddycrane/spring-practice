package com.teddycrane.springpractice.user;

import com.teddycrane.springpractice.enums.UserSearchType;
import com.teddycrane.springpractice.enums.UserStatus;
import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.error.*;
import com.teddycrane.springpractice.models.BaseController;
import com.teddycrane.springpractice.models.Either;
import com.teddycrane.springpractice.user.model.IUserController;
import com.teddycrane.springpractice.user.model.IUserService;
import com.teddycrane.springpractice.user.request.UpdateUserRequest;
import com.teddycrane.springpractice.user.request.AuthenticationRequest;
import com.teddycrane.springpractice.user.request.CreateUserRequest;
import com.teddycrane.springpractice.user.response.AuthenticationResponse;
import com.teddycrane.springpractice.user.response.PasswordResetResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RequestMapping(path = "/users")
@RestController
public class UserController extends BaseController implements IUserController {

	private final IUserService userService;

	public UserController(IUserService userService) {
		super();
		this.userService = userService;
	}

	@Override
	public Collection<User> getAllUsers() {
		logger.trace("getAllUsers called");

		// todo add auth here
		return this.userService.getAllUsers();
	}

	@Override
	public User getUser(String id) throws BadRequestException, UserNotFoundError {
		logger.trace("getUser called");

		try {
			UUID requestId = UUID.fromString(id);
			return this.userService.getUser(requestId);
		} catch (IllegalArgumentException e) {
			logger.error("Unable to parse the id {}", id);
			throw new BadRequestException("Unable to parse the provided id");
		}
	}

	@Override
	public User createUser(CreateUserRequest request) throws BadRequestException, DuplicateItemException {
		logger.trace("createUser called");
		try {
			return this.userService.createUser(request.getFirstName(),
					request.getLastName(),
					request.getUsername(),
					request.getPassword(),
					request.getEmail(),
					request.getType());
		} catch (DuplicateItemException e) {
			throw new DuplicateItemException(e.getMessage());
		} catch (InternalServerError e) {
			throw new InternalServerError(e.getMessage());
		}
	}

	@Override
	public AuthenticationResponse login(AuthenticationRequest request)
			throws BadRequestException, NotAuthenticatedException, UserNotFoundError {
		logger.trace("login requested");

		Optional<String> username = request.getUsername();
		Optional<String> email = request.getEmail();

		if (!username.isPresent() && !email.isPresent()) {
			logger.error("Must provide an email or a username to log in");
			throw new BadRequestException("Must provide an email and password to log in");
		} else if (username.isPresent()) {
			// authenticate with username even if email is present
			return this.userService.login(username.get(), null, request.getPassword());
		} else if (email.isPresent()) {
			return this.userService.login(null, email.get(), request.getPassword());
		} else {
			logger.error("Generic bad request happened");
			throw new BadRequestException("Placeholder");
		}
	}

	@Override
	public User updateUser(UpdateUserRequest request) throws BadRequestException, UserNotFoundError {
		logger.trace("updateUser called");

		try {
			UUID id = UUID.fromString(request.getUserId());
			return this.userService.updateUser(id,
					request.getUsername(),
					request.getPassword(),
					request.getFirstName(),
					request.getLastName(),
					request.getEmail(),
					request.getUserType());
		} catch (IllegalArgumentException e) {
			logger.error("Unable to parse the provided uuid {}", request.getUserId());
			throw new BadRequestException("Unable to parse the provided user id");
		} catch (IllegalAccessException e) {
			logger.error("The user {} is disabled", request.getUserId());
			throw new BadRequestException("Unable to update a disabled user");
		}
	}

	@Override
	public PasswordResetResponse resetPassword(String userId) throws BadRequestException, UserNotFoundError {
		logger.trace("reset password called for user {}", userId);

		try {
			UUID id = UUID.fromString(userId);

			return this.userService.resetPassword(id);
		} catch (IllegalArgumentException e) {
			logger.error("Invalid UUID format {}", userId);

			throw new BadRequestException("Invalid user ID provided");
		}
	}

	@Override
	public Collection<User> searchUsers(String searchType, String searchValue) throws BadRequestException {
		logger.trace("searchUsers called");

		try {
			// validate search type and search terms
			UserSearchType parsedSearchType = UserSearchType.valueOf(searchType.toUpperCase());

			switch (parsedSearchType) {
				// todo consolidate TYPE and ROLE blocks
				case TYPE: {
					logger.info("Finding users by UserType");
					// validate search value
					UserType type = UserType.valueOf(searchValue.toUpperCase());
					return this.userService.searchUsersByTypeOrRole(parsedSearchType, Either.right(type));
				}
				case STATUS: {
					logger.info("Finding users by user status");
					UserStatus role = UserStatus.valueOf(searchValue.toUpperCase());
					return this.userService.searchUsersByTypeOrRole(parsedSearchType, Either.left(role));
				}
				case USERNAME:
				case FULLNAME: {
					logger.info("Finding users by primitive values");
				}
				default: {
					// default behavior is to get all users
					return this.getAllUsers();
				}
			}
		} catch (IllegalArgumentException e) {
			// handles enum valueOf errors
			logger.error("The provided search value of {} is not an enum value of {}", searchValue, searchType);
			throw new BadRequestException("The search value and search type provided are not compatible");
		}
	}

}
