package com.teddycrane.springpractice.user;

import com.teddycrane.springpractice.error.*;
import com.teddycrane.springpractice.models.BaseController;
import com.teddycrane.springpractice.user.model.IUserController;
import com.teddycrane.springpractice.user.model.IUserService;
import com.teddycrane.springpractice.user.request.UpdateUserRequest;
import com.teddycrane.springpractice.user.request.AuthenticationRequest;
import com.teddycrane.springpractice.user.request.CreateUserRequest;
import com.teddycrane.springpractice.user.response.AuthenticationResponse;
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
		}
	}
}
