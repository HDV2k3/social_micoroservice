package com.example.identity_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.identity_service.entity.DeviceMetadata;

public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, String> {

    List<DeviceMetadata> findByUserId(String id);
}
