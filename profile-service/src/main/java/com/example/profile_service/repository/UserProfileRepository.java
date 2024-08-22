package com.example.profile_service.repository;

import com.example.profile_service.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends Neo4jRepository<UserProfile, String> {
    // Trong UserProfileRepository
    Page<UserProfile> findByUserIdIn(List<String> userIds, Pageable pageable);
    Page<UserProfile> findAllByUserId(String userId, Pageable pageable);
}
