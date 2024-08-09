package com.example.profile_service.repository;

import com.example.profile_service.entity.Introduction;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntroductionRepository extends Neo4jRepository<Introduction, String> {

}
