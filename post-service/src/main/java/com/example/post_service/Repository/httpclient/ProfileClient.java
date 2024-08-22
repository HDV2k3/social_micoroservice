package com.example.post_service.Repository.httpclient;

import com.example.post_service.Configuration.AuthenticationRequestInterceptor;
import com.example.post_service.Dto.ApiResponse;
import com.example.post_service.Dto.PageResponse;
import com.example.post_service.Dto.Response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "profile-service",
        url = "http://localhost:8081/profile",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<PageResponse<UserProfileResponse>> getProfiles(
            @RequestParam List<String> userIds,
            @RequestParam int page,
            @RequestParam int size);
}
