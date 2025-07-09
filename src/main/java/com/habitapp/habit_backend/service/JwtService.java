package com.habitapp.habit_backend.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.habitapp.habit_backend.model.User;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  private long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24h

  public String generateToken(User user) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

    return Jwts.builder()
        .setSubject(user.getMail())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractMail(String token) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
