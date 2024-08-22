package com.example.post_service.Repository;

import com.example.post_service.Entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findAllUserById(String userId, Pageable pageable);
    Page<Post> findAllByUserId(String userId, Pageable pageable);
}