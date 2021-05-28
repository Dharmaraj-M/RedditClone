package com.example.RedditCloneNew.service;

import com.example.RedditCloneNew.dto.CommentRequest;
import com.example.RedditCloneNew.dto.CommentResponse;
import com.example.RedditCloneNew.dto.CommentVoteRequest;
import com.example.RedditCloneNew.exceptions.PostNotFoundException;
import com.example.RedditCloneNew.exceptions.SpringRedditException;
import com.example.RedditCloneNew.model.Comment;
import com.example.RedditCloneNew.model.CommentVote;
import com.example.RedditCloneNew.model.Post;
import com.example.RedditCloneNew.model.User;
import com.example.RedditCloneNew.repository.CommentRepository;
import com.example.RedditCloneNew.repository.PostRepository;
import com.example.RedditCloneNew.repository.UserRepository;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.example.RedditCloneNew.model.VoteType.DOWNVOTE;
import static com.example.RedditCloneNew.model.VoteType.UPVOTE;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;

    public void createComment(CommentRequest commentRequest) {
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentRequest.getPostId()));
        Comment comment = commentRequestToComment(commentRequest, post, authService.getCurrentUser());
        commentRepository.save(comment);
    }

    public List<CommentResponse> getAllCommentsForPost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        return commentRepository.findByPost(post)
                .stream()
                .map(this::commentToCommentResponse)
                .collect(toList());
    }

    public List<CommentResponse> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(this::commentToCommentResponse)
                .collect(toList());
    }

    private Comment commentRequestToComment(CommentRequest commentRequest, Post post, User user) {
        return Comment.builder()
                .comment(commentRequest.getComment())
                .post(post)
                .createdDate(Instant.now())
                .user(user)
                .commentVotes(new ArrayList<>())
                .upvoteCount(0)
                .downvoteCount(0)
                .build();
    }

    private CommentResponse commentToCommentResponse(Comment comment) {

        return CommentResponse.builder()
                .postId(comment.getPost().getPostId())
                .commentId(comment.getCommentId())
                .comment(comment.getComment())
                .userName(comment.getUser().getUserName())
                .duration(TimeAgo.using(comment.getCreatedDate().toEpochMilli()))
                .upVoteCount(comment.getUpvoteCount())
                .downVoteCount(Math.abs(comment.getDownvoteCount()))
                .isUpVoted(voteCheck(comment))
                .isDownVoted(voteCheck(comment))
                .build();
    }

    public CommentResponse vote(CommentVoteRequest commentVoteRequest) {
        Comment comment = commentRepository.findById(commentVoteRequest.getCommentId())
                .orElseThrow(() -> new SpringRedditException("Comment not found." + commentVoteRequest.getCommentId()));
        List<CommentVote> commentVotes = comment.getCommentVotes();
        String currentUserId = authService.getCurrentUser().getUserId();

        for (CommentVote commentVote : commentVotes) {
            if (currentUserId.equals(commentVote.getUserId())) {
                if (commentVote.getVoteType().equals(commentVoteRequest.getVoteType())) {
                    throw new SpringRedditException("already VOTED");
                }
                if (commentVoteRequest.getVoteType().equals(DOWNVOTE) && commentVote.getVoteType().equals(UPVOTE)) {
                    commentVote.setVoteType(DOWNVOTE);
                    comment.setDownvoteCount(comment.getDownvoteCount() + 1);
                    comment.setUpvoteCount(comment.getUpvoteCount() - 1);
                    System.out.println("DOWNVOTED");
                }
                if (commentVoteRequest.getVoteType().equals(UPVOTE) && commentVote.getVoteType().equals(DOWNVOTE)) {
                    commentVote.setVoteType(UPVOTE);
                    comment.setUpvoteCount(comment.getUpvoteCount() + 1);
                    comment.setDownvoteCount(comment.getDownvoteCount() - 1);
                    System.out.println("UPVOTED");
                }
                commentRepository.save(comment);
                return commentToCommentResponse(comment);
            }
        }
        CommentVote commentVote = CommentVote.builder()
                .userId(authService.getCurrentUser().getUserId())
                .voteType(commentVoteRequest.getVoteType())
                .build();
        commentVotes.add(commentVote);
        if (commentVoteRequest.getVoteType().equals(UPVOTE)) {
            comment.setUpvoteCount(comment.getUpvoteCount() + 1);
        } else if (commentVoteRequest.getVoteType().equals(DOWNVOTE)) {
            comment.setDownvoteCount(comment.getDownvoteCount() - 1);
        }
        commentRepository.save(comment);
        return commentToCommentResponse(comment);
    }

    private String voteCheck(Comment comment) {
        List<CommentVote> commentVotes = comment.getCommentVotes();
        for (CommentVote commentVote : commentVotes) {
            if (authService.getCurrentUser().getUserId().equals(commentVote.getUserId())) {
                if (commentVote.getVoteType().equals(UPVOTE)) {
                    return "UPVOTE";
                } else if (commentVote.getVoteType().equals(DOWNVOTE)) {
                    return "DOWNVOTE";
                }
            }
        }
        return "null";
    }
}
