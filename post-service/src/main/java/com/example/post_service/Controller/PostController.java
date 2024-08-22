package com.example.post_service.Controller;

import com.example.post_service.Dto.ApiResponse;
import com.example.post_service.Dto.ApiResponseCode;
import com.example.post_service.Dto.PageResponse;
import com.example.post_service.Dto.Request.CommentRequest;
import com.example.post_service.Dto.Request.PostRequest;
import com.example.post_service.Dto.Response.CommentResponse;
import com.example.post_service.Dto.Response.PostResponse;
import com.example.post_service.Dto.Response.ShareResponse;
import com.example.post_service.Dto.Response.UserProfileResponse;
import com.example.post_service.Service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    PostService postService;
    // done
    @PostMapping(value = "/create-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResponse> createPost(
            @RequestPart PostRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        PostResponse postResponse = postService.createPost(request, files);
        return ApiResponse.of(ApiResponseCode.CREATED, postResponse);
    }

    // done
    @GetMapping("/my-posts")
    ApiResponse<PageResponse<PostResponse>> myPosts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ){
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.getMyPosts(page, size))
                .build();
    }
    //done function lay ra cac user da like bai viet
    @GetMapping("/{postId}/likes/users")
    public ResponseEntity<ApiResponse<PageResponse<UserProfileResponse>>> getUsersWhoLikedPost(
            @PathVariable String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        PageResponse<UserProfileResponse> response = postService.getUserProfilesWhoLikedPost(postId, page, size);
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, response));
    }
    // done function lay ra cac user da comment post
    @GetMapping("/{postId}/comments/users")
    public ResponseEntity<ApiResponse<PageResponse<UserProfileResponse>>> getUsersWhoCommentedOnPost(
            @PathVariable String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        PageResponse<UserProfileResponse> response = postService.getUserProfilesWhoCommentedOnPost(postId, page, size);
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, response));
    }
    // done
    @GetMapping("/{postId}/shares/users")
    public ResponseEntity<ApiResponse<PageResponse<UserProfileResponse>>> getUsersWhoSharedPost(
            @PathVariable String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        PageResponse<UserProfileResponse> response = postService.getUserProfilesWhoSharedPost(postId, page, size);
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, response));
    }
    //done lay ra noi dung user da comment
    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<PageResponse<CommentResponse>>> getAllCommentsForPost(
            @PathVariable String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        PageResponse<CommentResponse> response = postService.getAllCommentsForPost(postId, page, size);
        return ResponseEntity.ok(ApiResponse.of(ApiResponseCode.SUCCESS, response));
    }
    //done function like post
    @PostMapping("/{postId}/like")
    ApiResponse<Void> likePost(@PathVariable String postId) {
        postService.likePost(postId);
        return ApiResponse.of(ApiResponseCode.SUCCESS);
    }
    // done function comment post
    @PostMapping("/{postId}/comment")
    public ApiResponse<Void> commentOnPost(
            @PathVariable String postId,
            @RequestPart CommentRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        postService.commentOnPost(postId, request, files);
        return ApiResponse.of(ApiResponseCode.CREATED);
    }

    // done
    @PostMapping("/{postId}/share")
    ApiResponse<ShareResponse> sharePost(@PathVariable String postId) {
     return ApiResponse.<ShareResponse>builder()
             .result(postService.sharePost(postId))
             .build();
    }
    // done
    @GetMapping("/{postId}/likes/count")
    ApiResponse<Long> getLikeCount(@PathVariable String postId) {
        return ApiResponse.of(ApiResponseCode.SUCCESS, postService.getLikeCount(postId));
    }
    // done
    @GetMapping("/{postId}/comments/count")
    ApiResponse<Long> getCommentCount(@PathVariable String postId) {
        return ApiResponse.of(ApiResponseCode.SUCCESS, postService.getCommentCount(postId));
    }
    // done
    @GetMapping("/{postId}/shares/count")
    ApiResponse<Long> getShareCount(@PathVariable String postId) {
        return ApiResponse.of(ApiResponseCode.SUCCESS, postService.getShareCount(postId));
    }
}
