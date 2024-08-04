package com.example.identity_service.repository;

import com.example.identity_service.entity.DeviceMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, String> {

    List<DeviceMetadata> findByUserId(String id);
}
