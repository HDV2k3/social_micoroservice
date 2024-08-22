package com.example.connect_service.Repository;

import com.example.connect_service.Entity.Connect;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectRepository extends MongoRepository<Connect,String> {
    Optional<Connect> findByFollowerIdAndFollowingId(String followerId, String followingId);

    List<Connect> findUserById(String followerId);
}
