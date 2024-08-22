package com.example.profile_service.repository;

import com.example.profile_service.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends Neo4jRepository<UserProfile, String> {
    // Trong UserProfileRepository
    Page<UserProfile> findAllByUserId(String userId, Pageable pageable);
   Optional<UserProfile> findUserByUserId(String userId);
}
