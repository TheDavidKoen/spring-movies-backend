package dev.farhan.movieist.services;

import dev.farhan.movieist.models.User;
import dev.farhan.movieist.repositories.UserRepository;
import dev.farhan.movieist.security.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user
    public User registerUser(String email, String password) {
        // Check if the user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Encode the password
        String encodedPassword = passwordEncoder.encode(password);

        // Create a new User object
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        // Generate a unique alias (you can customize this as needed)
        user.setAlias(email.split("@")[0] + "_" + System.currentTimeMillis());

        // Save the user to the repository
        return userRepository.save(user);
    }

    // Login an existing user
    public Map<String, String> loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            // Generate JWT token
            String token = generateJwtToken(user.get());
            // Fetch the user's alias
            String alias = user.get().getAlias();

            // Return token and alias in a structured way
            return Map.of("token", token, "userAlias", alias);
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    // Generate JWT token for the logged-in user
    private String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())  // Ensure that the subject is a string (user ID)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("email", user.getEmail())
                .claim("alias", user.getAlias())
                .signWith(JwtUtil.getSecretKey(), SignatureAlgorithm.HS256) // Use shared secret key
                .compact();
    }
}