package com.teddycrane.springpractice.service.model;

import com.teddycrane.springpractice.entity.User;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface IUserService
{
	Collection<User> getAllUsers();
}
