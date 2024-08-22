package com.example.post_service.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String postId;   // ID của bài viết được comment
    String userId;
    String firstName;
    String lastName;
    String avatar;
    String content;
    Instant createDate;
    Instant modifiedDate;
}
