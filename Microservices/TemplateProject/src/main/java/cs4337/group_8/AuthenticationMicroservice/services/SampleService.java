package cs4337.group_8.AuthenticationMicroservice.services;

import cs4337.group_8.AuthenticationMicroservice.entities.SampleEntity;
import cs4337.group_8.AuthenticationMicroservice.exceptions.SampleCustomException;
import cs4337.group_8.AuthenticationMicroservice.repositories.SampleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SampleService {
    private final SampleRepository sampleRepository;
    // It is okay to pull in more repositories AND services to use

    public SampleService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    // Business logic
    public void sampleMethod() {
        log.info("Sample service method called");
        log.error("Error log: {}", "Some variable goes here");
        // Log a throwable
        log.warn("Warning log:", new SampleCustomException("Some exception goes here"));
    }

    public SampleEntity getUserExistanceById(Integer id){
        return sampleRepository.findById(id).orElseThrow(() -> new SampleCustomException("User not found"));
    }

    public List<SampleEntity> getAllUsersByName(String name){
        Optional<List<SampleEntity>> query = sampleRepository.findAllByFirstnameAndVerified(name, true);
        return query.orElseGet(ArrayList::new);

        // The above is the same as:
        /*
            This may be a bit cleaner
            if (query.isPresent()){
                return query.get();
            } else {
                return new ArrayList<SampleEntity>();
            }
        */
    }
}
