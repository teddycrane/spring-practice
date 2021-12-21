package com.teddycrane.springpractice.helper;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import com.teddycrane.springpractice.user.User;

import io.jsonwebtoken.Claims;

public interface IJwtValidator {
    String getUsernameFromToken(String token);

    Date getExpirationFromToken(String token);

    String getIdFromToken(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    String generateToken(User user);

    boolean validateToken(String token, User user) throws IllegalArgumentException;
}
