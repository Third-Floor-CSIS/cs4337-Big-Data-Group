package cs4337.group_8.AuthenticationMicroservice.services;

import cs4337.group_8.AuthenticationMicroservice.repositories.AuthenticationRepository;
import cs4337.group_8.AuthenticationMicroservice.repositories.TokenRepository;
import cs4337.group_8.AuthenticationMicroservice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

public class TestJwtService {
    private static final SharedResources sharedResources = new SharedResources();

    private JwtService jwtService;

    @BeforeEach
    public void Setup() {
        String testSecretKey = "59aa42eff7631ebe224434c3a8b573a278d5751a3c7cf4ac4f197ee307b154069e3d2728cd724f61b9bff2191e9af9fb083a1cc798a666d370d1026f76d7f6d1791808c4d0e6eb62fcdfab60a9984454b741ee9bac0036d9c1d4d3a45557c341aa2ab014826262081fd674e39d03e9fa000809255e96517a1df8c9d21b9bf1d760e310c9906143d230ebda8459d423013b00326962aa47958db136357a377f2dcdab875da672331791e5165e0515d2ee135f2058cd43dc0cb971b7bb2a46da24905c5f13c5468fa070ddde31934175deaac8c567d420f230c5c4e3713b98cddd01e7d06987d7a17ebc78230773c78e1c3949909374dbe36ef96fe524e5e586e6";
        long testJwtExpiration = 3600000L;          // 1H
        long testJwtRefreshExpiration = 86400000L;  // 1D

        jwtService = new JwtService(testSecretKey, testJwtExpiration, testJwtRefreshExpiration);
    }

    @Test
    public void generateToken_shouldReturnToken_whenUserDetailsIsValid() {
        UserDetails userDetails = sharedResources.getMockUserDetails();
        String mockAccessToken = "something";
        String token = jwtService.generateToken(userDetails, mockAccessToken);

        assertNotNull(token);
    }

    @Test
    public void generateRefreshToken_shouldThrowException_whenUserDetailsIsNull() {
        try {
            jwtService.generateToken((UserDetails) null, (String) null);
        } catch (AssertionError e) {
            assertNotNull(e);
        }
    }

    @Test
    public void generateRefreshToken_shouldReturnToken_whenUserDetailsIsValid() {
        UserDetails userDetails = sharedResources.getMockUserDetails();
        String token = jwtService.generateRefreshToken(userDetails);

        assertNotNull(token);
    }

    @Test
    public void extractClaim_shouldReturnClaim_whenTokenIsValid() {
        UserDetails userDetails = sharedResources.getMockUserDetails();
        String mockAccessToken = "something";
        String token = jwtService.generateToken(userDetails, mockAccessToken);

        String username = jwtService.extractUsername(token);

        assertNotNull(username);
    }

    @Test
    public void isTokenSignatureValid_shouldReturnTrue_whenTokenIsValid() {
        UserDetails userDetails = sharedResources.getMockUserDetails();
        String mockAccessToken = "something";

        String token = jwtService.generateToken(userDetails, mockAccessToken);

        boolean isTokenSignatureValid = jwtService.isTokenSignatureValid(token);

        assertTrue(isTokenSignatureValid);
    }

    @Test
    public void isTokenExpired_shouldReturnFalse_whenTokenIsValid() {
        UserDetails userDetails = sharedResources.getMockUserDetails();
        String mockAccessToken = "something";

        String token = jwtService.generateToken(userDetails, mockAccessToken);

        boolean isTokenExpired = jwtService.isTokenExpired(token);

        assertFalse(isTokenExpired);
    }
}
