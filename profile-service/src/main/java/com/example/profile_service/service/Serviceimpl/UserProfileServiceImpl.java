package com.example.profile_service.service.Serviceimpl;
import com.example.profile_service.Utils.JwtUtils;
import com.example.profile_service.contants.URL_BUCKET_NAME;
import com.example.profile_service.dto.PageResponse;
import com.example.profile_service.dto.request.*;
import com.example.profile_service.dto.response.*;
import com.example.profile_service.entity.*;
import com.example.profile_service.exception.AppException;
import com.example.profile_service.exception.ErrorCode;
import com.example.profile_service.mapper.*;
import com.example.profile_service.repository.*;
import com.example.profile_service.service.FirebaseStorageService;
import com.example.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class UserProfileServiceImpl implements UserProfileService {
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

    @Override
    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public UserProfileResponse getProfileByUserId(String id) {
        UserProfile userProfile =
                userProfileRepository.findUserByUserId(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        log.info("Avatar Name: {}", userProfile.getAvatar().getUrl());
        String fullPath = URL_BUCKET_NAME.AVATAR_FOLDER + userProfile.getAvatar().getUrl();
        String avatarUrl;
        try {
            avatarUrl = firebaseStorageService.getSignedUrl(URL_BUCKET_NAME.BUCKET_NAME, fullPath);
            log.info("Avatar: {}", avatarUrl);
        } catch (Exception e) {
            throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        }
        return UserProfileResponse.builder()
                .userId(id)
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .avatar(avatarUrl)
                .city(userProfile.getCity())
                .dob(userProfile.getDob())
                .build();
    }

    @Override
    public PageResponse<UserProfileResponse> getProfiles(int page, int size) {
        String userId = JwtUtils.getCurrentUserId();
        Sort sort = Sort.by("firstName").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = userProfileRepository.findAllByUserId(userId, pageable);

        List<UserProfileResponse> profilesWithUrls = pageData.getContent().stream()
                .map(userProfile -> {
                    String fullPath = URL_BUCKET_NAME.AVATAR_FOLDER + userProfile.getAvatar().getUrl();
                    String avatarUrl;
                    try {
                        avatarUrl = firebaseStorageService.getSignedUrl(URL_BUCKET_NAME.BUCKET_NAME, fullPath);
                    } catch (Exception e) {
                        throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
                    }

                    return UserProfileResponse.builder()
                            .userId(userProfile.getUserId())
                            .firstName(userProfile.getFirstName())
                            .lastName(userProfile.getLastName())
                            .avatar(avatarUrl)
                            .build();
                })
                .collect(Collectors.toList());

        return PageResponse.<UserProfileResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(profilesWithUrls)
                .build();
    }

    private UserProfile findUserProfileById(String profileId) {
        return userProfileRepository
                .findById(profileId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
    }

    @Override
    public AvatarProfileResponse uploadAvatarProfile(String profileId, MultipartFile file) throws IOException {
        UserProfile userProfile = findUserProfileById(profileId);
        String imageUrl = firebaseStorageService.uploadFile(URL_BUCKET_NAME.BUCKET_NAME, URL_BUCKET_NAME.AVATAR_FOLDER, file);
        Avatar avatar = new Avatar();
        avatar.setName(file.getOriginalFilename());
        avatar.setType(file.getContentType());
        avatar.setUrl(imageUrl);
        userProfile.setAvatar(avatar);
        userProfileRepository.save(userProfile);
        return avatarMapper.toAvatarProfileResponse(avatar);
    }

    @Override
    public CoverImageResponse uploadCoverImageProfile(String profileId, MultipartFile file) throws IOException {
        UserProfile userProfile = findUserProfileById(profileId);
        String imageUrl = firebaseStorageService.uploadFile(URL_BUCKET_NAME.BUCKET_NAME, URL_BUCKET_NAME.COVER_IMAGE_FOLDER, file);
        CoverImage coverImage = new CoverImage();
        coverImage.setName(file.getOriginalFilename());
        coverImage.setType(file.getContentType());
        coverImage.setUrl(imageUrl);
        userProfile.setCoverImage(coverImage);
        userProfileRepository.save(userProfile);
        return coverImageMapper.toCoverImageProfileResponse(coverImage);
    }

    @Override
    public EducationResponse createEducation(EducationRequest request, String profileId) {
        UserProfile userProfile = findUserProfileById(profileId);
        Education education = educationMapper.toEducationProfile(request);
        userProfile.setEducation(education);
        userProfileRepository.save(userProfile);
        education = educationRepository.save(education);
        return educationMapper.toEducationProfileResponse(education);
    }

    @Override
    public IntroductionResponse createIntroduction(IntroductionRequest request, String profileId) {
        UserProfile userProfile = findUserProfileById(profileId);
        Introduction introduction = introductionMapper.toProfileIntroduction(request);
        userProfile.setIntroduction(introduction);
        userProfileRepository.save(userProfile);
        introduction = introductionRepository.save(introduction);
        return introductionMapper.toProfileIntroductionResponse(introduction);
    }

    @Override
    public SkillResponse createSkill(SkillRequest request, String profileId) {
        UserProfile userProfile = findUserProfileById(profileId);
        Skill skill = skillMapper.toSkillProfile(request);
        userProfile.getSkills().add(skill);
        userProfileRepository.save(userProfile);
        skill = skillRepository.save(skill);
        return SkillResponse.builder()
                .id(skill.getId())
                .skill(skill.getSkill())
                .ProfileId(userProfile.getId())
                .build();
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, String profileId) {
        Project project = projectMapper.toProfileProject(request);
        Set<UserProfile> participants = request.getParticipantIds().stream()
                .map(userId -> userProfileRepository.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)))
                .collect(Collectors.toSet());
        project.setParticipants(participants);
        for (UserProfile participant : participants) {
            participant.getProjects().add(project);
            userProfileRepository.save(participant);
        }
        project = projectRepository.save(project);
        return projectMapper.toProfileProjectResponse(project);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        return userProfiles.stream()
                .map(userProfile -> {
                    String fullPath = URL_BUCKET_NAME.AVATAR_FOLDER + userProfile.getAvatar().getUrl();
                    String avatarUrl;
                    try {
                        avatarUrl = firebaseStorageService.getSignedUrl(URL_BUCKET_NAME.BUCKET_NAME, fullPath);
                    } catch (Exception e) {
                        throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
                    }

                    return UserProfileResponse.builder()
                            .userId(userProfile.getUserId())
                            .firstName(userProfile.getFirstName())
                            .lastName(userProfile.getLastName())
                            .avatar(avatarUrl)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
