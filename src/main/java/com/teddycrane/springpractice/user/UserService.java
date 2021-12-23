package com.teddycrane.springpractice.user;

import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.InternalServerError;
import com.teddycrane.springpractice.error.NotAuthenticatedException;
import com.teddycrane.springpractice.error.UserNotFoundError;
import com.teddycrane.springpractice.helper.JwtHelper;
import com.teddycrane.springpractice.models.BaseService;
import com.teddycrane.springpractice.models.UserData;
import com.teddycrane.springpractice.user.model.UserRepository;
import com.teddycrane.springpractice.user.model.IUserService;
import com.teddycrane.springpractice.user.response.AuthenticationResponse;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService extends BaseService implements IUserService
{

	private final UserRepository userRepository;
	private final JwtHelper tokenHelper;

	public UserService(UserRepository userRepository, JwtHelper tokenHelper)
	{
		super();
		this.userRepository = userRepository;
		this.tokenHelper = tokenHelper;
	}

	@Override
	public Collection<User> getAllUsers()
	{
		logger.trace("getAllUsers called");

		Iterable<User> dbItems = this.userRepository.findAll();
		Collection<User> result = new ArrayList<>();
		dbItems.forEach(result::add);
		return result;
	}

	@Override
	public User getUser(UUID id) throws UserNotFoundError
	{
		logger.trace("getUser called");

		Optional<User> _user = this.userRepository.findById(id);

		if (_user.isPresent())
		{
			return _user.get();
		} else
		{
			logger.error("No user found for the id {}", id);
			throw new UserNotFoundError("No user found for the provided id!");
		}
	}

	private String getSecurePassword(String rawPassword)
	{
		String result = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytes = md.digest(rawPassword.getBytes());
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < bytes.length; i++)
			{
				builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			result = builder.toString();

		} catch (NoSuchAlgorithmException e)
		{
			logger.error("No such algorithm!");
		}

		return result;
	}

	@Override
	public User createUser(String firstName, String lastName, String userName, String password, Optional<UserType> type) throws DuplicateItemException, InternalServerError
	{
		logger.trace("createUser called");
		Optional<User> existing = this.userRepository.findByUsername(userName);

		if (existing.isPresent())
		{
			logger.error("Unable to create a user with a duplicate username!");
			throw new DuplicateItemException("Unable to create a user with a duplicate username!");
		}

		// hash password
		String hashedPassword = getSecurePassword(password);
		logger.info("Hashed password {}", hashedPassword);

		return this.userRepository.save(new User(type.orElse(UserType.USER), firstName,
				lastName,
				userName,
				hashedPassword));
	}

	@Override
	public AuthenticationResponse login(String username, String password) throws NotAuthenticatedException, UserNotFoundError
	{
		logger.trace("login called");
		Optional<User> user = this.userRepository.findByUsername(username);

		if (user.isPresent())
		{
			User u = user.get();
			logger.info("login requested for user {}", u.getId());

			// this is the user provided password that should be compared to the one in the db
			String hashedProvidedPassword = getSecurePassword(password);

			if (hashedProvidedPassword.equals(u.getPassword()))
			{
				UserData userDataObject = new UserData(u.getUsername(), u.getId().toString(), u.getFirstName() + " " + u.getLastName());
				String token = this.tokenHelper.generateToken(userDataObject);
				return new AuthenticationResponse(true, token);
			} else
			{
				logger.info("The username and password combo provided are not valid");
				throw new NotAuthenticatedException("An invalid username/password combination was provided.");
			}
		} else
		{
			logger.error("Unable to find the user {}", username);
			throw new UserNotFoundError("No user exists with the provided id!");
		}

	}

	@Override
	public User updateUser(UUID id,
							Optional<String> username,
							Optional<String> password,
							Optional<String> firstName,
			Optional<String> lastName, Optional<UserType> userType) throws UserNotFoundError 
	{
		logger.trace("updateUser called");

		Optional<User> existing = this.userRepository.findById(id);

		if(existing.isEmpty())
		{
			logger.error("No user found for id {}", id);
			throw new UserNotFoundError("No user found for the provided id");
		}

		User user = existing.get();
		
		if (username.isPresent()) user.setUsername(username.get());
		if (password.isPresent()) user.setPassword(password.get());
		if (firstName.isPresent()) user.setFirstName(firstName.get());
		if (lastName.isPresent()) user.setLastName(lastName.get());
		// todo set up enum validation
		if (userType.isPresent()) user.setType(userType.get());

		return this.userRepository.save(user);
	}
}
