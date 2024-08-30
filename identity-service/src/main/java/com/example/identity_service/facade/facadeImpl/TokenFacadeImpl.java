package com.example.identity_service.facade.facadeImpl;

import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.facade.TokenFacade;
import com.example.identity_service.service.TokenService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TokenFacadeImpl implements TokenFacade {

    TokenService tokenService;

    @Override
    public TokenService.TokenInfo generateToken(User user) throws AppException {
        try {
            return tokenService.generateToken(user);
        } catch (AppException e) {
            log.error("Error generating token", e);
            throw e;
        }
    }

    @Override
    public SignedJWT verifyToken(String token) throws AppException {
        try {
            return tokenService.verifyToken(token);
        } catch (AppException e) {
            log.error("Error verifying token", e);
            throw e;
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
