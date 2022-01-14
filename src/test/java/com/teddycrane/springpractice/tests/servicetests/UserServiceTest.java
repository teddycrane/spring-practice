package com.teddycrane.springpractice.tests.servicetests;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.teddycrane.springpractice.enums.UserStatus;
import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.UserNotFoundError;
import com.teddycrane.springpractice.helper.JwtHelper;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import com.teddycrane.springpractice.user.User;
import com.teddycrane.springpractice.user.UserService;
import com.teddycrane.springpractice.user.model.IUserService;
import com.teddycrane.springpractice.user.model.UserRepository;
import com.teddycrane.springpractice.user.response.PasswordResetResponse;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

  private IUserService userService;

  private JwtHelper jwtHelper =
      new JwtHelper(
          Base64.getEncoder()
              .encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded()));

  @Mock private UserRepository userRepository;

  @Captor private ArgumentCaptor<String> originalPasswordCaptor;
  @Captor private ArgumentCaptor<User> userCaptor;

  private Iterable<User> userList;
  private User user;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    this.userService = new UserService(this.userRepository, this.jwtHelper);
    userList = TestResourceGenerator.generateUserList(5);
    user = TestResourceGenerator.generateUser();
  }

  @Test
  public void shouldGetAllUsers() {
    List<User> list = new ArrayList<>();
    userList.forEach((user) -> list.add(user));
    when(this.userRepository.findAll()).thenReturn(list);
    Collection<User> result = this.userService.getAllUsers();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(5, result.size());
  }

  @Test
  public void shouldGetASingleUser() {
    User u = TestResourceGenerator.generateUser();
    when(this.userRepository.findById(u.getId())).thenReturn(Optional.of(u));

    User result = this.userService.getUser(u.getId());

    Assertions.assertEquals(u, result);
  }

  @Test
  public void shouldThrowErrorIfUserDoesNotExist() {
    when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    Assertions.assertThrows(
        UserNotFoundError.class, () -> this.userService.getUser(UUID.randomUUID()));
  }

  @Test
  public void shouldCreateUser() {
    when(this.userRepository.save(any(User.class))).then(returnsFirstArg());

    User result =
        this.userService.createUser(
            "firstName",
            "lastName",
            "username",
            "email@email.com",
            "password",
            Optional.of(UserType.USER));

    Assertions.assertNotNull(result);
    Assertions.assertEquals("firstName", result.getFirstName());
    Assertions.assertEquals("lastName", result.getLastName());
    Assertions.assertEquals("username", result.getUsername());
    Assertions.assertEquals("email@email.com", result.getEmail());
    // assert that the password is hashed and not equal to the input
    Assertions.assertNotEquals("password", result.getPassword());
  }

  @Test
  public void shouldHandleUserCollisions() {
    User existing = TestResourceGenerator.generateUser();
    when(this.userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(existing));

    Assertions.assertThrows(
        DuplicateItemException.class,
        () ->
            this.userService.createUser(
                existing.getFirstName(),
                existing.getLastName(),
                existing.getUsername(),
                existing.getPassword(),
                existing.getEmail(),
                Optional.empty()));
  }

  @Test
  public void shouldAllowUserUpdatesWithAllFields() {
    User original = TestResourceGenerator.generateUser();
    UUID id = original.getId();
    when(this.userRepository.findById(id)).thenReturn(Optional.of(original));
    when(this.userRepository.save(any(User.class))).then(returnsFirstArg());

    try {
      User result =
          this.userService.updateUser(
              id,
              Optional.of("newUserName"),
              Optional.of("newPassword"),
              Optional.of("newFirstName"),
              Optional.of("newLastName"),
              Optional.of("newEmail@email.com"),
              Optional.of(UserType.ADMIN));

      Assertions.assertNotNull(result);
    } catch (IllegalAccessException e) {
      // fail test if exception is thrown
      Assertions.assertTrue(false);
    }
  }

  @Test
  public void shouldNotUpdateIfNoUserIsFound() {
    when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    Assertions.assertThrows(
        UserNotFoundError.class,
        () ->
            this.userService.updateUser(
                UUID.randomUUID(),
                Optional.of("newUserName"),
                Optional.of("newPassword"),
                Optional.of("newFirstName"),
                Optional.of("newLastName"),
                Optional.of("newEmail@email.com"),
                Optional.of(UserType.ADMIN)));
  }

  @Test
  public void shouldDisallowUpdateIfUserIsNotActive() {
    User u = TestResourceGenerator.generateUser();
    u.setStatus(UserStatus.DISABLED);

    when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.of(u));

    Assertions.assertThrows(
        IllegalAccessException.class,
        () ->
            this.userService.updateUser(
                UUID.randomUUID(),
                Optional.of("newUserName"),
                Optional.of("newPassword"),
                Optional.of("newFirstName"),
                Optional.of("newLastName"),
                Optional.of("newEmail@email.com"),
                Optional.of(UserType.ADMIN)));
  }

  @Test
  public void shouldResetPasswordForUser() {
    when(this.userRepository.findById(this.user.getId())).thenReturn(Optional.of(this.user));

    PasswordResetResponse response = this.userService.resetPassword(this.user.getId());
    Assertions.assertNotNull(response);
  }

  @Test
  public void shouldThrowErrorIfResettingPasswordForNonExistentUser() {
    when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    Assertions.assertThrows(
        UserNotFoundError.class, () -> this.userService.resetPassword(UUID.randomUUID()));
  }
}
