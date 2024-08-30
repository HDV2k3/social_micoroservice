package com.example.post_service.Service;

import com.example.post_service.Dto.PageResponse;
import com.example.post_service.Dto.Request.CommentRequest;
import com.example.post_service.Dto.Request.PostRequest;
import com.example.post_service.Dto.Response.CommentResponse;
import com.example.post_service.Dto.Response.PostResponse;
import com.example.post_service.Dto.Response.ShareResponse;
import com.example.post_service.Dto.Response.UserProfileResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
public interface PostService {

    PageResponse<UserProfileResponse> getUserProfilesWhoLikedPost(String postId, int page, int size);

    PageResponse<UserProfileResponse> getUserProfilesWhoCommentedOnPost(String postId, int page, int size);

    PageResponse<UserProfileResponse> getUserProfilesWhoSharedPost(String postId, int page, int size);

    PageResponse<CommentResponse> getAllCommentsForPost(String postId, int page, int size);

    PageResponse<PostResponse> getMyPosts(int page, int size);

    String likePost(String postId);

    CommentResponse commentOnPost(String postId, CommentRequest request, List<MultipartFile> files) throws IOException;

    PostResponse createPost(PostRequest request, List<MultipartFile> files) throws IOException;

    ShareResponse sharePost(String postId);

    long getLikeCount(String postId);

    long getCommentCount(String postId);

    long getShareCount(String postId);

}
