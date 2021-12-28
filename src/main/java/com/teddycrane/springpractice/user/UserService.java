package com.teddycrane.springpractice.user;

import com.github.javafaker.Faker;
import com.teddycrane.springpractice.enums.UserSearchType;
import com.teddycrane.springpractice.enums.UserStatus;
import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.InternalServerError;
import com.teddycrane.springpractice.error.NotAuthenticatedException;
import com.teddycrane.springpractice.error.UserNotFoundError;
import com.teddycrane.springpractice.helper.JwtHelper;
import com.teddycrane.springpractice.models.BaseService;
import com.teddycrane.springpractice.models.Either;
import com.teddycrane.springpractice.models.UserData;
import com.teddycrane.springpractice.user.model.UserRepository;
import com.teddycrane.springpractice.user.model.IUserService;
import com.teddycrane.springpractice.user.response.AuthenticationResponse;
import com.teddycrane.springpractice.user.response.PasswordChangeResponse;
import com.teddycrane.springpractice.user.response.PasswordResetResponse;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService extends BaseService implements IUserService {

	private final UserRepository userRepository;
	private final JwtHelper tokenHelper;

	public UserService(UserRepository userRepository, JwtHelper tokenHelper) {
		super();
		this.userRepository = userRepository;
		this.tokenHelper = tokenHelper;
	}

	@Override
	public Collection<User> getAllUsers() {
		logger.trace("getAllUsers called");

		Iterable<User> dbItems = this.userRepository.findAll();
		Collection<User> result = new ArrayList<>();
		dbItems.forEach(result::add);
		return result;
	}

	@Override
	public User getUser(UUID id) throws UserNotFoundError {
		logger.trace("getUser called");

		Optional<User> _user = this.userRepository.findById(id);

		if (_user.isPresent()) {
			return _user.get();
		} else {
			logger.error("No user found for the id {}", id);
			throw new UserNotFoundError("No user found for the provided id!");
		}
	}

	private String getSecurePassword(String rawPassword) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytes = md.digest(rawPassword.getBytes());
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			result = builder.toString();

		} catch (NoSuchAlgorithmException e) {
			logger.error("No such algorithm!");
		}

		return result;
	}

	@Override
	public User createUser(String firstName,
			String lastName,
			String userName,
			String email,
			String password,
			Optional<UserType> type)
			throws DuplicateItemException,
			InternalServerError {
		logger.trace("createUser called");
		Optional<User> existing = this.userRepository.findByUsername(userName);

		if (existing.isPresent()) {
			logger.error("Unable to create a user with a duplicate username!");
			throw new DuplicateItemException("Unable to create a user with a duplicate username!");
		}

		// hash password
		String hashedPassword = getSecurePassword(password);
		return this.userRepository.save(new User(type.orElse(UserType.USER), firstName,
				lastName,
				userName,
				hashedPassword,
				UserStatus.ACTIVE));
	}

	@Override
	public AuthenticationResponse login(String username, String email, String password)
			throws NotAuthenticatedException, UserNotFoundError {
		logger.trace("login called");
		Optional<User> user;

		if (username != null) {
			user = this.userRepository.findByUsername(username);
		} else if (email != null) {
			user = this.userRepository.findByEmail(email);
		} else {
			logger.info("No email or username provided");
			user = Optional.empty();
		}

		if (user.isPresent()) {
			User u = user.get();
			logger.info("login requested for user {}", u.getId());

			if (u.getStatus() != UserStatus.ACTIVE) {
				logger.error("Cannot authenticate an inactive user");
				throw new NotAuthenticatedException(
						"The specified user is not active.  Please contact an administrator");
			}

			// this is the user provided password that should be compared to the one in the
			// db
			String hashedProvidedPassword = getSecurePassword(password);

			if (hashedProvidedPassword.equals(u.getPassword())) {
				UserData userDataObject = new UserData(u.getUsername(), u.getId().toString(),
						u.getFirstName() + " " + u.getLastName());
				String token = this.tokenHelper.generateToken(userDataObject);
				return new AuthenticationResponse(true, token);
			} else {
				logger.info("The username and password combo provided are not valid");
				throw new NotAuthenticatedException("An invalid username/password combination was provided.");
			}
		} else {
			logger.error("Unable to find the user {}", username);
			throw new UserNotFoundError("No user exists with the provided id!");
		}

	}

	@Override
	public User updateUser(UUID id,
			Optional<String> username,
			Optional<String> password,
			Optional<String> firstName,
			Optional<String> lastName,
			Optional<String> email,
			Optional<UserType> userType) throws IllegalAccessException, UserNotFoundError {
		logger.trace("updateUser called");

		Optional<User> existing = this.userRepository.findById(id);

		if (existing.isEmpty()) {
			logger.error("No user found for id {}", id);
			throw new UserNotFoundError("No user found for the provided id");
		}

		User user = existing.get();

		// keep this if for now, since we're not actually supporting disabled users yet
		if (user.getStatus() != UserStatus.ACTIVE) {
			throw new IllegalAccessException("The specified user is not an active user");
		}

		// todo set up logic for requiring a password reset before changing the status

		if (username.isPresent())
			user.setUsername(username.get());
		if (password.isPresent())
			user.setPassword(password.get());
		if (firstName.isPresent())
			user.setFirstName(firstName.get());
		if (lastName.isPresent())
			user.setLastName(lastName.get());
		// todo set up enum validation
		if (userType.isPresent())
			user.setType(userType.get());
		if (email.isPresent())
			user.setEmail(email.get());

		return this.userRepository.save(user);
	}

	private String generateRandomPassword() {
		Faker faker = new Faker(new Random());
		return faker.letterify("??????????????????????????");
	}

	@Override
	public PasswordResetResponse resetPassword(UUID id) throws UserNotFoundError {
		logger.trace("resetPassword called");
		Optional<User> user = this.userRepository.findById(id);

		if (user.isPresent()) {
			User u = user.get();
			String newPassword = this.generateRandomPassword();
			u.setPassword(this.getSecurePassword(newPassword));
			u.setStatus(UserStatus.PASSWORDCHANGEREQUIRED);
			this.userRepository.save(u);
			return new PasswordResetResponse(true, newPassword);
		} else {
			logger.error("No user found for the id {}", id);
			throw new UserNotFoundError("No user found!");
		}
	}

	@Override
	public Collection<User> searchUsersByTypeOrRole(UserSearchType type, Either<UserStatus, UserType> searchValue)
			throws BadRequestException {
		logger.trace("searchUsersByTypeOrRole called");

		Optional<UserStatus> statusValue = searchValue.fromLeft();
		Optional<UserType> typeValue = searchValue.fromRight();

		if (type == UserSearchType.TYPE && typeValue.isPresent()) {
			return this.userRepository.findByType(typeValue.get());
		} else if (type == UserSearchType.STATUS && statusValue.isPresent()) {
			return this.userRepository.findByStatus(statusValue.get());
		} else {
			logger.error("Search type and value are mismatched");
			throw new BadRequestException("Search type and search value are incompatible.");
		}
	}

	@Override
	public Collection<User> searchUsersByPrimitiveValue(UserSearchType type, String value)
			throws BadRequestException, UserNotFoundError {
		logger.trace("searchUsersByPrimitiveValue called");
		ArrayList<User> response = new ArrayList<>();

		if (type == UserSearchType.USERNAME) {
			Optional<User> result = this.userRepository.findByUsername(value);
			if (result.isPresent()) {
				response.add(result.get());
			} else {
				logger.error("No users found for the provided id");
				throw new UserNotFoundError("No user found for the provided value");
			}
		} else {
			// for now, searching by full name is not supported and should be ignored
			Iterable<User> dbRes = this.userRepository.findAll();
			dbRes.forEach(response::add);
		}
		return response;
	}

	@Override
	public PasswordChangeResponse changePassword(UUID userId, String oldPassword, String newPassword)
			throws UserNotFoundError {
		logger.trace("changePassword called");
		Optional<User> user = this.userRepository.findById(userId);

		if (user.isEmpty()) {
			logger.error("No user found for the id {}", userId);
			throw new UserNotFoundError("No user found for the provided id");
		}

		User u = user.get();

		// if the old password and the new password match, then update the password
		if (u.getPassword().equals(this.getSecurePassword(oldPassword))) {
			// hash new password and set
			u.setPassword(this.getSecurePassword(newPassword));
			// update status only if a password change is required, otherwise keep status
			// the same
			if (u.getStatus() == UserStatus.PASSWORDCHANGEREQUIRED)
				u.setStatus(UserStatus.ACTIVE);
		}
		User result = this.userRepository.save(u);
		return new PasswordChangeResponse(true, result.getUsername());
	}
}
