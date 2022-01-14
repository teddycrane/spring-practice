package com.teddycrane.springpractice.helper;

import com.teddycrane.springpractice.models.UserData;
import java.util.Date;

public interface IJwtHelper {
  boolean ensureTokenIsValid(String token);

  Date getExpiryDateFromToken(String token);

  String getIdFromToken(String token);

  String getNameFromToken(String token);

  String getIssuerFromToken(String token);

  boolean isTokenExpired(String token);

  String getSubjectFromToken(String token);

  String generateToken(UserData data);
}
