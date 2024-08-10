package com.example.profile_service.repository;

import com.example.profile_service.entity.Project;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends Neo4jRepository<Project,String> {
}
