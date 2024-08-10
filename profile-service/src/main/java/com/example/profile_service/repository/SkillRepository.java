package com.example.profile_service.repository;

import com.example.profile_service.entity.Skill;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends Neo4jRepository<Skill,String> {
}
