package com.example.identity_service.repository.httpclient;

import com.example.identity_service

.dto.request.ProfileCreationRequest;
import com.example.identity_service

.dto.response.UserProfileReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service",url = "http://localhost:8081/profile")
public interface ProflieClient {
    @PostMapping(value = "/users",produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileReponse createProfile(@RequestBody ProfileCreationRequest request);
}
