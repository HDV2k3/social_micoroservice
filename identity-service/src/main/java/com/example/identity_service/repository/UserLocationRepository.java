package com.example.identity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.identity_service.entity.User;
import com.example.identity_service.entity.UserLocation;

public interface UserLocationRepository extends JpaRepository<UserLocation, String> {
    UserLocation findByCountryAndUser(String country, User user);
}
