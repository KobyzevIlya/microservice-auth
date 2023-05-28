package ru.hse.hw4.auth.security;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class Jwt {
    private Key key;

    public Jwt() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String createJwt(String email, Long userId) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(30));

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
