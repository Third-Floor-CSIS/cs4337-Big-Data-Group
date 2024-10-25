package cs4337.group_8.TemplateProject.controllers;

import cs4337.group_8.TemplateProject.DTO.SampleDTO;
import cs4337.group_8.TemplateProject.DTO.ValidatedSampleDTO;
import cs4337.group_8.TemplateProject.exceptions.SampleCustomException;
import cs4337.group_8.TemplateProject.services.SampleService;
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
public class SampleController {

    // Bean injections
    // It is okay to have more than one service, but nothing lower
    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @PostMapping(
        value = "/register",
        produces = "application/json",
        consumes = "application/json"
    )
    public ResponseEntity<SampleDTO> addNewUserFromRegistration(@Valid @RequestBody ValidatedSampleDTO incomingDTO) {
        try {
            sampleService.getUserExistanceById(incomingDTO.getId());
            log.info("New user registered");
            SampleDTO apiResponse = new SampleDTO("property1");
            return ResponseEntity.ok(apiResponse);
        } catch (SampleCustomException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SampleDTO(e.getMessage()));
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
