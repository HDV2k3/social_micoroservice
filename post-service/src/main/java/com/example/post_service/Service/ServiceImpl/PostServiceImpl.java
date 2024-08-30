package com.example.post_service.Service.ServiceImpl;
import com.example.post_service.Dto.ApiResponse;
import com.example.post_service.Dto.PageResponse;
import com.example.post_service.Dto.Request.CommentRequest;
import com.example.post_service.Dto.Request.PostRequest;
import com.example.post_service.Dto.Response.CommentResponse;
import com.example.post_service.Dto.Response.PostResponse;
import com.example.post_service.Dto.Response.ShareResponse;
import com.example.post_service.Dto.Response.UserProfileResponse;
import com.example.post_service.Entity.*;
import com.example.post_service.Exception.AppException;
import com.example.post_service.Exception.ErrorCode;
import com.example.post_service.Mapper.CommentMapper;
import com.example.post_service.Mapper.PostMapper;
import com.example.post_service.Mapper.ShareMapper;
import com.example.post_service.Repository.CommentRepository;
import com.example.post_service.Repository.LikeRepository;
import com.example.post_service.Repository.PostRepository;
import com.example.post_service.Repository.ShareRepository;
import com.example.post_service.Repository.httpclient.ProfileClient;
import com.example.post_service.Service.FirebaseStorageService;
import com.example.post_service.Service.PostService;
import com.example.post_service.Utils.JwtUtils;
import com.example.post_service.contants.URL_BUCKET_NAME;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    PostMapper postMapper;
    LikeRepository likeRepository;
    CommentRepository commentRepository;
    ShareRepository shareRepository;
    ProfileClient profileClient;
    ShareMapper shareMapper;
    CommentMapper commentMapper;
    FirebaseStorageService firebaseStorageService;

    @Override
    public PageResponse<UserProfileResponse> getUserProfilesWhoLikedPost(String postId, int page, int size) {
        List<String> userIds = likeRepository.findByPostId(postId)
                .stream()
                .map(Like::getUserId)
                .collect(Collectors.toList());

        if (userIds.isEmpty()) {
            return PageResponse.<UserProfileResponse>builder()
                    .currentPage(page)
                    .pageSize(size)
                    .totalPages(0)
                    .totalElements(0)
                    .data(Collections.emptyList())
                    .build();
        }

        ApiResponse<PageResponse<UserProfileResponse>> response = profileClient.getProfiles(userIds, page, size);
        return response.getResult();
    }

    @Override
    public PageResponse<UserProfileResponse> getUserProfilesWhoCommentedOnPost(String postId, int page, int size) {
        List<String> userIds = commentRepository.findByPostId(postId)
                .stream()
                .map(Comment::getUserId)
                .distinct()
                .collect(Collectors.toList());

        System.out.println("User IDs: " + userIds);

        ApiResponse<PageResponse<UserProfileResponse>> response = profileClient.getProfiles(userIds, page, size);
        System.out.println("Profile Response: " + response);

        return response.getResult();
    }

    @Override
    public PageResponse<UserProfileResponse> getUserProfilesWhoSharedPost(String postId, int page, int size) {
        List<String> userIds = shareRepository.findByPostId(postId)
                .stream()
                .map(Share::getUserId)
                .collect(Collectors.toList());

        ApiResponse<PageResponse<UserProfileResponse>> response = profileClient.getProfiles(userIds, page, size);
        return response.getResult();
    }

    @Override
    public PageResponse<CommentResponse> getAllCommentsForPost(String postId, int page, int size) {
        Page<Comment> commentsPage = commentRepository.findByPostId(postId, PageRequest.of(page - 1, size));

        List<String> userIds = commentsPage.getContent().stream()
                .map(Comment::getUserId)
                .distinct()
                .collect(Collectors.toList());

        if (userIds.isEmpty()) {
            return PageResponse.<CommentResponse>builder()
                    .currentPage(commentsPage.getNumber() + 1)
                    .pageSize(commentsPage.getSize())
                    .totalPages(commentsPage.getTotalPages())
                    .totalElements(commentsPage.getTotalElements())
                    .data(Collections.emptyList())
                    .build();
        }

        ApiResponse<PageResponse<UserProfileResponse>> profileResponse = profileClient.getProfiles(userIds, page, size);
        Map<String, UserProfileResponse> userProfileMap = profileResponse.getResult().getData().stream()
                .collect(Collectors.toMap(UserProfileResponse::getUserId, userProfile -> userProfile));

        List<CommentResponse> commentResponses = commentsPage.getContent().stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .postId(comment.getPostId())
                        .userId(comment.getUserId())
                        .firstName(Optional.ofNullable(userProfileMap.get(comment.getUserId()))
                                .map(UserProfileResponse::getFirstName)
                                .orElse("Unknown"))
                        .lastName(Optional.ofNullable(userProfileMap.get(comment.getUserId()))
                                .map(UserProfileResponse::getLastName)
                                .orElse("Unknown"))
                        .avatar(Optional.ofNullable(userProfileMap.get(comment.getUserId()))
                                .map(UserProfileResponse::getAvatar)
                                .orElse("Unknown"))
                        .content(comment.getContent())
                        .createDate(comment.getCreateDate())
                        .modifiedDate(comment.getModifiedDate())
                        .build())
                .collect(Collectors.toList());

        return PageResponse.<CommentResponse>builder()
                .currentPage(commentsPage.getNumber() + 1)
                .pageSize(commentsPage.getSize())
                .totalPages(commentsPage.getTotalPages())
                .totalElements(commentsPage.getTotalElements())
                .data(commentResponses)
                .build();
    }

    @Override
    public PageResponse<PostResponse> getMyPosts(int page, int size) {
        String userId = JwtUtils.getCurrentUserId();
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = postRepository.findAllByUserId(userId, pageable);

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(postMapper::toPostResponse).toList())
                .build();
    }

    @Override
    public String likePost(String postId) {
        String userId = JwtUtils.getCurrentUserId();
        if (likeRepository.findByPostIdAndUserId(postId, userId).isEmpty()) {
            Like like = Like.builder()
                    .postId(postId)
                    .userId(userId)
                    .build();
            likeRepository.save(like);
        } else {
            throw new AppException(ErrorCode.INVALID_KEY);
        }
        return "Successfully";
    }

    @Override
    public CommentResponse commentOnPost(String postId, CommentRequest request, List<MultipartFile> files) throws IOException {
        String userId = JwtUtils.getCurrentUserId();

        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        CommentImage commentImage = null;
        if (files != null && !files.isEmpty()) {
            MultipartFile file = files.getFirst();
            String storedFileName = firebaseStorageService.uploadFile(URL_BUCKET_NAME.BUCKET_NAME, URL_BUCKET_NAME.COMMENT_FOLDER, file);
            commentImage = CommentImage.builder()
                    .urlImagePost(storedFileName)
                    .build();
        }

        Comment comment = Comment.builder()
                .postId(postId)
                .userId(userId)
                .content(request.getContent())
                .commentImage(commentImage)
                .build();
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public PostResponse createPost(PostRequest request, List<MultipartFile> files) throws IOException {
        String userId = JwtUtils.getCurrentUserId();
        Post post = postMapper.toCreatePost(request);
        post.setUserId(userId);

        if (files != null && !files.isEmpty()) {
            MultipartFile file = files.getFirst();
            String storedFileName = firebaseStorageService.uploadFile(URL_BUCKET_NAME.BUCKET_NAME, URL_BUCKET_NAME.POST_FOLDER, file);

            // Create and set PostImage
            PostImage postImage = PostImage.builder()
                    .urlImagePost(storedFileName)
                    .build();
            post.setPostImage(postImage);
        }

        Post savedPost = postRepository.save(post);
        return postMapper.toPostResponse(savedPost);
    }


    @Override
    public ShareResponse sharePost(String postId) {
        String userId = JwtUtils.getCurrentUserId();
        Share share = Share.builder()
                .postId(postId)
                .userId(userId)
                .build();
        Share savedShare = shareRepository.save(share);
        return shareMapper.toShareResponse(savedShare);
    }

    @Override
    public long getLikeCount(String postId) {
        return likeRepository.countByPostId(postId);
    }

    @Override
    public long getCommentCount(String postId) {
        return commentRepository.countByPostId(postId);
    }

    @Override
    public long getShareCount(String postId) {
        return shareRepository.countByPostId(postId);
    }
}
