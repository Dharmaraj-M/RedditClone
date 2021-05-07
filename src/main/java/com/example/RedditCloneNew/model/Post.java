package com.example.RedditCloneNew.model;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Document(collection = "Posts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private String postId;
    @NotBlank
    private String postName;
    @Nullable
    private String url;
    @Nullable
    private String description;
    private Integer voteCount;
    @DBRef
    private User user;
    @DBRef
    private Subreddit subreddit;
    private Instant createdDate;
}
