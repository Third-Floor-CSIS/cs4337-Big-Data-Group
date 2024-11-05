package cs4337.group_8.AuthenticationMicroservice.services;


import cs4337.group_8.AuthenticationMicroservice.DTOs.UserDTO;
import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleAuthorizationResponse;
import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleUserDetails;
import cs4337.group_8.AuthenticationMicroservice.entities.GoogleResourceTokenEntity;
import cs4337.group_8.AuthenticationMicroservice.entities.UserEntity;
import cs4337.group_8.AuthenticationMicroservice.repositories.AuthenticationRepository;
import cs4337.group_8.AuthenticationMicroservice.repositories.TokenRepository;
import cs4337.group_8.AuthenticationMicroservice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


public class TestAuthenticationService {
    private final SharedResources sharedResources = new SharedResources();
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

        GoogleAuthorizationResponse mockApiResponse = sharedResources.mockGoogleAuthorizationResponse();
        GoogleUserDetails mockUserDetails = sharedResources.mockGoogleUserDetails();
        UserEntity mockUser = sharedResources.getMockUserEntity();


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

        GoogleAuthorizationResponse mockApiResponse = sharedResources.mockGoogleAuthorizationResponse();
        GoogleUserDetails mockUserDetails = sharedResources.mockGoogleUserDetails();
        UserEntity mockUserEntity = sharedResources.getMockUserEntity();

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
    public void refreshToken_shouldReturnNewAccessToken_whenRefreshAccessTokenIsValid() {
        String mockAccessToken = "mockAccessToken";
        String mockUpdatedAccessToken = "newAccessToken";
        GoogleResourceTokenEntity mockGoogleResourceTokenEntity = sharedResources.getMockTokenEntity();
        when(mockGoogleAuthService.getRefreshTokenForAccessTokenIfNotExpired(anyString())).thenReturn(false);
        when(mockTokenRepository.getTokenEntityByCurrentAccessToken(anyString())).thenReturn(Optional.of(mockGoogleResourceTokenEntity));
        when(mockGoogleAuthService.refreshAccessToken(mockGoogleResourceTokenEntity.getRefreshToken(), mockGoogleResourceTokenEntity.getUserId())).thenReturn(mockUpdatedAccessToken);

        String updatedAccessToken = authenticationService.refreshAccessToken(mockAccessToken);

        assertEquals(updatedAccessToken, mockUpdatedAccessToken);
    }

    @Test
    public void refreshToken_shouldThrowRefreshTokenExpiredException_whenRefreshAccessTokenIsInvalid() {
        String mockAccessToken = "mockAccessToken";

        when(mockGoogleAuthService.getRefreshTokenForAccessTokenIfNotExpired(anyString())).thenReturn(true);

        try {
            authenticationService.refreshAccessToken(mockAccessToken);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Refresh token has expired, please login again");
        }
    }

    private GoogleResourceTokenEntity getMockTokenEntity() {
        return sharedResources.getMockTokenEntity();
    }


    private GoogleAuthorizationResponse mockGoogleAuthorizationResponse() {
        return sharedResources.mockGoogleAuthorizationResponse();
    }

    protected GoogleUserDetails mockGoogleUserDetails() {
        return sharedResources.mockGoogleUserDetails();
    }

    private UserEntity getMockUserEntity() {

        return sharedResources.getMockUserEntity();
    }
}
