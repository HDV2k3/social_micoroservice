package com.example.connect_service.Facade.FacadeImpl;

import com.example.connect_service.Dto.Request.ConnectRequest;
import com.example.connect_service.Dto.Response.ConnectResponse;
import com.example.connect_service.Entity.Enum.ConnectStatus;
import com.example.connect_service.Facade.ConnectFacade;
import com.example.connect_service.Service.ConnectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConnectFacadeImpl implements ConnectFacade {

    ConnectService connectService;

    @Override
    public ConnectResponse createConnect(ConnectRequest request) {
        return connectService.createConnect(request);
    }

    @Override
    public ConnectResponse updateConnectStatus(String connectId, ConnectStatus newStatus) {
        return connectService.updateConnectStatus(connectId, newStatus);
    }

    @Override
    public List<ConnectResponse> getMyConnects() {
        return connectService.getMyConnects();
    }

    @Override
    public ConnectResponse acceptConnectRequest(String connectId) {
        return connectService.acceptConnectRequest(connectId);
    }
}
