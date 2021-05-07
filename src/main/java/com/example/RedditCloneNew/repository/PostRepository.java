package com.example.RedditCloneNew.repository;

import com.example.RedditCloneNew.model.Post;
import com.example.RedditCloneNew.model.Subreddit;
import com.example.RedditCloneNew.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
