package com.example.post_service.Mapper;

import com.example.post_service.Dto.Response.PostResponse;
import com.example.post_service.Entity.Post;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostResponse toPostResponse(Post post);
}
