package com.example.RedditCloneNew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsDto {
    private String id;
    private String postId;
    private Instant createdDate;
    private String text;
    private String userName;
}
