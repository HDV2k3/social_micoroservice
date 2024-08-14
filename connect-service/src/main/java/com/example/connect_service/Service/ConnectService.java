package com.example.connect_service.Service;

import com.example.connect_service.Dto.Request.ConnectRequest;
import com.example.connect_service.Dto.Response.ConnectResponse;
import com.example.connect_service.Entity.Connect;
import com.example.connect_service.Entity.Enum.ConnectStatus;
import com.example.connect_service.Exception.AppException;
import com.example.connect_service.Exception.ErrorCode;
import com.example.connect_service.Mapper.ConnectMapper;
import com.example.connect_service.Repository.ConnectRepository;
import com.example.connect_service.Utils.JwtUtils;
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
public class ConnectService {

    ConnectRepository connectRepository;
    ConnectMapper connectMapper;
    // Kiểm tra xem userId có đang follow followingId hay không
    public boolean existingConnect(String userId, String followingId) {
        return connectRepository.findByUser_IdAndFollowing_Id(userId, followingId).isPresent();
    }

    // Tạo kết nối follow
    public ConnectResponse createConnect(ConnectRequest request) {
        String userIdA = JwtUtils.getCurrentUserId();

        // Kiểm tra nếu đã có kết nối tồn tại
        if (existingConnect(userIdA, request.getFollowingId())) {
            throw new AppException(ErrorCode.ALREADY_EXISTS);
        }

        // Tạo đối tượng Connect mới
        Connect newConnect = Connect.builder()
                .followerId(userIdA)
                .followingId(request.getFollowingId())
                .followedAt(Instant.now())
                .status(ConnectStatus.PENDING)
                .build();

        // Lưu đối tượng Connect vào cơ sở dữ liệu
        Connect savedConnect = connectRepository.save(newConnect);

        // Trả về phản hồi thành công
        return connectMapper.toConnectCreateResponse(savedConnect);
    }
    // Cập nhật trạng thái kết nối
    public ConnectResponse updateConnectStatus(String connectId, ConnectStatus newStatus) {
        Connect connect = connectRepository.findById(connectId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        connect.setStatus(newStatus);
        Connect updatedConnect = connectRepository.save(connect);


        return connectMapper.toConnectCreateResponse(updatedConnect);
    }

    public List<ConnectResponse> getMyConnects() {
        String userId = JwtUtils.getCurrentUserId();
        return connectRepository.findUserById(userId).stream()
                .map(connectMapper::toConnectCreateResponse)
                .toList();
    }
}
