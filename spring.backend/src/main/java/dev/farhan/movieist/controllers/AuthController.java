package dev.farhan.movieist.controllers;

import dev.farhan.movieist.models.User;
import dev.farhan.movieist.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        return new ResponseEntity<>(authService.registerUser(email, password), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        try {
            // Get the result map that contains token and alias
            Map<String, String> result = authService.loginUser(email, password);

            // Extract token and alias from the result map
            String token = result.get("token");
            String alias = result.get("userAlias");

            // Return the token and alias as a response
            return new ResponseEntity<>(Map.of("token", token, "userAlias", alias), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}