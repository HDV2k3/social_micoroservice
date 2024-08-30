package com.example.identity_service.service.ServiceImpl;

import com.example.identity_service.entity.User;
import com.example.identity_service.repository.UserRepository;
import com.example.identity_service.service.VerificationTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VerificationTokenServiceImpl implements VerificationTokenService {
    UserRepository userRepository;
    @Override
    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user);
        return token;
    }

    @Override
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
