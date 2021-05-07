package com.example.RedditCloneNew.repository;

import com.example.RedditCloneNew.model.Post;
import com.example.RedditCloneNew.model.User;
import com.example.RedditCloneNew.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
