package com.example.identity_service.facade;

import com.example.identity_service.entity.User;

public interface VerificationTokenFacade {
    String createVerificationToken(User user);
    String validateVerificationToken(String token);
}