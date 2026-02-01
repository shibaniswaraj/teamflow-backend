package com.teamflow.teamflow.security;

import com.teamflow.teamflow.model.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 🔑 Secret key used to sign the JWT
    // IMPORTANT: must be long enough for HS256
    private static final String SECRET =
            "teamflow-super-secret-key-teamflow-123456";

    // ⏳ Token validity (1 day)
    private static final long EXPIRATION_TIME =
            24 * 60 * 60 * 1000;

    // 🔐 Convert secret string into a cryptographic key
    private static final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * Generate JWT token
     * @param email user's email (acts as identity)
     * @param role user's role (ADMIN / MEMBER)
     */
    public String generateToken(String email, Role role) {

        return Jwts.builder()

                // Subject = primary identity (email)
                .setSubject(email)

                // Custom claim → role
                .claim("role", role.name())

                // Token creation time
                .setIssuedAt(new Date())

                // Token expiry time
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )

                // Sign token using secret key + algorithm
                .signWith(key, SignatureAlgorithm.HS256)

                // Build token into compact string
                .compact();
    }

    /**
     * Validate token & extract claims
     * Throws exception if token is invalid or expired
     */
    public Claims validateToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)   // same key used to sign
                .build()
                .parseClaimsJws(token)
                .getBody();           // return token data
    }
}
