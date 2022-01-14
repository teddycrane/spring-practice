package com.teddycrane.springpractice.helper;

import com.teddycrane.springpractice.models.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtHelper implements IJwtHelper {
  // private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
  private static final long expirationTime = 60 * 60 * 1000;
  private static final String issuer = "com.teddycrane.springpractice";
  private final Logger logger = LogManager.getLogger(JwtHelper.class);
  private final Key key;

  public JwtHelper(@Value("${secret.key}") String keyString) {
    key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(keyString));
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  /**
   * Generic utility function that gets a claim from a token.
   *
   * @param <T> The object type that represents the claim
   * @param token The token to get the claim from
   * @param claimsResolver A java.util.function that resolves the claim to the required type
   * @return The type <T> claim
   */
  private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private <T> T getClaimFromToken(String token, String claim) throws NoSuchElementException {
    final Claims claims = getAllClaimsFromToken(token);
    if (claims.containsKey(claim)) {
      return (T) claims.get(claim);
    } else {
      logger.error("invalid claim provided");
      throw new NoSuchElementException(String.format("No claim with the name %s", claim));
    }
  }

  /**
   * Ensures that an auth token is valid
   *
   * @param token The auth token to validate
   * @return True if the token is valid and issued by this instance of the application, and
   *     otherwise false.
   */
  public boolean ensureTokenIsValid(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      logger.error("The token provided is not an acceptable token");
      return false;
    }
  }

  public Date getExpiryDateFromToken(String token) {
    return this.getClaimFromToken(token, Claims::getExpiration);
  }

  public String getIdFromToken(String token) {
    return this.getClaimFromToken(token, Claims::getId);
  }

  public String getNameFromToken(String token) {
    return this.getClaimFromToken(token, "name");
  }

  public String getIssuerFromToken(String token) {
    return this.getClaimFromToken(token, Claims::getIssuer);
  }

  public boolean isTokenExpired(String token) {
    Date expiryDate = this.getExpiryDateFromToken(token);
    return expiryDate.before(new Date(System.currentTimeMillis()));
  }

  public String getSubjectFromToken(String token) {
    return this.getClaimFromToken(token, Claims::getSubject);
  }

  public String generateToken(UserData data) {
    logger.trace("Generating token for user {}", data.getId());

    Date issuedAt = new Date(System.currentTimeMillis());
    Map<String, String> claims = new HashMap<>();
    claims.put("name", data.getName());

    String result =
        Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(issuedAt)
            .setExpiration(new Date(issuedAt.getTime() + expirationTime))
            .setIssuer(issuer)
            .setSubject(data.getUsername())
            .setId(data.getId())
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();

    return result;
  }
}
