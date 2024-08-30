package com.example.identity_service.service;

import com.example.identity_service.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface TokenService {

    TokenInfo generateToken(User user);

    SignedJWT verifyToken(String token) throws JOSEException, ParseException;

    record TokenInfo(String token, java.util.Date expiryDate) {}
}
