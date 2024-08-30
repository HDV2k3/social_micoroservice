package com.example.connect_service.Service;

import com.example.connect_service.Dto.Request.ConnectRequest;
import com.example.connect_service.Dto.Response.ConnectResponse;
import com.example.connect_service.Entity.Enum.ConnectStatus;

import java.util.List;

public interface ConnectService {
    boolean existingConnect(String userId, String followingId);
    ConnectResponse createConnect(ConnectRequest request);
    ConnectResponse updateConnectStatus(String connectId, ConnectStatus newStatus);
    List<ConnectResponse> getMyConnects();
    ConnectResponse acceptConnectRequest(String connectId);
}
