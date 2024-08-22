package com.example.post_service.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(value = "shares")
public class Share {
    @MongoId
    String id;
    String postId;   // ID của bài viết được share
    String userId;
    // Thông tin người đăng bài viết
    String postAuthorId;
    String postAuthorFirstName;
    String postAuthorLastName;
    String postContent;
    Instant sharedAt;
}
