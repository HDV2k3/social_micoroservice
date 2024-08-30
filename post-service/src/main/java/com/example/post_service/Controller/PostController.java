package com.example.post_service.Controller;
import com.example.post_service.Dto.ApiResponse;
import com.example.post_service.Dto.PageResponse;
import com.example.post_service.Dto.Request.CommentRequest;
import com.example.post_service.Dto.Request.PostRequest;
import com.example.post_service.Dto.Response.CommentResponse;
import com.example.post_service.Dto.Response.PostResponse;
import com.example.post_service.Dto.Response.ShareResponse;
import com.example.post_service.Dto.Response.UserProfileResponse;
import com.example.post_service.Facade.PostFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    PostFacade postFacade;
    // done
    @PostMapping(value = "/create-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResponse> createPost(
            @RequestPart PostRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        var result = postFacade.createPost(request, files);
        return ApiResponse.success(result);
    }

    // done
    @GetMapping("/my-posts")
    ApiResponse<PageResponse<PostResponse>> myPosts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ){
        var result = postFacade.getMyPosts(page, size);
        return ApiResponse.success(result);
    }
    //done function lay ra cac user da like bai viet
    @GetMapping("/{postId}/likes/users")
    public ApiResponse<PageResponse<UserProfileResponse>> getUsersWhoLikedPost(
            @PathVariable String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var result = postFacade.getUserProfilesWhoLikedPost(postId,page, size);
        return ApiResponse.success(result);
    }
    // done function lay ra cac user da comment post
    @GetMapping("/{postId}/comments/users")
    public ApiResponse<PageResponse<UserProfileResponse>> getUsersWhoCommentedOnPost(
            @PathVariable String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var result = postFacade.getUserProfilesWhoCommentedOnPost(postId,page, size);
        return ApiResponse.success(result);
    }
    // done
    @GetMapping("/{postId}/shares/users")
    public ApiResponse<PageResponse<UserProfileResponse>> getUsersWhoSharedPost(
            @PathVariable String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var result = postFacade.getUserProfilesWhoSharedPost(postId,page, size);
        return ApiResponse.success(result);
    }
    //done lay ra noi dung user da comment
    @GetMapping("/{postId}/comments")
    public ApiResponse<PageResponse<CommentResponse>> getAllCommentsForPost(
            @PathVariable String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var result = postFacade.getAllCommentsForPost(postId,page, size);
        return ApiResponse.success(result);
    }
    //done function like post
    @PostMapping("/{postId}/like")
    ApiResponse<String> likePost(@PathVariable String postId) {
        var result = postFacade.likePost(postId);
        return ApiResponse.success(result);
    }
    // done function comment post
    @PostMapping("/{postId}/comment")
    public ApiResponse<CommentResponse> commentOnPost(
            @PathVariable String postId,
            @RequestPart CommentRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        var result = postFacade.commentOnPost(postId,request,files);
        return ApiResponse.success(result);
    }

    // done
    @PostMapping("/{postId}/share")
    ApiResponse<ShareResponse> sharePost(@PathVariable String postId) {
        var result = postFacade.sharePost(postId);
        return ApiResponse.success(result);
    }
    // done
    @GetMapping("/{postId}/likes/count")
    ApiResponse<Long> getLikeCount(@PathVariable String postId) {
        var result = postFacade.getLikeCount(postId);
        return ApiResponse.success(result);
    }
    // done
    @GetMapping("/{postId}/comments/count")
    ApiResponse<Long> getCommentCount(@PathVariable String postId) {
        var result = postFacade.getCommentCount(postId);
        return ApiResponse.success(result);
    }
    // done
    @GetMapping("/{postId}/shares/count")
    ApiResponse<Long> getShareCount(@PathVariable String postId) {
        var result = postFacade.getShareCount(postId);
        return ApiResponse.success(result);
    }
}
