package cs4337.group_8.ProfileService.controllers;

import cs4337.group_8.ProfileService.DTO.ProfileDTO;
import cs4337.group_8.ProfileService.exceptions.SampleCustomException;
import cs4337.group_8.ProfileService.mappers.ProfileMapper;
import cs4337.group_8.ProfileService.services.KafkaProducer;
import cs4337.group_8.ProfileService.services.ProfileService;
import cs4337.group_8.ProfileService.entities.ProfileEntity;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/profile")
// Simple logging interface: meaning we can plug in and out different logging implementations. By Default its the Springboot logger
@Slf4j
public class ControllerProfile {

    // Bean injections
    // It is okay to have more than one service, but nothing lower
    private final ProfileService profileService;
    private final KafkaProducer kafkaProducer;

    public ControllerProfile(ProfileService profileService, KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
        this.profileService = profileService;

    }

    @GetMapping("/test")
    public String test() {
        kafkaProducer.sendMessage("Profile Microservice kafka test");
        return "Hello World";
    }

    @PostMapping(
        value = "/new",
        produces = "application/json",
        consumes = "application/json"
    )
    public ResponseEntity<ProfileDTO> addNewUserFromRegistration(
        @Valid
        @RequestBody
        ProfileDTO incomingDTO
    ) {
        try {
            // if a confict exist throwsi it down below
            profileService.getUserExistanceById(incomingDTO.getUser_id());
            log.info("New user registered");
            ProfileDTO apiResponse = new ProfileDTO();
            return ResponseEntity.ok(apiResponse);
        } catch (SampleCustomException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ProfileDTO());
        }
    }

    @GetMapping(
        value = "/existing",
        produces = "application/json"
    )
    public ResponseEntity<ProfileDTO> getProfile(
        @Valid
        @RequestBody
        String user_id
    ) {
        try {
            ProfileEntity profileEntity = profileService.getUserExistanceById(user_id);
            ProfileDTO profile = ProfileMapper.INSTANCE.toDto( profileEntity );
            return ResponseEntity.ok(profile);
        } catch (SampleCustomException e) {
            // TODO: not the right response
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ProfileDTO());
        }
    }

    @PostMapping(
        value = "/existing",
        produces = "application/json",
        consumes = "application/json"
    )
    public ResponseEntity<ProfileDTO> setProfile(
        @Valid
        @RequestBody
        ProfileDTO incomingDTO
    ) {
        try {
            // TODO: verify the user modifying it owns the profile
            // Save it to the database
            profileService.updateByUserId(
                incomingDTO.getUser_id(),
                incomingDTO.getFull_name(),
                incomingDTO.getBio(),
                incomingDTO.getProfile_pic()
            );
            // return modified version
            return ResponseEntity.ok(incomingDTO);
        } catch (SampleCustomException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ProfileDTO());
        }
    }
}
