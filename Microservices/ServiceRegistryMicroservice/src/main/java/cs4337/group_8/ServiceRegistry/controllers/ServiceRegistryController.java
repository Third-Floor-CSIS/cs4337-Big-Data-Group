package cs4337.group_8.ServiceRegistry.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/sample-root-endpoint")
@Slf4j // Simple logging interface: meaning we can plug in and out different logging implementations. By Default its the Springboot logger
public class ServiceRegistryController {




    @PostMapping(value = "/ok", produces = "application/json") // No need to specify consumes or produces, but it is good practice
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
