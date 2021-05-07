package com.example.RedditCloneNew.service;

import com.example.RedditCloneNew.repository.CommentRepository;
import com.example.RedditCloneNew.repository.PostRepository;
import com.example.RedditCloneNew.repository.UserRepository;
import com.example.RedditCloneNew.dto.CommentsDto;
import com.example.RedditCloneNew.exceptions.PostNotFoundException;
import com.example.RedditCloneNew.model.Comment;
import com.example.RedditCloneNew.model.NotificationEmail;
import com.example.RedditCloneNew.model.Post;
import com.example.RedditCloneNew.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void createComment(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId()));
        Comment comment = commentsDtoToComments(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(authService.getCurrentUser() + " posted a comment on your post.");
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUserName() + " Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        return commentRepository.findByPost(post)
                .stream()
                .map(this::commentToCommentsDto)
                .collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(this::commentToCommentsDto)
                .collect(toList());
    }

    private Comment commentsDtoToComments(CommentsDto commentsDto, Post post, User user) {
        return Comment.builder()
                .comment(commentsDto.getText())
                .post(post)
                .createdDate(Instant.now())
                .user(user)
                .build();
    }

    private CommentsDto commentToCommentsDto(Comment comment) {
        return CommentsDto.builder()
                .id(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .createdDate(comment.getCreatedDate())
                .text(comment.getComment())
                .userName(comment.getUser().getUserName())
                .build();
    }
}
