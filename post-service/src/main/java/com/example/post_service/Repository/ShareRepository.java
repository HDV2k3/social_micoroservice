package com.example.post_service.Repository;

import com.example.post_service.Entity.Share;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareRepository extends MongoRepository<Share, String> {
    long countByPostId(String postId);// Đếm số lượng shares theo postId
    List<Share> findByPostId(String postId);
}
