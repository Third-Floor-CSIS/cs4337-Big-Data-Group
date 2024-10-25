package cs4337.group_8.AuthenticationMicroservice.controllers;

import cs4337.group_8.AuthenticationMicroservice.DTOs.RegistrationDTO;
import cs4337.group_8.AuthenticationMicroservice.exceptions.AuthenticationException;
import cs4337.group_8.AuthenticationMicroservice.exceptions.ValidateTokenException;
import cs4337.group_8.AuthenticationMicroservice.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/grantcode")
    public ResponseEntity grantCode(@RequestParam("code") String code,
                                    @RequestParam("scope") String scope,
                                    @RequestParam("authuser") String authUser,
                                    @RequestParam("prompt") String prompt) {
        try {
            return authenticationService.handleAuthentication(code);
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegistrationDTO registrationForm) {
        String email = registrationForm.getEmail();
        if (authenticationService.doesEmailExist(email)) {
            return ResponseEntity
                    .badRequest()
                    .body("Email already exists");
        }

        return ResponseEntity
                .badRequest()
                .body("Can't register user: No User Microservice");
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Object> validateToken(@RequestHeader("Authorization") String token, @RequestParam("userId") int userId) {
        try {
            System.out.println("Validating token: " + token);
            token = token.substring(7);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authenticationService.refreshToken(token, userId));
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body("Token is valid");
        } catch (ValidateTokenException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
}
