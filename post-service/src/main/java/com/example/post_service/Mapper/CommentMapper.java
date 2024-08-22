package com.example.post_service.Mapper;

import com.example.post_service.Dto.Response.CommentResponse;
import com.example.post_service.Dto.Response.PostResponse;
import com.example.post_service.Entity.Comment;
import com.example.post_service.Entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponse toCommentResponse(Comment comment);
}
