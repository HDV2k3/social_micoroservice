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
        findUserProfileById(profileId);
        Education education = educationMapper.toEducationProfile(request);
        education = educationRepository.save(education);
        return educationMapper.toEducationProfileResponse(education);
    }

    public IntroductionResponse createIntroduction(IntroductionRequest request,String profileId)
    {
        findUserProfileById(profileId);
        Introduction introduction = introductionMapper.toProfileIntroduction(request);
        introduction = introductionRepository.save(introduction);
        return  introductionMapper.toProfileIntroductionResponse(introduction);
    }
    public SkillResponse createSkill(SkillRequest request,String profileId)
    {
        findUserProfileById(profileId);
        Skill skill = skillMapper.toSkillProfile(request);
        skill =skillRepository.save(skill);
        return skillMapper.toSkillProfileResponse(skill);
    }
    public ProjectResponse createProject(ProjectRequest request,String profileId)
    {
        findUserProfileById(profileId);
        Project project = projectMapper.toProfileProject(request);

        // Duyệt qua danh sách participantIds để tìm và thêm UserProfile vào project
        Set<UserProfile> participants = request.getParticipantIds().stream()
                .map(userId -> userProfileRepository.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)))
                .collect(Collectors.toSet());

        project.setParticipants(participants);
        project = projectRepository.save(project);
        return  projectMapper.toProfileProjectResponse(project);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        return userProfiles.stream()
                .map(userProfileMapper::toUserProfileResponse)
                .collect(Collectors.toList());
    }

}
