package com.example.RedditCloneNew.repository;

import com.example.RedditCloneNew.model.Comment;
import com.example.RedditCloneNew.model.Post;
import com.example.RedditCloneNew.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
