package com.example.RedditCloneNew.service;

import com.example.RedditCloneNew.repository.SubredditRepository;
import com.example.RedditCloneNew.dto.SubredditDto;
import com.example.RedditCloneNew.exceptions.SpringRedditException;
import com.example.RedditCloneNew.exceptions.SubredditNotFoundException;
import com.example.RedditCloneNew.model.Subreddit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SubredditService {

    private final SubredditRepository subredditRepository;

    public SubredditDto createSubreddit(SubredditDto subredditDto) {
        Subreddit subreddit = new Subreddit();
        subreddit.setSubredditName(subredditDto.getName());
        subreddit.setDescription(subredditDto.getDescription());
        subreddit.setCreatedDate(Instant.now());
        subredditRepository.save(subreddit);
        Subreddit subreddit1 = subredditRepository.findBySubredditName(subredditDto.getName())
                .orElseThrow(() -> new SubredditNotFoundException(subredditDto.getName()));
        return mapToDto(subreddit1);
    }

    public List<SubredditDto> getAllSubreddits() {
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        int size = subreddit.getPosts() == null ? 0 : subreddit.getPosts().size();
        return SubredditDto.builder()
                .id(subreddit.getSubredditId())
                .name(subreddit.getSubredditName())
                .description(subreddit.getDescription())
                .numberOfPosts(size)
                .build();
    }

    public SubredditDto getSubredditById(String subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId).orElseThrow(() -> new SpringRedditException("No Subreddit found."));
        int size = subreddit.getPosts() == null ? 0 : subreddit.getPosts().size();
        return SubredditDto.builder()
                .id(subreddit.getSubredditId())
                .name(subreddit.getSubredditName())
                .description(subreddit.getDescription())
                .numberOfPosts(size)
                .build();
    }
}
