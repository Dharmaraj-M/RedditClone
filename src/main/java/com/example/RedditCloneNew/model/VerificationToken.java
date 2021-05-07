package com.example.RedditCloneNew.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "Validation Token")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {
    @Id
    private String tokenId;
    private String token;
    @DBRef
    private User user;
    private Instant expiryDate;
}
