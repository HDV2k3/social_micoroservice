package com.example.identity_service.service;

import com.example.identity_service.entity.NewLocationToken;
import com.example.identity_service.entity.User;

public interface NewLocationTokenService {
    NewLocationToken createNewLocationToken(String country, User user);
}
