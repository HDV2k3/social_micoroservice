package com.example.identity_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.identity_service.entity.User;
import com.example.identity_service.repository.UserRepository;

import java.util.UUID;

@Service
public class VerificationTokenService {

    @Autowired
    private UserRepository userRepository;

    public String createVerificationToken(User user) {
        String  token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user);
        return token;
    }

    public String validateVerificationToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            return "invalid";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }
}
