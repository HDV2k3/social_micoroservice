package com.example.connect_service.Service;
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
public class ConnectService {

    ConnectRepository connectRepository;
    ConnectMapper connectMapper;
    ProfileClient profileClient;
    FirebaseStorageService firebaseStorageService;

    // Kiểm tra xem userId có đang follow followingId hay không
    public boolean existingConnect(String userId, String followingId) {
        return connectRepository.findByFollowerIdAndFollowingId(userId, followingId).isPresent();
    }

    public ConnectResponse createConnect(ConnectRequest request) {
        String userIdA = JwtUtils.getCurrentUserId();

        // Check if the connection already exists
        if (existingConnect(userIdA, request.getFollowingId())) {
            throw new AppException(ErrorCode.ALREADY_EXISTS);
        }

        // Fetch User A's profile information using ProfileClient
        UserProfileResponse userProfile = profileClient.getProfile(userIdA);

        // Generate a signed URL for the avatar image
        // full path to the image in Firebase Storage
        String fullPath = URL_BUCKET_NAME.AVATAR_FOLDER + userProfile.getAvatar();
        String avatarUrl;
        try {
            avatarUrl = firebaseStorageService.getSignedUrl(URL_BUCKET_NAME.BUCKET_NAME, fullPath);
        } catch (Exception e) {
            throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        }
        // Create and save the Connect object with profile information
        Connect newConnect = Connect.builder()
                .followerId(userIdA)
                .followingId(request.getFollowingId())
                .followedAt(Instant.now())
                .status(ConnectStatus.PENDING)
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .avatar(avatarUrl)  // Use the signed URL for the avatar
                .build();

        Connect savedConnect = connectRepository.save(newConnect);

        // Return a successful response
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

    public ConnectResponse acceptConnectRequest(String connectId) {
        // Retrieve the connection request by ID
        Connect connect = connectRepository.findById(connectId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        // Check if the current user is the intended recipient of the connection request
        String userId = JwtUtils.getCurrentUserId();
        if (!connect.getFollowingId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Update the status to ACCEPTED
        connect.setStatus(ConnectStatus.ACCEPTED);
        connect.setAcceptedAt(Instant.now());  // Optional: Track when the request was accepted

        // Save the updated connection
        Connect updatedConnect = connectRepository.save(connect);

        // Return the updated connection response
        return connectMapper.toConnectCreateResponse(updatedConnect);
    }

}
