package com.example.identity_service.facade;

import com.example.identity_service.entity.NewLocationToken;
import com.example.identity_service.entity.User;

public interface NewLocationTokenFacade {
    NewLocationToken createNewLocationToken(String country, User user);
}