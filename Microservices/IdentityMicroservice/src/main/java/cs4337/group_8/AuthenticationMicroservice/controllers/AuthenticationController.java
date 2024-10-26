package cs4337.group_8.AuthenticationMicroservice.controllers;

import cs4337.group_8.AuthenticationMicroservice.DTOs.UserDTO;
import cs4337.group_8.AuthenticationMicroservice.exceptions.AuthenticationException;
import cs4337.group_8.AuthenticationMicroservice.exceptions.RefreshTokenExpiredException;
import cs4337.group_8.AuthenticationMicroservice.exceptions.ValidateTokenException;
import cs4337.group_8.AuthenticationMicroservice.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
            UserDTO user = authenticationService.handleAuthentication(code);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            token = token.substring(7);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authenticationService.refreshToken(token));
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body("Token refreshed");
        } catch (RefreshTokenExpiredException e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/login"));
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .headers(headers)
                    .body("Refresh token expired");
        }
    }
}
