package com.example.connect_service.Service.ServiceImpl;

import com.example.connect_service.Dto.Request.ConnectRequest;
import com.example.connect_service.Dto.Response.ConnectResponse;
import com.example.connect_service.Dto.Response.UserProfileResponse;
import com.example.connect_service.Entity.Connect;
import com.example.connect_service.Entity.Enum.ConnectStatus;
import com.example.connect_service.Exception.AppException;
import com.example.connect_service.Exception.ErrorCode;
import com.example.connect_service.Mapper.ConnectMapper;
import com.example.connect_service.Repository.ConnectRepository;
import com.example.connect_service.Repository.httpclient.ProfileClient;
import com.example.connect_service.Service.ConnectService;
import com.example.connect_service.Utils.JwtUtils;
import com.example.connect_service.contants.URL_BUCKET_NAME;
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
public class ConnectServiceImpl implements ConnectService {

    ConnectRepository connectRepository;
    ConnectMapper connectMapper;
    ProfileClient profileClient;
    @Override
    public boolean existingConnect(String userId, String followingId) {
        return connectRepository.findByFollowerIdAndFollowingId(userId, followingId).isPresent();
    }

    @Override
    public ConnectResponse createConnect(ConnectRequest request) {
        String userIdA = JwtUtils.getCurrentUserId();

        if (existingConnect(userIdA, request.getFollowingId())) {
            throw new AppException(ErrorCode.ALREADY_EXISTS);
        }

        UserProfileResponse userProfile = profileClient.getProfile(userIdA).getResult();
        String avatarUrl = URL_BUCKET_NAME.AVATAR_FOLDER + userProfile.getAvatar();
        Connect newConnect = Connect.builder()
                .followerId(userIdA)
                .followingId(request.getFollowingId())
                .followedAt(Instant.now())
                .status(ConnectStatus.PENDING)
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .avatar(avatarUrl)
                .build();

        Connect savedConnect = connectRepository.save(newConnect);
        return connectMapper.toConnectCreateResponse(savedConnect);
    }

    @Override
    public ConnectResponse updateConnectStatus(String connectId, ConnectStatus newStatus) {
        Connect connect = connectRepository.findById(connectId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        connect.setStatus(newStatus);
        Connect updatedConnect = connectRepository.save(connect);
        return connectMapper.toConnectCreateResponse(updatedConnect);
    }

    @Override
    public List<ConnectResponse> getMyConnects() {
        String userId = JwtUtils.getCurrentUserId();
        return connectRepository.findUserById(userId).stream()
                .map(connectMapper::toConnectCreateResponse)
                .toList();
    }

    @Override
    public ConnectResponse acceptConnectRequest(String connectId) {
        Connect connect = connectRepository.findById(connectId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        String userId = JwtUtils.getCurrentUserId();
        if (!connect.getFollowingId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        connect.setStatus(ConnectStatus.ACCEPTED);
        connect.setAcceptedAt(Instant.now());

        Connect updatedConnect = connectRepository.save(connect);
        return connectMapper.toConnectCreateResponse(updatedConnect);
    }
}
