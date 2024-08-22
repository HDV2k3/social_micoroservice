package com.example.post_service.Dto.Response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShareResponse {
    String postId;
    String postContent;
    String postAuthorId;
    String postAuthorFirstName;
    String postAuthorLastName;
    Instant sharedAt;
}
