package com.example.connect_service.Controller;
import com.example.connect_service.Dto.ApiResponse;
import com.example.connect_service.Dto.ConnectResponseRequest;
import com.example.connect_service.Dto.Request.ConnectRequest;
import com.example.connect_service.Dto.Response.ConnectResponse;
import com.example.connect_service.Facade.ConnectFacade;
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

    ConnectFacade connectFacade;

    @PostMapping("/create-connect")
    ApiResponse<ConnectResponse> createConnect(@RequestBody ConnectRequest request) {
      var result = connectFacade.createConnect(request);
      return ApiResponse.success(result);
    }

    @PutMapping("/response-connect")
    ApiResponse<ConnectResponse> respondToConnect(@RequestBody ConnectResponseRequest request) {
       var result = connectFacade.updateConnectStatus(request.getConnectId(),request.getStatus());
       return ApiResponse.success(result);
    }

    @GetMapping("/lists-request-connect")
    ApiResponse<List<ConnectResponse>> myConnects() {
        var result = connectFacade.getMyConnects();
        return ApiResponse.success(result);
//        return ApiResponse.<List<ConnectResponse>>builder()
//                .result(connectService.getMyConnects())
//                .build();
    }

    @PostMapping("/{connectId}/accept")
    ApiResponse<ConnectResponse> acceptConnectRequest(@PathVariable String connectId) {
        var result = connectFacade.acceptConnectRequest(connectId);
        return ApiResponse.success(result);
//        return ApiResponse.<ConnectResponse>builder()
//                .result(connectService.acceptConnectRequest(connectId))
//                .build();
    }
}
