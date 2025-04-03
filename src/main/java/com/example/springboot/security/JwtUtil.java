package com.example.springboot.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
    private SecretKey key;

    // ✅ Inject secret key from application.properties
    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    // ✅ Validate the key after dependency injection
    @PostConstruct
    public void validateKey() {
        if (key == null) {
            throw new IllegalStateException("JWT secret key is not initialized!");
        }
    }

    // ✅ Generate JWT Token with Role
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // Include user role in JWT

        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // ✅ Extract Username (Email) from JWT
    public Optional<String> extractUsername(String token) {
        return Optional.ofNullable(parseClaims(token).getSubject());
    }

    // ✅ Extract User Role from JWT
    public Optional<String> extractRole(String token) {
        Claims claims = parseClaims(token);
        return Optional.ofNullable(claims.get("role", String.class));
    }

    // ✅ Validate JWT Token
    public boolean validateToken(String token, String username) {
        Claims claims = parseClaims(token);
        return claims.getSubject().equals(username) && !isTokenExpired(claims);
    }

    // ✅ Check Token Expiration
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    // ✅ Parse JWT Claims
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
