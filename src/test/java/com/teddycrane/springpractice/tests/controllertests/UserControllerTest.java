package com.teddycrane.springpractice.tests.controllertests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.teddycrane.springpractice.enums.UserStatus;
import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.InternalServerError;
import com.teddycrane.springpractice.user.User;
import com.teddycrane.springpractice.user.UserController;
import com.teddycrane.springpractice.user.model.IUserController;
import com.teddycrane.springpractice.user.model.IUserService;
import com.teddycrane.springpractice.user.request.AuthenticationRequest;
import com.teddycrane.springpractice.user.request.CreateUserRequest;
import com.teddycrane.springpractice.user.request.UpdateUserRequest;
import com.teddycrane.springpractice.user.response.AuthenticationResponse;
import com.teddycrane.springpractice.user.response.PasswordResetResponse;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserControllerTest {
    private IUserController userController;
    private User user;
    private UUID testUUID;

    @Mock
    private IUserService userService;

    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;

    @Captor
    private ArgumentCaptor<String> usernameCaptor, emailCaptor, passwordCaptor, fNameCaptor, lNameCaptor;

    @Captor
    private ArgumentCaptor<Optional<UserType>> typeCaptor;

    @Captor
    ArgumentCaptor<Optional<String>> usernameOptional, passwordOptional, firstNameOptional, lastNameOptional,
            emailOptional;

    @Captor
    ArgumentCaptor<Optional<UserType>> userTypeOptional;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        this.userController = new UserController(this.userService);
        this.user = new User(UserType.USER, "first", "last", "username", "password", "email@email.fake");
        this.testUUID = user.getId();
    }

    @Test
    public void getUser_shouldGetUser() {
        when(this.userService.getUser(testUUID)).thenReturn(user);

        User result = this.userController.getUser(testUUID.toString());
        verify(userService).getUser(uuidCaptor.capture());

        Assertions.assertEquals(testUUID, uuidCaptor.getValue());
        Assertions.assertTrue(result.equals(user));
    }

    @Test
    public void getUser_shouldExceptWhenInvalidUUIDProvided() {
        String invalidUUID = "test string";

        Assertions.assertThrows(BadRequestException.class, () -> this.userController.getUser(invalidUUID));
    }

    @Test
    public void getAllUsers_shouldGetAllUsers() {
        Collection<User> users = new ArrayList<>();
        users.add(user);
        when(this.userService.getAllUsers()).thenReturn(users);

        Collection<User> result = this.userController.getAllUsers();

        Assertions.assertTrue(result.equals(users));
    }

    @Test
    public void login_shouldAuthenticateValidUsersWithUsername() {
        AuthenticationResponse response = new AuthenticationResponse(true, "mock-token");
        when(this.userService.login("username", null, "password")).thenReturn(response);

        AuthenticationResponse result = this.userController
                .login(new AuthenticationRequest("username", null, "password"));
        verify(this.userService).login(usernameCaptor.capture(), emailCaptor.capture(), passwordCaptor.capture());

        Assertions.assertEquals("username", usernameCaptor.getValue());
        Assertions.assertEquals(null, emailCaptor.getValue());
        Assertions.assertEquals("password", passwordCaptor.getValue());

        Assertions.assertTrue(response.equals(result));
    }

    @Test
    public void login_shouldAuthenticateWithEmail() {
        AuthenticationResponse response = new AuthenticationResponse(true, "mock-token");
        when(this.userService.login(null, "email", "password")).thenReturn(response);

        AuthenticationResponse result = this.userController
                .login(new AuthenticationRequest(null, "email", "password"));
        verify(this.userService).login(usernameCaptor.capture(), emailCaptor.capture(), passwordCaptor.capture());

        Assertions.assertEquals(null, usernameCaptor.getValue());
        Assertions.assertEquals("email", emailCaptor.getValue());
        Assertions.assertEquals("password", passwordCaptor.getValue());

        Assertions.assertTrue(response.equals(result));
    }

    @Test
    public void login_shouldNotAuthenticateWithoutEmailOrUsername() {
        Assertions.assertThrows(BadRequestException.class,
                () -> this.userController.login(new AuthenticationRequest(null, null, "password")));
    }

    @Test
    public void createUser_shouldCreateValidUserWithType() {
        CreateUserRequest request = new CreateUserRequest("username", "password", "firstName", "lastName",
                "email@email.com", UserType.ADMIN);
        User u = new User(UserType.ADMIN, "firstName", "lastName", "username", "password", "email@email.com");
        when(this.userService.createUser("firstName", "lastName", "username", "email@email.com", "password",
                Optional.of(UserType.ADMIN))).thenReturn(u);

        User result = this.userController.createUser(request);
        verify(this.userService).createUser(fNameCaptor.capture(), lNameCaptor.capture(), usernameCaptor.capture(),
                emailCaptor.capture(), passwordCaptor.capture(), typeCaptor.capture());

        Assertions.assertEquals("firstName", fNameCaptor.getValue());
        Assertions.assertEquals("lastName", lNameCaptor.getValue());
        Assertions.assertEquals("username", usernameCaptor.getValue());
        Assertions.assertEquals("password", passwordCaptor.getValue());
        Assertions.assertEquals("email@email.com", emailCaptor.getValue());
        Assertions.assertTrue(typeCaptor.getValue().isPresent());
        Assertions.assertEquals(UserType.ADMIN, typeCaptor.getValue().get());

        Assertions.assertTrue(u.equals(result));
    }

    @Test
    public void createUser_shouldHandleUserCreationErrors() {
        CreateUserRequest request = new CreateUserRequest("username", "password", "firstName", "lastName",
                "email@email.com", UserType.ADMIN);

        when(this.userService.createUser("firstName", "lastName", "username", "email@email.com", "password",
                Optional.of(UserType.ADMIN))).thenThrow(DuplicateItemException.class);

        Assertions.assertThrows(DuplicateItemException.class, () -> this.userController.createUser(request));
    }

    @Test
    public void createUser_shouldHandleInternalServerErrors() {
        CreateUserRequest request = new CreateUserRequest("username", "password", "firstName", "lastName",
                "email@email.com", UserType.ADMIN);
        when(this.userService.createUser("firstName", "lastName", "username", "email@email.com", "password",
                Optional.of(UserType.ADMIN))).thenThrow(InternalServerError.class);

        Assertions.assertThrows(InternalServerError.class, () -> this.userController.createUser(request));
    }

    @Test
    public void updateUser_shouldUpdateUserWithAllValues() {
        UUID id = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest(id.toString(), "firstName", "lastName",
                "password", "username", "email@email.com", UserType.USER);

        User expected = new User(id, UserType.USER, "firstName", "lastName", "username", "password", "email@email.com",
                UserStatus.ACTIVE);

        try {
            when(this.userService.updateUser(id, Optional.of("username"), Optional.of("password"),
                    Optional.of("firstName"),
                    Optional.of("lastName"), Optional.of("email@email.com"), Optional.of(UserType.USER)))
                            .thenReturn(expected);

            User result = this.userController.updateUser(request);
            verify(this.userService).updateUser(
                    uuidCaptor.capture(),
                    usernameOptional.capture(),
                    passwordOptional.capture(),
                    firstNameOptional.capture(),
                    lastNameOptional.capture(),
                    emailOptional.capture(),
                    userTypeOptional.capture());

            Assertions.assertEquals(id, uuidCaptor.getValue());
            Assertions.assertEquals("username", usernameOptional.getValue().get());
            Assertions.assertEquals("password", passwordOptional.getValue().get());
            Assertions.assertEquals("firstName", firstNameOptional.getValue().get());
            Assertions.assertEquals("lastName", lastNameOptional.getValue().get());

            Assertions.assertTrue(expected.equals(result));

        } catch (IllegalAccessException e) {
            // fail test because for some reason we have to have a try/catch here wtf
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void updateUser_shouldHandleInvalidUUIDForUpdatingUsers() {
        UpdateUserRequest request = new UpdateUserRequest("bad-uuid", "firstName", "lastName",
                "password", "username", "email@email.com", UserType.USER);

        Assertions.assertThrows(BadRequestException.class, () -> this.userController.updateUser(request));
    }

    @Test
    public void updateUser_shouldFailForInactiveUsers() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest(testUUID.toString(), "firstName", "lastName",
                "password", "username", "email@email.com", UserType.USER);

        when(this.userService.updateUser(eq(testUUID), any(Optional.class), any(Optional.class), any(Optional.class),
                any(Optional.class), any(Optional.class), any(Optional.class))).thenThrow(IllegalAccessException.class);

        Assertions.assertThrows(BadRequestException.class, () -> this.userController.updateUser(request));
    }

    @Test
    public void resetPassword_shouldResetPassword() {
        UUID id = UUID.randomUUID();
        when(this.userService.resetPassword(id)).thenReturn(new PasswordResetResponse());

        PasswordResetResponse response = this.userController.resetPassword(id.toString());
        verify(this.userService).resetPassword(uuidCaptor.capture());

        Assertions.assertEquals(id, uuidCaptor.getValue());

        Assertions.assertEquals(true, response.getSuccess());
        Assertions.assertEquals("", response.getNewPassword());

        // test uuid error
        Assertions.assertThrows(BadRequestException.class, () -> this.userController.resetPassword("test"));
    }
}
