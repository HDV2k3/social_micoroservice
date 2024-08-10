package com.example.profile_service.service;

import com.example.profile_service.contants.URL_BUCKET_NAME;
import com.example.profile_service.dto.request.*;
import com.example.profile_service.dto.response.*;
import com.example.profile_service.entity.*;
import com.example.profile_service.exception.AppException;
import com.example.profile_service.exception.ErrorCode;
import com.example.profile_service.mapper.*;
import com.example.profile_service.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    EducationRepository educationRepository;
    UserProfileMapper userProfileMapper;
    FirebaseStorageService firebaseStorageService;
    AvatarMapper avatarMapper;
    CoverImageMapper coverImageMapper;
    EducationMapper educationMapper;
    IntroductionMapper introductionMapper;
    SkillMapper skillMapper;
    SkillRepository skillRepository;
    IntroductionRepository introductionRepository;
    ProjectMapper projectMapper;
    ProjectRepository projectRepository;
    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id) .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }
    // Method to find a user profile by ID
    private UserProfile findUserProfileById(String profileId) {
        return userProfileRepository
                .findById(profileId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
    }
    public AvatarProfileResponse uploadAvatarProfile(String profileId, MultipartFile file) throws IOException {
        // Find the user profile
        UserProfile userProfile = findUserProfileById(profileId);

        // Upload the avatar to Firebase and get the URL
        String imageUrl = firebaseStorageService.uploadFile(URL_BUCKET_NAME.BUCKET_NAME, URL_BUCKET_NAME.AVATAR_FOLDER, file);

        // Create a new Avatar entity
        Avatar avatar = new Avatar();
        avatar.setName(file.getOriginalFilename());
        avatar.setType(file.getContentType());
        avatar.setUrl(imageUrl);

        // Associate the avatar with the user profile
        userProfile.setAvatar(avatar);

        // Save the updated user profile
        userProfileRepository.save(userProfile);

        // Use the mapper to convert the Avatar entity to AvatarProfileResponse DTO
       return avatarMapper.toAvatarProfileResponse(avatar);
    }
    public CoverImageResponse uploadCoverImageProfile(String profileId, MultipartFile file) throws IOException {
        UserProfile userProfile = findUserProfileById(profileId);

        String imageUrl = firebaseStorageService.uploadFile(URL_BUCKET_NAME.BUCKET_NAME, URL_BUCKET_NAME.COVER_IMAGE_FOLDER, file);

        CoverImage coverImage =new CoverImage();
        coverImage.setName(file.getOriginalFilename());
        coverImage.setType(file.getContentType());
        coverImage.setUrl(imageUrl);

        userProfile.setCoverImage(coverImage);

        userProfileRepository.save(userProfile);

       return coverImageMapper.toCoverImageProfileResponse(coverImage);

    }
    public EducationResponse createEducation(EducationRequest request,String profileId)
    {
        UserProfile userProfile = findUserProfileById(profileId);

        Education education = educationMapper.toEducationProfile(request);
        userProfile.setEducation(education);
        userProfileRepository.save(userProfile);
        education = educationRepository.save(education);
        return educationMapper.toEducationProfileResponse(education);
    }

    public IntroductionResponse createIntroduction(IntroductionRequest request,String profileId)
    {
        UserProfile userProfile = findUserProfileById(profileId);
        Introduction introduction = introductionMapper.toProfileIntroduction(request);
        userProfile.setIntroduction(introduction);
        userProfileRepository.save(userProfile);
        introduction = introductionRepository.save(introduction);
        return  introductionMapper.toProfileIntroductionResponse(introduction);
    }
    public SkillResponse createSkill(SkillRequest request,String profileId)
    {
        UserProfile userProfile = findUserProfileById(profileId);
        Skill skill = skillMapper.toSkillProfile(request);
        // Thêm kỹ năng vào tập hợp kỹ năng của UserProfile
        userProfile.getSkills().add(skill);
        // Lưu UserProfile và tạo mối quan hệ HAS_SKILL
        userProfileRepository.save(userProfile);

        skill =skillRepository.save(skill);
        // Tạo SkillResponse và trả về
        return SkillResponse.builder()
                .id(skill.getId())
                .skill(skill.getSkill())
                .ProfileId(userProfile.getId()) // Đặt ProfileId
                .build();
    }
    public ProjectResponse createProject(ProjectRequest request, String profileId) {
        // Tạo một Project từ request
        Project project = projectMapper.toProfileProject(request);

        // Duyệt qua danh sách participantIds để tìm và thêm UserProfile vào project
        Set<UserProfile> participants = request.getParticipantIds().stream()
                .map(userId -> userProfileRepository.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)))
                .collect(Collectors.toSet());

        // Cập nhật mối quan hệ giữa Project và các UserProfile
        project.setParticipants(participants);

        // Cập nhật các UserProfile để thiết lập mối quan hệ INVOLVES với Project
        for (UserProfile participant : participants) {
            participant.getProjects().add(project);
            userProfileRepository.save(participant);  // Lưu lại UserProfile với mối quan hệ mới
        }

        // Lưu Project vào cơ sở dữ liệu
        project = projectRepository.save(project);

        // Chuyển đổi Project thành ProjectResponse và trả về
        return projectMapper.toProfileProjectResponse(project);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        return userProfiles.stream()
                .map(userProfileMapper::toUserProfileResponse)
                .collect(Collectors.toList());
    }

}
