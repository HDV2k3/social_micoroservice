package com.example.post_service.Repository;

import com.example.post_service.Entity.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends MongoRepository<Like, String> {
    // Truy vấn để tìm một Like cụ thể dựa trên postId và userId
    Optional<Like> findByPostIdAndUserId(String postId, String userId);
    long countByPostId(String postId);  // Đếm số lượng likes theo postId
    List<Like> findByPostId(String postId);
}
