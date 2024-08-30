package com.example.connect_service.Facade;

import com.example.connect_service.Dto.Request.ConnectRequest;
import com.example.connect_service.Dto.Response.ConnectResponse;
import com.example.connect_service.Entity.Enum.ConnectStatus;

import java.util.List;

public interface ConnectFacade {
    ConnectResponse createConnect(ConnectRequest request);
    ConnectResponse updateConnectStatus(String connectId, ConnectStatus newStatus);
    List<ConnectResponse> getMyConnects();
    ConnectResponse acceptConnectRequest(String connectId);
}