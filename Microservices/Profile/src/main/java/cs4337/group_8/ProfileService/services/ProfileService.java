package cs4337.group_8.ProfileService.services;

import cs4337.group_8.ProfileService.entities.ProfileEntity;
import cs4337.group_8.ProfileService.exceptions.SampleCustomException;
import cs4337.group_8.ProfileService.repositories.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    // It is okay to pull in more repositories AND services to use

    public ProfileService(final ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    // Business logic
    public void sampleMethod() {
        log.info("Sample service method called");
        log.error("Error log: {}", "Some variable goes here");
        // Log a throwable
        log.warn("Warning log:", new SampleCustomException("Some exception goes here"));
    }

    public ProfileEntity getUserExistanceById(final String id) {
        return profileRepository.findById(id).orElseThrow(() -> new SampleCustomException("User not found"));
    }

    public void updateByUserId(
            final String userId,
            final String fullName,
            final String bio,
            final String profilePic
    ) {
        profileRepository.updateByUserId(userId, fullName, bio, profilePic);
    }
}
