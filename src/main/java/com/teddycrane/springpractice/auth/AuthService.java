package com.teddycrane.springpractice.auth;

import com.teddycrane.springpractice.enums.UserStatus;
import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.UserNotFoundError;
import com.teddycrane.springpractice.models.BaseService;
import com.teddycrane.springpractice.user.User;
import com.teddycrane.springpractice.user.model.UserRepository;
import java.util.Optional;
import java.util.UUID;
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

    // allow all resources for base users, this should be reverted
    return true;
    // // explicit disallow for PATCH /users
    // if (resourcePath.contains("/users") && method.equalsIgnoreCase("PATCH")) {
    // return false;
    // } else {
    // return true;
    // }
  }

  /**
   * Checks if a resource is available based on role and status
   *
   * @param role The user role to check
   * @param status The user Status to check
   * @param resourcePath The resource path to check
   * @param method the HTTP method to check
   * @return True if allowed, otherwise false.
   */
  private boolean isResourceAllowedForRoleAndStatus(
      UserType role, UserStatus status, String resourcePath, String method) {
    logger.info("Checking permissions for role {} and status {}", role, status);

    switch (status) {
      case ACTIVE:
        return isResourceAllowedForRole(role, resourcePath, method);
      case DISABLED:
        return false;
      case PASSWORDCHANGEREQUIRED:
        {
          // for future, allow change password endpoint here
          if (resourcePath.equals("/users/change-password") && method.equalsIgnoreCase("POST")) {
            return true;
          } else {
            return false;
          }
        }
      default:
        return false;
    }
  }

  /**
   * Checks if a resource is allowed for a given role
   *
   * @param role The role to check access for
   * @param resourcePath The URI for the resource to check
   * @param method The HTTP method in question
   * @return Returns true if the role is allowed access, otherwise, returns false
   */
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
      UserStatus status = u.getStatus();

      return this.isResourceAllowedForRoleAndStatus(role, status, resourcePath, method);
    } catch (IllegalArgumentException e) {
      logger.error("Bad user id provided");
      throw new BadRequestException(e.getMessage());
    }
  }
}
