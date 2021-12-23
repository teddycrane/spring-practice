package com.teddycrane.springpractice.helper;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.teddycrane.springpractice.models.UserData;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHelper {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long expirationTime = 60 * 60 * 1000;
    private static final String issuer = "com.teddycrane.springpractice";
    private final Logger logger = LogManager.getLogger(JwtHelper.class);

    public boolean ensureTokenIsValid(String token)
    {
        logger.trace("ensureTokenIsValid called");
        Jws<Claims> jws;

        try 
        {
            jws = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token);
            logger.info("successfully parsed token {}", jws);
            return true;
        } catch(Exception e) 
        {
            logger.error("an exception occurred");
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) 
    {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * Generic utility function that gets a claim from a token.  
     * @param <T> The object type that represents the claim
     * @param token The token to get the claim from
     * @param claimsResolver A java.util.function that resolves the claim to the required type
     * @return The type <T> claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Date getExpiryDateFromToken(String token) 
    {
        return this.getClaimFromToken(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token)
    {
        Date expiryDate = this.getExpiryDateFromToken(token);
        return expiryDate.before(new Date(System.currentTimeMillis()));
    }

    public String generateToken(UserData data)
    {
        logger.trace("Generating token for user {}", data.getId());
        
        Date issuedAt = new Date(System.currentTimeMillis());
        Map<String, String> claims = new HashMap<>();
        claims.put("name", data.getName());

        String result = Jwts.builder()
                            .setClaims(claims)
                            .setIssuedAt(issuedAt)
                            .setExpiration(new Date(issuedAt.getTime() + expirationTime))
                            .setIssuer(issuer)
                            .setSubject(data.getUsername())
                            .setId(data.getId())
                            .signWith(key)
                            .compact();

        return result;
    }
}
