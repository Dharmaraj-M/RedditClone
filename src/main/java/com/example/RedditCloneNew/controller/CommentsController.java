package com.example.RedditCloneNew.controller;

import com.example.RedditCloneNew.dto.CommentResponse;
import com.example.RedditCloneNew.dto.CommentRequest;
import com.example.RedditCloneNew.dto.CommentVoteRequest;
import com.example.RedditCloneNew.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentRequest commentRequest) {
        commentService.createComment(commentRequest);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsForPost(@PathVariable String postId) {
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsForUser(@PathVariable String userName) {
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForUser(userName));
    }

    @PostMapping("/vote")
    public ResponseEntity<CommentResponse> vote(@RequestBody CommentVoteRequest commentVoteRequest) {
        return ResponseEntity.status(OK).body(commentService.vote(commentVoteRequest));
    }
}
