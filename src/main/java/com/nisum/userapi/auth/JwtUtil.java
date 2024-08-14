package com.nisum.userapi.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class JwtUtil {

    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLES = "roles";

    private final JwtUtilParameters jwtUtilParameters;
    private final JwtParser jwtParser;

    public String createToken(UserDetails userDetails) {
        Claims claims = Jwts.claims()
                .subject(userDetails.getUsername())
                .add(KEY_EMAIL, userDetails.getUsername())
                .add(KEY_ROLES, userDetails.getAuthorities())
                .build();

        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(jwtUtilParameters.getExpiration()));

        return Jwts.builder()
                .claims(claims)
                .expiration(tokenValidity)
                .signWith(Keys.hmacShaKeyFor(jwtUtilParameters.getSecretKey().getBytes()))
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public Date getExpiration(String token) {
        return parseJwtClaims(token).getExpiration();
    }
}
