package com.example.identity_service.facade;

import com.example.identity_service.entity.NewLocationToken;
import com.example.identity_service.entity.User;

public interface IPCheckFacade {
    NewLocationToken isNewLoginLocation(String email, String ip);
    void addUserLocation(User user, String ip);
}