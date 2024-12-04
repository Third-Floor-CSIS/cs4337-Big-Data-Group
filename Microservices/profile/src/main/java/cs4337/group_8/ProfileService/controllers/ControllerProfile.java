package cs4337.group_8.ProfileService.controllers;

import cs4337.group_8.ProfileService.DTO.ProfileDTO;
import cs4337.group_8.ProfileService.exceptions.SampleCustomException;
import cs4337.group_8.ProfileService.mappers.ProfileMapper;
import cs4337.group_8.ProfileService.services.JwtService;
import cs4337.group_8.ProfileService.services.ProfileService;
import cs4337.group_8.ProfileService.entities.ProfileEntity;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
// Simple logging interface: meaning we can plug in and out different logging implementations. By Default its the Springboot logger
@Slf4j
public class ControllerProfile {

    // Bean injections
    // It is okay to have more than one service, but nothing lower
    private final ProfileService profileService;
    private final JwtService jwtService;
    public ControllerProfile(ProfileService profileService,
                             JwtService jwtService) {
        this.profileService = profileService;
        this.jwtService = jwtService;
    }

    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

    @GetMapping(
        value = "/existing",
        produces = "application/json"
    )
    public ResponseEntity<ProfileEntity> getProfile(
        @RequestHeader("Authorization") String jwtHeader
    ) {
        try {
            // Preprocessing jwt token
            String jwtToken = jwtHeader.substring(7);
            // Extracting user_id from jwt token
            String userId = jwtService.extractUserId(jwtToken);
            String accessToken = jwtService.extractAccessToken(jwtToken);

            ProfileEntity profileEntity = profileService.createNewUserIfNotExists(userId, accessToken);
            return ResponseEntity.ok(profileEntity);
        } catch (SampleCustomException e) {
            // TODO: not the right response
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ProfileEntity());
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
            // Save it to the database
            profileService.updateByUserId(incomingDTO);
            // return modified version
            return ResponseEntity.ok(incomingDTO);
        } catch (SampleCustomException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ProfileDTO());
        }
    }

    @PostMapping("/follow/{user_id}")
    public ResponseEntity<Object> followUser(
        @PathVariable("user_id") String targetId,
        @RequestHeader("Authorization") String jwtHeader
    ) {
        try {
            // Preprocessing jwt token
            String jwtToken = jwtHeader.substring(7);
            // Extracting user_id from jwt token
            String initiatiorId = jwtService.extractUserId(jwtToken);
            String accessToken = jwtService.extractAccessToken(jwtToken);
            profileService.validateUserExists(initiatiorId, accessToken);
            profileService.validateTargetExists(targetId);
            ProfileEntity initiator = profileService.applyFollow(initiatiorId, targetId);
            return ResponseEntity.ok().body(initiator);
        } catch (SampleCustomException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ProfileDTO());
        }
    }
}
