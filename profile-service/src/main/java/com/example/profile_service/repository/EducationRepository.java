package com.example.profile_service.repository;

import com.example.profile_service.entity.Education;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends Neo4jRepository<Education, String> {

}
