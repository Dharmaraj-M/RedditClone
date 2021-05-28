package com.example.RedditCloneNew.dto;

import com.example.RedditCloneNew.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentVoteRequest {
    private String commentId;
    private VoteType voteType;
}
