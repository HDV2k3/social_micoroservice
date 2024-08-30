package com.example.identity_service.facade.facadeImpl;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.request.LogoutRequest;
import com.example.identity_service.dto.request.RefreshRequest;
import com.example.identity_service.dto.response.AuthenticationResponse;
import com.example.identity_service.dto.response.IntrospectResponse;
import com.example.identity_service.facade.AuthenticationFacade;
import com.example.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    AuthenticationService authenticationService;

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        try {
            return authenticationService.introspect(request);
        } catch (Exception e) {
            // Handle or rethrow the exception as needed
            throw new RuntimeException("Error introspecting token", e);
        }
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            return authenticationService.authenticate(request);
        } catch (Exception e) {
            // Handle or rethrow the exception as needed
            throw new RuntimeException("Error authenticating user", e);
        }
    }

    @Override
    public String logout(LogoutRequest request) {
        try {
            authenticationService.logout(request);
        } catch (Exception e) {
            // Handle or rethrow the exception as needed
            throw new RuntimeException("Error logging out", e);
        }
        return "Logout Successfully";
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) {
        try {
            return authenticationService.refreshToken(request);
        } catch (Exception e) {
            // Handle or rethrow the exception as needed
            throw new RuntimeException("Error refreshing token", e);
        }
    }
}
