package com.teddycrane.springpractice.auth;

import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.UserNotFoundError;

import org.springframework.stereotype.Service;

@Service
public interface IAuthService {
    boolean isResourceAllowedForUser(String userId, String resourcePath, String method) throws UserNotFoundError, BadRequestException;
}