package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.enums.UserStatus;
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
    user = new User(UserType.USER, "test", "name", "testuser", "password", "email@email.com");

    Assertions.assertEquals("test", user.getFirstName());
    Assertions.assertEquals("name", user.getLastName());
    Assertions.assertEquals("testuser", user.getUsername());
    Assertions.assertEquals("password", user.getPassword());
  }

  @Test
  public void shouldHandleEmailsOnConstruction() {
    user = new User(UserType.USER, "test", "name", "testuser", "password", "email");

    Assertions.assertEquals("", user.getEmail());

    user.setEmail("email@email.com");
    Assertions.assertEquals("email@email.com", user.getEmail());

    Assertions.assertThrows(IllegalArgumentException.class, () -> user.setEmail(""));
  }

  @Test
  public void shouldCorrectlyFormatToString() {
    User u =
        new User(
            UserType.USER,
            "first",
            "last",
            "test",
            "password",
            "email@email.com",
            UserStatus.ACTIVE);
    Assertions.assertNotNull(u.toString());
  }

  @Test
  public void testEquality() {
    User u =
        new User(
            UserType.USER,
            "first",
            "last",
            "test",
            "password",
            "email@email.com",
            UserStatus.ACTIVE);
    User other = new User(u);

    Assertions.assertTrue(u.equals(other));

    // test property change one by one
    other.setEmail("test@test.com");
    Assertions.assertFalse(u.equals(other));

    other.setPassword("test");
    Assertions.assertFalse(u.equals(other));

    other.setUsername("other");
    Assertions.assertFalse(u.equals(other));

    other.setType(UserType.ROOT);
    Assertions.assertFalse(u.equals(other));

    other.setIsDeleted(true);
    Assertions.assertFalse(u.equals(other));

    other.setLastName("different");
    Assertions.assertFalse(u.equals(other));

    other.setFirstName("firstNamediff");
    Assertions.assertFalse(u.equals(other));

    User newUser = new User();
    Assertions.assertFalse(u.equals(newUser));

    // test different types equals
    Assertions.assertFalse(u.equals(""));

    Assertions.assertNotNull(new User(UserType.USER, "First", "last"));
    Assertions.assertFalse(user.getIsDeleted());
  }

  @Test
  public void hashCode_shouldGenerate() {
    Assertions.assertNotNull(user.hashCode());
  }
}
