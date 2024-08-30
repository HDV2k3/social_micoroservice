package com.example.identity_service.facade;

import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.service.TokenService;
import com.nimbusds.jwt.SignedJWT;

public interface TokenFacade {
    TokenService.TokenInfo generateToken(User user) throws AppException;
    SignedJWT verifyToken(String token) throws AppException;
}
