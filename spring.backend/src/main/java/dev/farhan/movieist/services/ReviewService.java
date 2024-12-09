package dev.farhan.movieist.services;

import dev.farhan.movieist.models.Movie;
import dev.farhan.movieist.models.Review;
import dev.farhan.movieist.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId, String alias) {
        Review review = new Review(reviewBody, alias, LocalDateTime.now(), LocalDateTime.now());
        review = repository.insert(review);

        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review.getId()))
                .first();

        return review;
    }

    public List<Review> getReviewsForMovie(String imdbId) {
        Movie movie = mongoTemplate.findOne(
                Query.query(Criteria.where("imdbId").is(imdbId)),
                Movie.class
        );
        if (movie != null && movie.getReviewIds() != null) {
            // Convert List<String> to List<ObjectId>
            List<ObjectId> reviewObjectIds = movie.getReviewIds()
                    .stream()
                    .map(ObjectId::new)
                    .toList();
            return repository.findAllById(reviewObjectIds);
        }
        return Collections.emptyList();
    }
}