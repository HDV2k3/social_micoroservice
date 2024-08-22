package com.example.connect_service.Controller;

import com.example.connect_service.Dto.ApiResponse;
import com.example.connect_service.Dto.ConnectResponseRequest;
import com.example.connect_service.Dto.Request.ConnectRequest;
import com.example.connect_service.Dto.Response.ConnectResponse;
import com.example.connect_service.Service.ConnectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConnectController {

    ConnectService connectService;

    @PostMapping("/create-connect")
    ApiResponse<ConnectResponse> createConnect(@RequestBody ConnectRequest request) {
        return ApiResponse.<ConnectResponse>builder()
                .result(connectService.createConnect(request))
                .build();
    }

    @PutMapping("/response-connect")
    ApiResponse<ConnectResponse> respondToConnect(@RequestBody ConnectResponseRequest request) {
        return ApiResponse.<ConnectResponse>builder()
                .result(connectService.updateConnectStatus(request.getConnectId(), request.getStatus()))
                .build();
    }

    @GetMapping("/lists")
    ApiResponse<List<ConnectResponse>> myConnects() {
        return ApiResponse.<List<ConnectResponse>>builder()
                .result(connectService.getMyConnects())
                .build();
    }
}
