package com.koyeb.rayshan.utils;

import com.koyeb.rayshan.configs.AppConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.security.PasswordSpec;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private final String secretKey;

    private final AppConfig appConfig;

    public JwtUtil(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.secretKey = appConfig.getJwtSecret();
    }
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Set expiration for 10 hours
                //.signWith(SignatureAlgorithm.HS256, secretKey)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Set expiration for 10 hours
                //.signWith(SignatureAlgorithm.HS256, secretKey)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (username.equals(extractedUsername) && !isTokenExpired(token));
    }

    // Extract Username
    public String extractUsername(String token) {
        SecretKey key = new PasswordSpec(secretKey.toCharArray());
        return getAllClaimsFromToken(token).getSubject();
    }

    // Check if Token Expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract Expiration Date
    private Date extractExpiration(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Mono<Claims> getAllClaimsFromTokenMono(String token) {
        return Mono.fromCallable(() ->
                Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
        );
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
