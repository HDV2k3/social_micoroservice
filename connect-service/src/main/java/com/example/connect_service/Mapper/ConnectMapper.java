package com.example.connect_service.Mapper;


import com.example.connect_service.Dto.Response.ConnectResponse;
import com.example.connect_service.Entity.Connect;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConnectMapper {

    ConnectResponse toConnectCreateResponse(Connect connect);
}
