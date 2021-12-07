package com.teddycrane.springpractice.user;

import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.UserNotFoundError;
import com.teddycrane.springpractice.models.BaseService;
import com.teddycrane.springpractice.user.model.UserRepository;
import com.teddycrane.springpractice.user.model.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService extends BaseService implements IUserService
{

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository)
	{
		super();
		this.userRepository = userRepository;
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

	@Override
	public User createUser(String firstName, String lastName, String userName, String password, Optional<UserType> type) throws DuplicateItemException
	{
		logger.trace("createUser called");

		// allowing name duplication since we have the UUID as a differentiator
		return this.userRepository.save(new User(firstName, lastName, type.orElse(UserType.USER), userName, password));
	}
}
