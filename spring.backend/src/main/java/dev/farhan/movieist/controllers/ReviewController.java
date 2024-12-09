package dev.farhan.movieist.controllers;

import dev.farhan.movieist.models.Review;
import dev.farhan.movieist.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Map<String, String> request) {
        try {
            // Get alias from SecurityContext
            String alias = SecurityContextHolder.getContext().getAuthentication().getName();
            if (alias == null || alias.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access denied. Invalid token.");
            }

            String reviewBody = request.get("reviewBody");
            String imdbId = request.get("imdbId");

            Review review = service.createReview(reviewBody, imdbId, alias);
            return new ResponseEntity<>(review, HttpStatus.CREATED);

        } catch (Exception e) {
            System.err.println("Error creating review: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @GetMapping("/{imdbId}")
    public ResponseEntity<?> getReviews(@PathVariable String imdbId) {
        try {
            List<Review> reviews = service.getReviewsForMovie(imdbId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch reviews.");
        }
    }

}