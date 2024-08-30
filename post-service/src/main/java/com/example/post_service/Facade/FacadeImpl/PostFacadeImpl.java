package com.example.post_service.Facade.FacadeImpl;


import com.example.post_service.Dto.PageResponse;
import com.example.post_service.Dto.Request.CommentRequest;
import com.example.post_service.Dto.Request.PostRequest;
import com.example.post_service.Dto.Response.CommentResponse;
import com.example.post_service.Dto.Response.PostResponse;
import com.example.post_service.Dto.Response.ShareResponse;
import com.example.post_service.Dto.Response.UserProfileResponse;
import com.example.post_service.Facade.PostFacade;
import com.example.post_service.Service.FirebaseStorageService;

import com.example.post_service.Service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostFacadeImpl implements PostFacade {

    PostService postService;


    @Override
    public PageResponse<UserProfileResponse> getUserProfilesWhoLikedPost(String postId, int page, int size) {
        return postService.getUserProfilesWhoLikedPost(postId, page, size);
    }

    @Override
    public PageResponse<UserProfileResponse> getUserProfilesWhoCommentedOnPost(String postId, int page, int size) {
        return postService.getUserProfilesWhoCommentedOnPost(postId, page, size);
    }

    @Override
    public PageResponse<UserProfileResponse> getUserProfilesWhoSharedPost(String postId, int page, int size) {
        return postService.getUserProfilesWhoSharedPost(postId, page, size);
    }

    @Override
    public PageResponse<CommentResponse> getAllCommentsForPost(String postId, int page, int size) {
        return postService.getAllCommentsForPost(postId, page, size);
    }

    @Override
    public PageResponse<PostResponse> getMyPosts(int page, int size) {
        return postService.getMyPosts(page, size);
    }

    @Override
    public String likePost(String postId) {
        postService.likePost(postId);
        return "Successfully";
    }

    @Override
    public CommentResponse commentOnPost(String postId, CommentRequest request, List<MultipartFile> files) throws IOException {
        return postService.commentOnPost(postId, request, files);

    }

    @Override
    public PostResponse createPost(PostRequest request, List<MultipartFile> files) throws IOException {
        return postService.createPost(request, files);
    }

    @Override
    public ShareResponse sharePost(String postId) {
        return postService.sharePost(postId);
    }

    @Override
    public long getLikeCount(String postId) {
        return postService.getLikeCount(postId);
    }

    @Override
    public long getCommentCount(String postId) {
        return postService.getCommentCount(postId);
    }

    @Override
    public long getShareCount(String postId) {
        return postService.getShareCount(postId);
    }
}
