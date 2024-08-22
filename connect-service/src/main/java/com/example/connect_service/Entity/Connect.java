package com.example.connect_service.Entity;

import com.example.connect_service.Entity.Enum.ConnectStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Getter
@Setter
@Builder
@Document(value = "connect")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Connect {
    @MongoId
    String id;

    // ID of the user who is following
    String followerId;

    // ID of the user who is being followed
    String followingId;
    String firstName;
    String lastName;
    String avatar;
    // Timestamp when the follow request was made
    Instant followedAt;

    // Status of the follow request (e.g., PENDING, ACCEPTED, DECLINED)
    ConnectStatus status;

    // Optional: Timestamp when the follow request was accepted
    Instant acceptedAt;
}
//UserA follow UserB: Khi UserA gửi yêu cầu follow,
// một document mới sẽ được tạo trong collection connect với status là PENDING và followedAt là thời điểm yêu cầu được gửi.
//
//UserB nhận thông báo:
// UserB sẽ nhận được thông báo về yêu cầu follow và có thể chấp nhận hoặc từ chối yêu cầu.
//
//UserB chấp nhận hoặc từ chối:
// Nếu UserB chấp nhận yêu cầu, trạng thái status sẽ được cập nhật thành ACCEPTED và acceptedAt sẽ được ghi lại.
// Nếu UserB từ chối, status sẽ được cập nhật thành DECLINED.