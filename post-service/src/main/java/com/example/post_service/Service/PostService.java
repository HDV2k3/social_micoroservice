package com.example.post_service.Service;

import com.example.post_service.Dto.Request.PostRequest;
import com.example.post_service.Dto.Response.PostResponse;
import com.example.post_service.Entity.Post;
import com.example.post_service.Mapper.PostMapper;
import com.example.post_service.Repository.PostRepository;
import com.example.post_service.Utils.JwtUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class PostService {

    PostRepository postRepository;
    PostMapper postMapper;
    public PostResponse createPost(PostRequest request) {
        String userId = JwtUtils.getCurrentUserId();
        Post post = Post.builder()
                .content(request.getContent())
                .userId(userId)
                .createDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();
        postRepository.save(post);
        return postMapper.toPostResponse(post);
    }
    public List<PostResponse> getMyPosts() {
        String userId = JwtUtils.getCurrentUserId();
        return postRepository.findUserById(userId).stream()
                .map(postMapper::toPostResponse)
                .toList();
    }
}
