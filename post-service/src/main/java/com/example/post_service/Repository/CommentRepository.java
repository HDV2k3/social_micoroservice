package com.example.post_service.Repository;

import com.example.post_service.Entity.Comment;
import com.example.post_service.Entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    long countByPostId(String postId);  // Đếm số lượng comments theo postId
    List<Comment> findByPostId(String postId);
    Page<Comment> findByPostId(String postId,Pageable pageable);
}
