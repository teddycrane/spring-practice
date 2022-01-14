package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.helper.IJwtHelper;
import com.teddycrane.springpractice.helper.JwtHelper;
import com.teddycrane.springpractice.models.UserData;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JwtHelperTest {
  private IJwtHelper jwtHelper;

  @BeforeEach
  public void init() {
    String keyString = Base64.getEncoder().encodeToString(
        Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
    jwtHelper = new JwtHelper(keyString);
  }

  @Test
  public void shouldGenerateValidTokens() {
    UserData validTestData = new UserData("username", "id", "name");
    String token = jwtHelper.generateToken(validTestData);

    Assertions.assertNotNull(token);
    Assertions.assertTrue(jwtHelper.ensureTokenIsValid(token));

    Assertions.assertEquals("id", jwtHelper.getIdFromToken(token));
    Assertions.assertEquals("username", jwtHelper.getSubjectFromToken(token));
    Assertions.assertEquals("name", jwtHelper.getNameFromToken(token));
    Assertions.assertFalse(jwtHelper.isTokenExpired(token));
    Assertions.assertEquals("com.teddycrane.springpractice",
                            jwtHelper.getIssuerFromToken(token));
  }

  @Test
  public void shouldHandleInvalidTokens() {
    String badToken = "bad";

    Assertions.assertFalse(jwtHelper.ensureTokenIsValid(badToken));
  }
}
