package com.example.identity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.identity_service.entity.NewLocationToken;
import com.example.identity_service.entity.UserLocation;

public interface NewLocationTokenRepository extends JpaRepository<NewLocationToken, String> {

    NewLocationToken findByToken(String token);

    NewLocationToken findByUserLocation(UserLocation userLocation);
}
