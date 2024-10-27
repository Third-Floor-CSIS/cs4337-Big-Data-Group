package cs4337.group_8.AuthenticationMicroservice.services;

import cs4337.group_8.AuthenticationMicroservice.DTOs.UserDTO;
import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleAuthorizationResponse;
import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleUserDetails;
import cs4337.group_8.AuthenticationMicroservice.entities.TokenEntity;
import cs4337.group_8.AuthenticationMicroservice.entities.UserEntity;
import cs4337.group_8.AuthenticationMicroservice.exceptions.RefreshTokenExpiredException;
import cs4337.group_8.AuthenticationMicroservice.exceptions.ValidateTokenException;
import cs4337.group_8.AuthenticationMicroservice.repositories.TokenRepository;
import cs4337.group_8.AuthenticationMicroservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class AuthenticationService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final GoogleAuthService googleAuthService;
    private static final long DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
    private static final long SECOND_IN_MILLISECONDS = 1000;

    public AuthenticationService(
            TokenRepository tokenRepository,
            UserRepository userRepository,
            GoogleAuthService googleAuthService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.googleAuthService = googleAuthService;
    }

    public UserDTO handleAuthentication(String grantCode) {
        GoogleAuthorizationResponse apiResponse = googleAuthService.getOauthInformationFromGrantCode(grantCode);
        String accessToken = apiResponse.getAccess_token();
        GoogleUserDetails userDetails = googleAuthService.getGoogleProfileDetails(accessToken);
        UserEntity user = userRepository.findByEmail(userDetails.getEmail())
                .orElseGet(() -> createNewUserFromGoogleOauth(userDetails));

        storeTokens(apiResponse, user.getUser_id());

        UserDTO userDto = new UserDTO();
        userDto.setUserId(user.getUser_id());
        userDto.setProfilePicture(user.getProfile_picture());
        return userDto;
    }

    public String refreshToken(String accessToken) throws RefreshTokenExpiredException {
        boolean refreshTokenIsInvalid = googleAuthService.isRefreshTokenExpiredForAccessToken(accessToken);
        if (!refreshTokenIsInvalid) {
            TokenEntity tokenEntity = tokenRepository.getTokenEntityByCurrentAccessToken(accessToken)
                    .orElseThrow(() -> new ValidateTokenException("No token found for user, please login again"));

            String refreshToken = tokenEntity.getRefreshToken();

            accessToken = googleAuthService.refreshAccessToken(refreshToken, tokenEntity.getUserId());
            return accessToken;
        }

        throw new RefreshTokenExpiredException("Refresh token has expired, please login again");
    }

    private UserEntity createNewUserFromGoogleOauth(GoogleUserDetails userDetails) {
        UserEntity createdUser = new UserEntity();
        createdUser.setEmail(userDetails.getEmail());
        createdUser.setFull_name(userDetails.getName());
        createdUser.setProfile_picture(userDetails.getPicture());
        createdUser.setUsername(null);
        createdUser.setPassword(null);
        createdUser.setBio("");
        createdUser.setFollower_count(0);
        createdUser.setFollowing_count(0);
        return userRepository.save(createdUser);
    }

    private void storeTokens(GoogleAuthorizationResponse tokenDetails, int userId) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserId(userId);
        tokenEntity.setCurrentAccessToken(tokenDetails.getAccess_token());
        tokenEntity.setExpirationTimeAccessToken(new Timestamp(System.currentTimeMillis() + (SECOND_IN_MILLISECONDS * tokenDetails.getExpires_in())));
        tokenEntity.setRefreshToken(tokenDetails.getRefresh_token());
        tokenEntity.setExpirationTimeRefreshToken(new Timestamp(System.currentTimeMillis() + (DAY_IN_MILLISECONDS * 14)));
        tokenRepository.save(tokenEntity);
    }
}
