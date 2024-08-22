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
@Document(value = "comments")
public class Comment {
    @MongoId
    String id;
    String postId;   // ID của bài viết được comment
    String userId;
    String content;
    Instant createDate;
    Instant modifiedDate;
}
