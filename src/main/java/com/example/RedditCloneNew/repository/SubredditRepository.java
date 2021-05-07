package com.example.RedditCloneNew.repository;

import com.example.RedditCloneNew.model.Subreddit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubredditRepository extends MongoRepository<Subreddit, String> {
    Optional<Subreddit> findBySubredditName(String subredditName);
}
