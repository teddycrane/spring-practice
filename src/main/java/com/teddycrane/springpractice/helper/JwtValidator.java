package com.teddycrane.springpractice.helper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import com.teddycrane.springpractice.user.User;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtValidator implements Serializable, IJwtValidator {
    private static final long serialVersionUID = -2550185165626007488L;
    private static final String issuer = "com.teddycrane.springpractice";

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret")
    private String secret;

    public String getUsernameFromToken(String token)
    {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationFromToken(String token)
    {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    public String getIdFromToken(String token)
    {
        return getClaimFromToken(token, Claims::getId);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

    private boolean isTokenExpired(String token)
    {
        final Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user)
    {
        Map<String, String> claims = new HashMap<>();
        claims.put("issuer", issuer);
        claims.put("name", user.getFirstName() + " " + user.getLastName());
        claims.put("id", user.getId().toString());

        return doGeneration(claims, user.getUsername());
    }

    private String doGeneration(Map<String, String> claims, String username)
    {
        return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
    }

    public boolean validateToken(String token, User user) throws IllegalArgumentException
    {
        final String username = getUsernameFromToken(token);
        final UUID userId = UUID.fromString(getIdFromToken(token));
        final String tokenIssuer = getClaimFromToken(token, Claims::getIssuer);

        return username.equals(user.getUsername()) 
        && userId.equals(user.getId()) 
        && tokenIssuer.equals(issuer)
        && !isTokenExpired(token);
    }

}

