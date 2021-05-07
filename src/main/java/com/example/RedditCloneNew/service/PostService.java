package com.example.RedditCloneNew.service;

import com.example.RedditCloneNew.repository.*;
import com.example.RedditCloneNew.dto.PostRequest;
import com.example.RedditCloneNew.dto.PostResponse;
import com.example.RedditCloneNew.exceptions.PostNotFoundException;
import com.example.RedditCloneNew.exceptions.SubredditNotFoundException;
import com.example.RedditCloneNew.model.*;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.RedditCloneNew.model.VoteType.DOWNVOTE;
import static com.example.RedditCloneNew.model.VoteType.UPVOTE;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public void createPost(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findBySubredditName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        Post post = new Post();
        post.setPostName(postRequest.getPostName());
        post.setUrl(postRequest.getUrl());
        post.setDescription(postRequest.getDescription());
        post.setVoteCount(0);
        post.setUser(authService.getCurrentUser());
        post.setSubreddit(subreddit);
        post.setCreatedDate(Instant.now());
        postRepository.save(post);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::postToPostResponse)
                .collect(Collectors.toList());
    }

    public PostResponse getPost(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return postToPostResponse(post);
    }

    public List<PostResponse> getPostsBySubreddit(String subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId));
        return postRepository.findAllBySubreddit(subreddit)
                .stream()
                .map(this::postToPostResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(this::postToPostResponse)
                .collect(toList());
    }

    private PostResponse postToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .userName(post.getUser().getUserName())
                .subredditName(post.getSubreddit().getSubredditName())
                .voteCount(post.getVoteCount())
                .commentCount(commentRepository.findByPost(post).size())
                .duration(TimeAgo.using(post.getCreatedDate().toEpochMilli()))
                .upVote(checkVoteType(post, UPVOTE))
                .downVote(checkVoteType(post, DOWNVOTE))
                .build();
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser = voteRepository
                    .findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }
}
