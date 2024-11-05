package cs4337.group_8.AuthenticationMicroservice.controllers;

import cs4337.group_8.AuthenticationMicroservice.DTOs.UserDTO;
import cs4337.group_8.AuthenticationMicroservice.exceptions.AuthenticationException;
import cs4337.group_8.AuthenticationMicroservice.exceptions.AuthenticationNotFoundException;
import cs4337.group_8.AuthenticationMicroservice.exceptions.RefreshTokenExpiredException;
import cs4337.group_8.AuthenticationMicroservice.services.AuthenticationService;
import cs4337.group_8.AuthenticationMicroservice.services.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService,
                                    JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @GetMapping("/test")
    public ResponseEntity test() {
        return new ResponseEntity<>("Test", HttpStatus.OK);
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

    @PostMapping("/refresh-access-token")
    public ResponseEntity<Object> refreshToken(@RequestHeader("Authorization") String jwtToken) {
        try {
            jwtToken = jwtToken.substring(7);
            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(authenticationService.refreshAccessToken(jwtToken));
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
        } catch (AuthenticationNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/refresh-jwt-token")
    public ResponseEntity<Object> refreshJwtToken(@RequestHeader("Authorization") String jwtToken) {
        try {
            jwtToken = jwtToken.substring(7);
            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(jwtService.refreshJwtToken(jwtToken));

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
        } catch (JwtException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
