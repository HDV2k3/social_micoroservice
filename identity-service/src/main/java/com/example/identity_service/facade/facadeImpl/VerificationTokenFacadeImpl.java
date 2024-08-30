package com.example.identity_service.facade.facadeImpl;

import com.example.identity_service.entity.User;
import com.example.identity_service.service.VerificationTokenService;
import com.example.identity_service.facade.VerificationTokenFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VerificationTokenFacadeImpl implements VerificationTokenFacade {

    VerificationTokenService verificationTokenService;

    @Override
    public String createVerificationToken(User user) {
        String token = verificationTokenService.createVerificationToken(user);
        log.info("Token created: {}", token);
        return token;
    }

    @Override
    public String validateVerificationToken(String token) {
        String result = verificationTokenService.validateVerificationToken(token);
        log.info("Token validation result: {}", result);
        return result;
    }
}