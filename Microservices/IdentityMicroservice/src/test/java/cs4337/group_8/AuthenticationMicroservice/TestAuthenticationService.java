package cs4337.group_8.AuthenticationMicroservice;


import cs4337.group_8.AuthenticationMicroservice.DTOs.UserDTO;
import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleAuthorizationResponse;
import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleUserDetails;
import cs4337.group_8.AuthenticationMicroservice.entities.TokenEntity;
import cs4337.group_8.AuthenticationMicroservice.entities.UserEntity;
import cs4337.group_8.AuthenticationMicroservice.repositories.AuthenticationRepository;
import cs4337.group_8.AuthenticationMicroservice.repositories.TokenRepository;
import cs4337.group_8.AuthenticationMicroservice.repositories.UserRepository;
import cs4337.group_8.AuthenticationMicroservice.services.AuthenticationService;
import cs4337.group_8.AuthenticationMicroservice.services.GoogleAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


public class TestAuthenticationService {
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationRepository mockAuthenticationRepository;
    @Mock
    private TokenRepository mockTokenRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private GoogleAuthService mockGoogleAuthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void handleAuthentication_shouldReturnUserDTO_whenAuthenticationIsValid() {
        String grantCode = "some grant code from Google";

        GoogleAuthorizationResponse mockApiResponse = mockGoogleAuthorizationResponse();
        GoogleUserDetails mockUserDetails = mockGoogleUserDetails();
        UserEntity mockUser = getMockUserEntity();


        when(mockGoogleAuthService.getOauthInformationFromGrantCode(anyString())).thenReturn(mockApiResponse);
        when(mockGoogleAuthService.getGoogleProfileDetails(anyString())).thenReturn(mockUserDetails);
        when(mockUserRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        UserDTO userResponse = authenticationService.handleAuthentication(grantCode);
        assertEquals(userResponse.getUserId(), mockUser.getUser_id());
        assertEquals(userResponse.getProfilePicture(), mockUser.getProfile_picture());
    }

    @Test
    public void handleAuthentication_shouldCreateNewUser_whenUserDoesNotExists() throws Exception {
        String grantCode = "some grant code from Google";

        GoogleAuthorizationResponse mockApiResponse = mockGoogleAuthorizationResponse();
        GoogleUserDetails mockUserDetails = mockGoogleUserDetails();
        UserEntity mockUserEntity = getMockUserEntity();

        when(mockGoogleAuthService.getOauthInformationFromGrantCode(anyString())).thenReturn(mockApiResponse);
        when(mockGoogleAuthService.getGoogleProfileDetails(anyString())).thenReturn(mockUserDetails);
        when(mockUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(mockTokenRepository.save(any())).thenReturn(null);
        when(mockUserRepository.save(any())).thenReturn(mockUserEntity);

        UserDTO userResponse = authenticationService.handleAuthentication(grantCode);
        assertEquals(userResponse.getUserId(), mockUserEntity.getUser_id());
        assertEquals(userResponse.getProfilePicture(), mockUserEntity.getProfile_picture());
    }

    @Test
    public void refreshToken_shouldReturnNewAccessToken_whenRefreshTokenIsValid() {
        String mockAccessToken = "mockAccessToken";
        String mockUpdatedAccessToken = "newAccessToken";
        TokenEntity mockTokenEntity = getMockTokenEntity();
        when(mockGoogleAuthService.isRefreshTokenExpiredForAccessToken(anyString())).thenReturn(false);
        when(mockTokenRepository.getTokenEntityByCurrentAccessToken(anyString())).thenReturn(Optional.of(mockTokenEntity));
        when(mockGoogleAuthService.refreshAccessToken(mockTokenEntity.getRefreshToken(), mockTokenEntity.getUserId())).thenReturn(mockUpdatedAccessToken);

        String updatedAccessToken = authenticationService.refreshToken(mockAccessToken);

        assertEquals(updatedAccessToken, mockUpdatedAccessToken);
    }

    @Test
    public void refreshToken_shouldThrowRefreshTokenExpiredException_whenRefreshTokenIsInvalid() {
        String mockAccessToken = "mockAccessToken";

        when(mockGoogleAuthService.isRefreshTokenExpiredForAccessToken(anyString())).thenReturn(true);

        try {
            authenticationService.refreshToken(mockAccessToken);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Refresh token has expired, please login again");
        }
    }

    private TokenEntity getMockTokenEntity() {
        TokenEntity mockTokenEntity = new TokenEntity();
        mockTokenEntity.setUserId(1);
        mockTokenEntity.setRefreshToken("mockRefreshToken");
        mockTokenEntity.setCurrentAccessToken("mockAccessToken");
        return mockTokenEntity;
    }


    private GoogleAuthorizationResponse mockGoogleAuthorizationResponse() {
        GoogleAuthorizationResponse mockApiResposne = new GoogleAuthorizationResponse();
        mockApiResposne.setAccess_token("mockAccessToken");
        mockApiResposne.setRefresh_token("mockRefreshToken");
        mockApiResposne.setExpires_in(3600);
        return mockApiResposne;
    }

    protected GoogleUserDetails mockGoogleUserDetails() {
        GoogleUserDetails mockUserDetails = new GoogleUserDetails();
        mockUserDetails.setId("1");
        mockUserDetails.setName("firstname lastname");
        mockUserDetails.setEmail("mock@email.com");
        mockUserDetails.setFamily_name("lastname");
        mockUserDetails.setGiven_name("firstname");
        mockUserDetails.setPicture("picture URL");
        mockUserDetails.setVerified_email(true);
        return mockUserDetails;
    }

    private UserEntity getMockUserEntity() {
        UserEntity mockUser = new UserEntity();
        mockUser.setUser_id(1);

        mockUser.setUsername(null);
        mockUser.setPassword(null);
        mockUser.setEmail("mock@email.com");
        mockUser.setFull_name("firstname lastname");
        mockUser.setBio("");

        mockUser.setProfile_picture("picture URL");
        mockUser.setFollower_count(0);
        mockUser.setFollowing_count(0);

        mockUser.setCreated_at(new Timestamp(2024, 1, 1, 0, 0, 0, 0));
        return mockUser;
    }
}
