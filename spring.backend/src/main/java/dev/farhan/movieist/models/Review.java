package dev.farhan.movieist.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reviews")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Review {
    private ObjectId id;
    private String body;
    private String alias; // Add alias field
    private LocalDateTime created;
    private LocalDateTime updated;

    public Review(String body, String alias, LocalDateTime created, LocalDateTime updated) {
        this.body = body;
        this.alias = alias; // Set alias
        this.created = created;
        this.updated = updated;
    }
}