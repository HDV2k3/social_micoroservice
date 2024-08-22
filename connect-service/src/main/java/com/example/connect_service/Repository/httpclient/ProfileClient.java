package com.example.connect_service.Repository.httpclient;

import com.example.connect_service.Configuration.AuthenticationRequestInterceptor;
import com.example.connect_service.Dto.Response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "profile-service",
        url = "http://localhost:8081/profile",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {

    @GetMapping("/users/userId/{userId}")
    UserProfileResponse getProfile(@PathVariable("userId") String userId);
}
