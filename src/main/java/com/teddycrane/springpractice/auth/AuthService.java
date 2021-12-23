package com.teddycrane.springpractice.auth;

import java.util.Optional;
import java.util.UUID;

import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.UserNotFoundError;
import com.teddycrane.springpractice.models.BaseService;
import com.teddycrane.springpractice.user.User;
import com.teddycrane.springpractice.user.model.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class AuthService extends BaseService implements IAuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    private boolean isResourceAllowedForUser(String resourcePath, String method) {
        logger.trace("Checking permissions for {} {}", method, resourcePath);
        
        // explicit disallow for PATCH /users
        if (resourcePath.contains("/users") && method.equalsIgnoreCase("PATCH")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isResourceAllowedForRole(UserType role, String resourcePath, String method) {
        logger.info("Checking permissions for user type {}", role);

        switch (role) {
            case USER:
                return isResourceAllowedForUser(resourcePath, method);
            case ADMIN:
            case ROOT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isResourceAllowedForUser(String userId, String resourcePath, String method)
            throws UserNotFoundError, BadRequestException {
                logger.trace("Checking resource permissions");

        try {
            UUID id = UUID.fromString(userId);
            Optional<User> _user = this.userRepository.findById(id);

            if (_user.isEmpty()) {
                logger.error("Invalid user id in token");
                throw new UserNotFoundError("Invalid user id in token");
            }

            User u = _user.get();
            logger.info("Checking permissions for user {}", u.getId());
            UserType role = u.getType();

            return this.isResourceAllowedForRole(role, resourcePath, method);
        } catch (IllegalArgumentException e) {
            logger.error("Bad user id provided");
            throw new BadRequestException(e.getMessage());
        }

    }
}
