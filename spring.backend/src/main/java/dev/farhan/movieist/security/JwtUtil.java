package dev.farhan.movieist.security;


import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JwtUtil {
    // Same key for signing and verifying JWT tokens
    private static final Key SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    public static Key getSecretKey() {
        return SECRET_KEY;
    }
}