package com.example.RedditCloneNew.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "Vote")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    @Id
    private String voteId;
    private VoteType voteType;
    @NotNull
    @DBRef
    private Post post;
    @DBRef
    private User user;
}
