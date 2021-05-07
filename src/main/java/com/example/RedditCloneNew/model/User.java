package com.example.RedditCloneNew.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
@Document(collection = "Users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String userId;
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    @NotEmpty
    @Email
    private String email;
    private Instant createdDate;
    private boolean enabled;
}
