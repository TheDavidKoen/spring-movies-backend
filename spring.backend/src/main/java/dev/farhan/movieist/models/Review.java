package dev.farhan.movieist.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    private ObjectId id;
    private String reviewBody; // Changed from 'body' to 'reviewBody'
    private String alias;
    private LocalDateTime created;
    private LocalDateTime updated;

    public Review(String reviewBody, String alias, LocalDateTime created, LocalDateTime updated) {
        this.reviewBody = reviewBody;
        this.alias = alias;
        this.created = created;
        this.updated = updated;
    }
}