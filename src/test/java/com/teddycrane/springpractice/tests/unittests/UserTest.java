package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.user.User;

import org.junit.jupiter.api.*;

class UserTest {

    private User user;

    @BeforeEach
    public void init() {
        user = new User();
    }

    @Test
    public void shouldCreateUser() {
        Assertions.assertNotNull(user);
    }

    @Test
    public void shouldCreateUserWithConstructor() {
        user = new User(UserType.USER, "test", "name", "testuser", "password");

        Assertions.assertEquals("test", user.getFirstName());
        Assertions.assertEquals("name", user.getLastName());
        Assertions.assertEquals("testuser", user.getUsername());
        Assertions.assertEquals("password", user.getPassword());
    }
}
