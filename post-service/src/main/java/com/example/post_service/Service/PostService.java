package com.example.post_service.Service;

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
import com.example.post_service.Mapper.PostMapper;
import com.example.post_service.Mapper.ShareMapper;
import com.example.post_service.Repository.CommentRepository;
import com.example.post_service.Repository.LikeRepository;
import com.example.post_service.Repository.PostRepository;
import com.example.post_service.Repository.ShareRepository;
import com.example.post_service.Repository.httpclient.ProfileClient;
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
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {

    PostRepository postRepository;
    PostMapper postMapper;
    LikeRepository likeRepository;
    CommentRepository commentRepository;
    ShareRepository shareRepository;
    ProfileClient profileClient;
    ShareMapper shareMapper;
    FirebaseStorageService firebaseStorageService;

    public PageResponse<UserProfileResponse> getUserProfilesWhoLikedPost(String postId, int page, int size) {
        List<String> userIds = likeRepository.findByPostId(postId)
                .stream()
                .map(Like::getUserId)
                .collect(Collectors.toList());

        // Check if userIds list is empty
        if (userIds.isEmpty()) {
            return PageResponse.<UserProfileResponse>builder()
                    .currentPage(page)
                    .pageSize(size)
                    .totalPages(0)
                    .totalElements(0)
                    .data(Collections.emptyList())
                    .build();
        }

        // Call Profile Client with pagination
        ApiResponse<PageResponse<UserProfileResponse>> response = profileClient.getProfiles(userIds, page, size);

        return response.getResult();
    }

    public PageResponse<UserProfileResponse> getUserProfilesWhoCommentedOnPost(String postId, int page, int size) {
        List<String> userIds = commentRepository.findByPostId(postId)
                .stream()
                .map(Comment::getUserId)
                .distinct()
                .collect(Collectors.toList());

        // Kiểm tra danh sách userIds
        System.out.println("User IDs: " + userIds);

        // Gọi Profile Client với phân trang
        ApiResponse<PageResponse<UserProfileResponse>> response = profileClient.getProfiles(userIds, page, size);

        // Kiểm tra dữ liệu trả về
        System.out.println("Profile Response: " + response);

        return response.getResult(); // Extract PageResponse<UserProfileResponse> from ApiResponse
    }

    public PageResponse<UserProfileResponse> getUserProfilesWhoSharedPost(String postId, int page, int size) {
        List<String> userIds = shareRepository.findByPostId(postId)
                .stream()
                .map(Share::getUserId)
                .collect(Collectors.toList());

        // Call Profile Client with pagination
        ApiResponse<PageResponse<UserProfileResponse>> response = profileClient.getProfiles(userIds, page, size);
        return response.getResult(); // Assuming ApiResponse<PageResponse<UserProfileResponse>> has a PageResponse<UserProfileResponse> result
    }

    public PageResponse<CommentResponse> getAllCommentsForPost(String postId, int page, int size) {
        // Lấy danh sách comment từ repository với phân trang
        Page<Comment> commentsPage = commentRepository.findByPostId(postId, PageRequest.of(page - 1, size));

        // Lấy danh sách userId từ các comment
        List<String> userIds = commentsPage.getContent().stream()
                .map(Comment::getUserId)
                .distinct()
                .collect(Collectors.toList());

        // Kiểm tra nếu danh sách userIds không trống trước khi gọi profile service
        if (userIds.isEmpty()) {
            return PageResponse.<CommentResponse>builder()
                    .currentPage(commentsPage.getNumber() + 1)
                    .pageSize(commentsPage.getSize())
                    .totalPages(commentsPage.getTotalPages())
                    .totalElements(commentsPage.getTotalElements())
                    .data(Collections.emptyList())
                    .build();
        }

        // Gọi profile service để lấy thông tin người dùng với phân trang
        ApiResponse<PageResponse<UserProfileResponse>> profileResponse = profileClient.getProfiles(userIds, page, size);
        // Tạo map để ánh xạ userId với thông tin người dùng
        Map<String, UserProfileResponse> userProfileMap = profileResponse.getResult().getData().stream()
                .collect(Collectors.toMap(UserProfileResponse::getUserId, userProfile -> userProfile));

        // Chuyển đổi danh sách comment sang CommentResponse và ghép thông tin user
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


    // Trả về danh sách tất cả các comment của bài viết
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


    public void likePost(String postId) {
        String userId = JwtUtils.getCurrentUserId();
        if (likeRepository.findByPostIdAndUserId(postId, userId).isEmpty()) {
            Like like = Like.builder()
                    .postId(postId)
                    .userId(userId)
                    .build();
            likeRepository.save(like);
        } else {
            throw new AppException(ErrorCode.INVALID_KEY); // Replace with appropriate error code
        }
    }

    public void commentOnPost(String postId, CommentRequest request, List<MultipartFile> files) throws IOException {
        String userId = JwtUtils.getCurrentUserId();

        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new AppException(ErrorCode.ID_NOT_FOUND); // Post not found
        }
        CommentImage commentImage = null;
        if (files != null && !files.isEmpty()) {
            MultipartFile file = files.getFirst();
            String storedFileName = firebaseStorageService.uploadFile(URL_BUCKET_NAME.BUCKET_NAME, URL_BUCKET_NAME.COMMENT_FOLDER, file);
            commentImage = CommentImage.builder()
                    .name(file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown")
                    .type(file.getContentType() != null ? file.getContentType() : "unknown")
                    .urlImagePost(storedFileName)
                    .build();
        }
        Comment comment = Comment.builder()
                .postId(postId)
                .userId(userId)
                .content(request.getContent())
                .commentImage(commentImage)
                .createDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();
        commentRepository.save(comment);
    }

    //    public PostResponse createPost(PostRequest request, List<MultipartFile> files) throws IOException {
//        String userId = JwtUtils.getCurrentUserId();
//        Post post = Post.builder()
//                .content(request.getContent())
//                .userId(userId)
//                .createDate(Instant.now())
//                .modifiedDate(Instant.now())
//                .build();
//        postRepository.save(post);
//        return postMapper.toPostResponse(post);
//    }
    public PostResponse createPost(PostRequest request, List<MultipartFile> files) throws IOException {
        String userId = JwtUtils.getCurrentUserId();

        // Create a PostImage object if files are provided
        PostImage postImage = null;
        if (files != null && !files.isEmpty()) {
            MultipartFile file = files.getFirst();
            String storedFileName = firebaseStorageService.uploadFile(URL_BUCKET_NAME.BUCKET_NAME, URL_BUCKET_NAME.POST_FOLDER, file);
            postImage = PostImage.builder()
                    .name(file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown")
                    .type(file.getContentType() != null ? file.getContentType() : "unknown")
                    .urlImagePost(storedFileName)
                    .build();
        }

        Post post = Post.builder()
                .content(request.getContent())
                .userId(userId)
                .createDate(Instant.now())
                .modifiedDate(Instant.now())
                .postImage(postImage) // Set the image
                .likeCount(0L)
                .shareCount(0L)
                .commentCount(0L)
                .build();

        postRepository.save(post);
        return postMapper.toPostResponse(post);
    }


    public ShareResponse sharePost(String postId) {
        String userId = JwtUtils.getCurrentUserId();

        // Get the post information
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new AppException(ErrorCode.ID_NOT_FOUND); // Post not found
        }
        Post post = postOptional.get();

        // Create a list of userIds including the author of the post
        List<String> userIds = Collections.singletonList(post.getUserId());

        // Call the profile service to get user information
        ApiResponse<PageResponse<UserProfileResponse>> profileResponse = profileClient.getProfiles(userIds, 1, 10);

        UserProfileResponse author = profileResponse.getResult().getData().stream()
                .filter(profile -> profile.getUserId().equals(post.getUserId()))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)); // Author not found

        // Create and save the Share entity with post and author information
        Share share = Share.builder()
                .postId(postId)
                .userId(userId)
                .sharedAt(Instant.now())
                .postContent(post.getContent())
                .postAuthorId(author.getUserId())
                .postAuthorFirstName(author.getFirstName())
                .postAuthorLastName(author.getLastName())
                .build();
        shareRepository.save(share);

        // Convert Share entity to ShareResponse DTO
        return shareMapper.toShareResponse(share);
    }


    public long getLikeCount(String postId) {
        return likeRepository.countByPostId(postId);
    }

    public long getCommentCount(String postId) {
        return commentRepository.countByPostId(postId);
    }

    public long getShareCount(String postId) {
        return shareRepository.countByPostId(postId);
    }


}
