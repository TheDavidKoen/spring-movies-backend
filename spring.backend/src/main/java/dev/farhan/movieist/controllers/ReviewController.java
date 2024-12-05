package dev.farhan.movieist.controllers;

import dev.farhan.movieist.models.Review;
import dev.farhan.movieist.services.ReviewService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private static final Key SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    @Autowired
    private ReviewService service;

    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, String> request) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String alias = claims.get("alias", String.class);
            String reviewBody = request.get("reviewBody");
            String imdbId = request.get("imdbId");

            Review review = service.createReview(reviewBody, imdbId, alias);
            return new ResponseEntity<>(review, HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access denied. Invalid token.");
        }
    }
}