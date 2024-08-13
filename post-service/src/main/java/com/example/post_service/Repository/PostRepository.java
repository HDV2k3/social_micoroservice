package com.example.post_service.Repository;

import com.example.post_service.Entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findUserById(String userId);
}