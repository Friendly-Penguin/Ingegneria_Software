package com.project.web.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtils {

    //Expiration time of the JWToken setted to 1hr
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    private final SecretKey key;

    // Constructor to initialize the secret key for signing the tokens
    public JWTUtils(){

        String secretString = "4f7587d335e5c37c82a3cfe688ec3a265c683de3e585e6d06fc50cde5d8e5be9910daa27c338c53568135c5bb521a366f678a626213e153f2a125b1cbbfac652af91066925ff6e623d523fc2c4958da5b27b11f07de5999dd9fed5";
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");

    }

    // Method to generate a JWT for a given user
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // Extracts the username from the JWT token
    public String extractUsername (String token){
        return extractClaims(token, Claims::getSubject);

    }

    // Generic method to extract claims from the token
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    // Checks if the token is valid by comparing the username and ensuring the token is not expired
    public boolean isValidToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Checks if the token has expired
    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}
