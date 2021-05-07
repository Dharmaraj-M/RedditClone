package com.example.RedditCloneNew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RedditCloneNewApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedditCloneNewApplication.class, args);
    }

}
