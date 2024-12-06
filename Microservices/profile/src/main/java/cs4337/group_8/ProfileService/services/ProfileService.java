package cs4337.group_8.ProfileService.services;

import cs4337.group_8.ProfileService.DTO.ProfileDTO;
import cs4337.group_8.ProfileService.entities.FollowingEntity;
import cs4337.group_8.ProfileService.entities.ProfileEntity;
import cs4337.group_8.ProfileService.entities.Status;
import cs4337.group_8.ProfileService.exceptions.SampleCustomException;
import cs4337.group_8.ProfileService.mappers.GoogleUserDetails;
import cs4337.group_8.ProfileService.repositories.FollowingRepository;
import cs4337.group_8.ProfileService.repositories.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final GoogleResourceExchangeService googleResourceService;
    private final FollowingRepository followingRepository;
    private  final KafkaProducer kafkaProducer;
    // It is okay to pull in more repositories AND services to use

    public ProfileService(ProfileRepository ProfileRepository,
                          GoogleResourceExchangeService googleResourceService,
                          FollowingRepository followingRepository, KafkaProducer kafkaProducer) {
        this.profileRepository = ProfileRepository;
        this.googleResourceService = googleResourceService;
        this.followingRepository = followingRepository;
        this.kafkaProducer = kafkaProducer;
    }

    // Business logic
    public void sampleMethod() {
        log.info("Sample service method called");
        log.error("Error log: {}", "Some variable goes here");
        // Log a throwable
        log.warn("Warning log:", new SampleCustomException("Some exception goes here"));
    }
    public ProfileEntity getUserExistanceById(String id){
        return profileRepository.findByIdEquals(id).orElseThrow(() -> new SampleCustomException("User not found"));
    }

    public ProfileEntity createNewUserIfNotExists(String id, String accessToken) {
        ProfileEntity profile = profileRepository.findById(id).orElseGet(() -> createUser(id, accessToken));
        kafkaProducer.sendMessage("User profile created or Already exists: " + profile.getUser_id());
        return profile;
    }

    public ProfileEntity createUser(String id, String accessToken) {
        ProfileEntity profile = new ProfileEntity();
        profile.setUser_id(id);
        GoogleUserDetails googleUserDetails = googleResourceService.getGoogleProfileDetails(accessToken);
        profile.setFull_name(googleUserDetails.getName());
        profile.setBio("");
        profile.setProfile_pic(googleUserDetails.getPicture());
        profile.setCount_follower(0);
        profile.setCount_following(0);
        ProfileEntity savedProfile = profileRepository.save(profile);
        kafkaProducer.sendMessage("New user profile created: " + savedProfile.getUser_id());
        return savedProfile;
    }

    public ProfileEntity createBlankUser(String userId) {
        ProfileEntity profile = new ProfileEntity();
        profile.setUser_id(userId);
        profile.setFull_name("Unknown");
        profile.setBio("");
        profile.setProfile_pic("");
        profile.setCount_follower(0);
        profile.setCount_following(0);
        return profileRepository.save(profile);
    }

    public void validateUserExists(String userId, String accessToken) {
        profileRepository.findById(userId).orElseGet(
                () -> createUser(userId, accessToken)
        );
    }

    public void validateTargetExists(String targetId) {
        profileRepository.findById(targetId).orElseGet(
                () -> createBlankUser(targetId)
        );
    }

    public ProfileEntity applyFollow(String followerId, String followingId) {
        Optional<FollowingEntity> fe = followingRepository.findByInitiatorIdAndTargetId(followerId, followingId);
        ProfileEntity initiator = null;
        if (fe.isPresent()) {   // If user has followed at some point, we retain history
            FollowingEntity followingEntity = fe.get();
            if (followingEntity.getStatus() == Status.ACTIVE) {
                initiator = unfollow(followerId, followingId);
                kafkaProducer.sendMessage("User " + followerId + " unfollowed user " + followingId);
            } else if (followingEntity.getStatus() == Status.INACTIVE) {
                initiator = refollow(followerId, followingId);
                kafkaProducer.sendMessage("User " + followerId + " refollowed user " + followingId);
            }
        } else {
            // If there was never an interaction, create one
            followingRepository.save(new FollowingEntity(followerId, followingId, Instant.now(), Status.ACTIVE, Instant.now()));
            initiator = updateFollowerFollowingCount(followerId, followingId);
            kafkaProducer.sendMessage("User " + followerId + " followed user " + followingId);
        }
        return initiator;
    }


    public ProfileEntity unfollow(String followerId, String followingId) {
        FollowingEntity followingEntity = followingRepository.findByInitiatorIdAndTargetId(followerId, followingId).get();

        followingEntity.setStatus(Status.INACTIVE);
        followingEntity.setUpdatedAt(Instant.now());
        followingRepository.save(followingEntity);
        return updateFollowerFollowingCount(followerId, followingId);
    }

    public ProfileEntity refollow(String followerId, String followingId) {
        FollowingEntity followingEntity = followingRepository.findByInitiatorIdAndTargetId(followerId, followingId).get();

        followingEntity.setStatus(Status.ACTIVE);
        followingEntity.setUpdatedAt(Instant.now());
        followingRepository.save(followingEntity);
        return updateFollowerFollowingCount(followerId, followingId);
    }

    public ProfileEntity updateFollowerFollowingCount(String followerId, String followingId) {
        Integer followingCount = followingRepository.countAllByInitiatorIdEqualsAndStatusEquals(followerId, Status.ACTIVE);
        ProfileEntity initatiator = profileRepository.findByIdEquals(followerId).get();
        initatiator.setCount_following(followingCount);
        Integer followerCount = followingRepository.countAllByTargetIdEqualsAndStatusEquals(followingId, Status.ACTIVE);
        ProfileEntity target = profileRepository.findByIdEquals(followingId).get();
        target.setCount_follower(followerCount);
        profileRepository.save(target);
        return profileRepository.save(initatiator);
    }

    public void updateByUserId(
            String userId,
            String fullName,
            String bio,
            String profilePic
    ) {
        profileRepository.updateByUserId(userId, fullName, bio, profilePic);
    }

    public void updateByUserId(
            ProfileDTO updatedProfile
    ) {
        updateByUserId(
            updatedProfile.getUser_id(),
            updatedProfile.getFull_name(),
            updatedProfile.getBio(),
            updatedProfile.getProfile_pic()
        );
        kafkaProducer.sendMessage("Profile updated for user: " + updatedProfile.getUser_id());
    }

    public void updateByUserId(
            String userId,
            ProfileDTO updatedProfile
    ) {
        updateByUserId(
                userId,
                updatedProfile.getFull_name(),
                updatedProfile.getBio(),
                updatedProfile.getProfile_pic()
        );
    }
}
