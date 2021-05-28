package com.example.RedditCloneNew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private String postId;
    private String commentId;
    private String comment;
    private String userName;
    private String duration;
    private Integer upVoteCount;
    private Integer downVoteCount;
    private String isUpVoted;
    private String isDownVoted;
}
