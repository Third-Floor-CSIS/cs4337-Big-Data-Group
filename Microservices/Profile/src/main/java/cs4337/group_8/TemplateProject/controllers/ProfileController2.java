package cs4337.group_8.TemplateProject.controllers;

import cs4337.group_8.TemplateProject.DTO.ProfileDTO;
import cs4337.group_8.TemplateProject.DTO.ProfileDTOValidated;
import cs4337.group_8.TemplateProject.exceptions.SampleCustomException;
import cs4337.group_8.TemplateProject.services.ProfileService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/profile")
// Simple logging interface: meaning we can plug in and out different logging implementations. By Default its the Springboot logger
@Slf4j
public class ProfileController2 {

    // Bean injections
    // It is okay to have more than one service, but nothing lower
    private final ProfileService ProfileService;

    public ProfileController2(ProfileService ProfileService) {
        this.ProfileService = ProfileService;
    }

    @PostMapping(
        value = "/register",
        produces = "application/json",
        consumes = "application/json"
    )
    public ResponseEntity<ProfileDTO> addNewUserFromRegistration(@Valid @RequestBody ProfileDTOValidated incomingDTO) {
        try {
            ProfileService.getUserExistanceById(incomingDTO.getUser_id());
            log.info("New user registered");
            ProfileDTO apiResponse = new ProfileDTO();
            return ResponseEntity.ok(apiResponse);
        } catch (SampleCustomException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ProfileDTO());
        }
    }

    // No need to specify consumes or produces, but it is good practice
    @PostMapping(
            value = "/ok",
            produces = "application/json"
    )
    public ResponseEntity<String> loginUser() {
        return ResponseEntity.ok("Hello, World!");
    }

    @GetMapping(value = "/get", produces = "application/json")
    public ResponseEntity<HashMap<String, Object>> getSample() {
        HashMap<String, Object> response = new HashMap<>();
        response.put("key", "value");
        response.put("name", "milan");
        response.put("age", 22);
        response.put("how are you", "good");
        // You can also just return response, but ResponseEntity is more explicit
        return ResponseEntity.ok(response);
    }
}
