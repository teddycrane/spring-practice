package com.teddycrane.springpractice.service;

import com.teddycrane.springpractice.entity.User;
import com.teddycrane.springpractice.repository.UserRepository;
import com.teddycrane.springpractice.service.model.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

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
}
