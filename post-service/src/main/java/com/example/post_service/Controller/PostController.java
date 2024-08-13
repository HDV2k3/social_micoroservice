package com.example.post_service.Controller;

import com.example.post_service.Dto.ApiResponse;
import com.example.post_service.Dto.Request.PostRequest;
import com.example.post_service.Dto.Response.PostResponse;
import com.example.post_service.Service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping("/create-post")
    ApiResponse<PostResponse> createPost(@RequestBody PostRequest request) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }

    @GetMapping("/my-post")
    ApiResponse<List<PostResponse>> getMyPosts() {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getMyPosts())
                .build();
    }
}
