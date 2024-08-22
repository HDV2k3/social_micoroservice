package com.example.post_service.Mapper;
import com.example.post_service.Dto.Response.ShareResponse;
import com.example.post_service.Entity.Share;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShareMapper {
    ShareResponse toShareResponse(Share share);
}
