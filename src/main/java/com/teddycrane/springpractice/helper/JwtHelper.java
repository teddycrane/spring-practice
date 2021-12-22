package com.teddycrane.springpractice.helper;

import java.security.Key;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import com.teddycrane.springpractice.models.UserData;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHelper {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long expirationTime = 60 * 60 * 1000;
    private static final String issuer = "com.teddycrane.springpractice";
    private final Logger logger = LogManager.getLogger(JwtHelper.class);

    public String generateToken(UserData data)
    {
        logger.trace("Generating token for user {}", data.getId());
        Date issuedAt = new Date(System.currentTimeMillis());
        Map<String, String> claims = new HashMap<>();
        claims.put("name", data.getName());

        String result = Jwts.builder()
                            .setSubject(data.getUsername())
                            .setId(data.getId())
                            .setIssuer(issuer)
                            .setClaims(claims)
                            .setIssuedAt(issuedAt)
                            .setExpiration(new Date(issuedAt.getTime() + expirationTime))
                            .signWith(key)
                            .compact();

        return result;
    }
}
