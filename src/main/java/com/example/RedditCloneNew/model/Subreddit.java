package com.example.RedditCloneNew.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "Subreddits")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subreddit {
    @Id
    private String subredditId;
    @NotBlank
    private String subredditName;
    @NotBlank
    private String description;
    @DBRef
    private List<Post> posts;
    private Instant createdDate;
    @DBRef
    private User user;
}
