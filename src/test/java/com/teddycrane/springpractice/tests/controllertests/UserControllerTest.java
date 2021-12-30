package com.teddycrane.springpractice.tests.controllertests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.user.User;
import com.teddycrane.springpractice.user.UserController;
import com.teddycrane.springpractice.user.model.IUserController;
import com.teddycrane.springpractice.user.model.IUserService;
import com.teddycrane.springpractice.user.request.AuthenticationRequest;
import com.teddycrane.springpractice.user.request.CreateUserRequest;
import com.teddycrane.springpractice.user.response.AuthenticationResponse;

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

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        this.userController = new UserController(this.userService);
        this.user = new User(UserType.USER, "first", "last", "username", "password", "email@email.fake");
        this.testUUID = user.getId();
    }

    @Test
    public void shouldGetRace() {
        when(this.userService.getUser(testUUID)).thenReturn(user);

        User result = this.userController.getUser(testUUID.toString());
        verify(userService).getUser(uuidCaptor.capture());

        Assertions.assertEquals(testUUID, uuidCaptor.getValue());
        Assertions.assertTrue(result.equals(user));
    }

    @Test
    public void shouldExceptWhenInvalidUUIDProvided() {
        String invalidUUID = "test string";

        Assertions.assertThrows(BadRequestException.class, () -> this.userController.getUser(invalidUUID));
    }

    @Test
    public void shouldGetAllUsers() {
        Collection<User> users = new ArrayList<>();
        users.add(user);
        when(this.userService.getAllUsers()).thenReturn(users);

        Collection<User> result = this.userController.getAllUsers();

        Assertions.assertTrue(result.equals(users));
    }

    @Test
    public void shouldAuthenticateValidUsersWithUsername() {
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
    public void shouldAuthenticateWithEmail() {
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
    public void shouldNotAuthenticateWithoutEmailOrUsername() {
        Assertions.assertThrows(BadRequestException.class,
                () -> this.userController.login(new AuthenticationRequest(null, null, "password")));
    }

    @Test
    public void shouldCreateValidUserWithType() {
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
}
