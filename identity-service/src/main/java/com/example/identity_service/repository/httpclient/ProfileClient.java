package com.example.identity_service.repository.httpclient;

import com.example.identity_service.configuration.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.identity_service.dto.request.ProfileCreationRequest;

@FeignClient(
        name = "profile-service",
        url = "http://localhost:8081/profile",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    void createProfile(@RequestBody ProfileCreationRequest request);
}
