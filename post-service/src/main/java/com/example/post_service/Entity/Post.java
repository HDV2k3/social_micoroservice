package com.example.post_service.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.lang.annotation.Documented;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@Document(value = "post")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @MongoId
    String id;
    String userId;
    String content;
    Instant createDate;
    Instant modifiedDate;
    PostImage postImage;
    Long likeCount;   // Số lượng likes
    Long shareCount;  // Số lượng shares
    Long commentCount; // Số lượng comments
}
