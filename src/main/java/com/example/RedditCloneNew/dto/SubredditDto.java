package com.example.RedditCloneNew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubredditDto {
    private String id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
