package cs4337.group_8.TemplateProject.services;

import cs4337.group_8.TemplateProject.entities.ProfileEntity;
import cs4337.group_8.TemplateProject.exceptions.SampleCustomException;
import cs4337.group_8.TemplateProject.repositories.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProfileService {
    private final ProfileRepository ProfileRepository;
    // It is okay to pull in more repositories AND services to use

    public ProfileService(ProfileRepository ProfileRepository) {
        this.ProfileRepository = ProfileRepository;
    }

    // Business logic
    public void sampleMethod() {
        log.info("Sample service method called");
        log.error("Error log: {}", "Some variable goes here");
        // Log a throwable
        log.warn("Warning log:", new SampleCustomException("Some exception goes here"));
    }

    public ProfileEntity getUserExistanceById(String id){
        return ProfileRepository.findById(id).orElseThrow(() -> new SampleCustomException("User not found"));
    }

    public List<ProfileEntity> getAllUsersByName(String name){
        Optional<List<ProfileEntity>> query = ProfileRepository.findAllByFirstnameAndVerified(name, true);
        return query.orElseGet(ArrayList::new);

        // The above is the same as:
        /*
            This may be a bit cleaner
            if (query.isPresent()){
                return query.get();
            } else {
                return new ArrayList<ProfileEntity>();
            }
        */
    }
}
