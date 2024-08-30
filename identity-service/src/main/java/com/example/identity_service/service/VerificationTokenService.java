package com.example.identity_service.service;


import com.example.identity_service.entity.User;

public interface VerificationTokenService {

    String createVerificationToken(User user);

    String validateVerificationToken(String token);
}
