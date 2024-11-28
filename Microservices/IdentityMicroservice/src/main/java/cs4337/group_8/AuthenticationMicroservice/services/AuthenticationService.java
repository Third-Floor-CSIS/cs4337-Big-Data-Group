package cs4337.group_8.AuthenticationMicroservice.services;

import cs4337.group_8.AuthenticationMicroservice.DTOs.UserDTO;
import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleAuthorizationResponse;
import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleUserDetails;
import cs4337.group_8.AuthenticationMicroservice.entities.GoogleResourceTokenEntity;
import cs4337.group_8.AuthenticationMicroservice.entities.UserEntity;
import cs4337.group_8.AuthenticationMicroservice.exceptions.AuthenticationNotFoundException;
import cs4337.group_8.AuthenticationMicroservice.exceptions.RefreshTokenExpiredException;
import cs4337.group_8.AuthenticationMicroservice.exceptions.ValidateTokenException;
import cs4337.group_8.AuthenticationMicroservice.repositories.TokenRepository;
import cs4337.group_8.AuthenticationMicroservice.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class AuthenticationService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final GoogleAuthService googleAuthService;
    private final JwtService jwtService;

    public AuthenticationService(
            TokenRepository tokenRepository,
            UserRepository userRepository,
            GoogleAuthService googleAuthService,
            JwtService jwtService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.googleAuthService = googleAuthService;
        this.jwtService = jwtService;
    }

    public UserDTO handleAuthentication(String grantCode) {
        GoogleAuthorizationResponse apiResponse = googleAuthService.getOauthInformationFromGrantCode(grantCode);
        String accessToken = apiResponse.getAccess_token();
        GoogleUserDetails userDetails = googleAuthService.getGoogleProfileDetails(accessToken);
        UserEntity user = userRepository.findByEmail(userDetails.getEmail())
                .orElseGet(() -> createNewUserFromGoogleOauth(userDetails));

        storeTokens(apiResponse, user.getUser_id());

        String jwtToken = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getUser_id()),
                "",
                List.of(new SimpleGrantedAuthority("USER"))
        ), accessToken);

        UserDTO userDto = new UserDTO();
        userDto.setUserId(user.getUser_id());
        userDto.setJwtToken(jwtToken);

        return userDto;
    }

    public String refreshAccessToken(String jwtToken) throws RefreshTokenExpiredException {

        Claims jwtClaims = jwtService.extractAllClaims(jwtToken);

        String accessToken;
        String userId;

        if (jwtClaims.get("access_token") != null && jwtClaims.get("userID") != null) {
            accessToken = (String) jwtClaims.get("access_token");
            userId = (String) jwtClaims.get("userID");
        } else {
            throw new AuthenticationNotFoundException("Access token not found in JWT token");
        }

        try {
            String refreshToken = googleAuthService.getRefreshTokenForAccessTokenIfNotExpired(accessToken);

            accessToken = googleAuthService.refreshAccessToken(refreshToken, Integer.parseInt(userId));

            String newJwt = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
                    userId,
                    "",
                    List.of(new SimpleGrantedAuthority("USER"))
            ), accessToken);

            return newJwt;
        } catch (ValidateTokenException e) {
            throw new AuthenticationNotFoundException("No token found for user, please login again");
        }

    }

    private UserEntity createNewUserFromGoogleOauth(GoogleUserDetails userDetails) {
        UserEntity createdUser = new UserEntity();
        createdUser.setEmail(userDetails.getEmail());
        createdUser.setPassword(null);


        createdUser = userRepository.save(createdUser);
        jwtService.generateRefreshToken(new org.springframework.security.core.userdetails.User(
                String.valueOf(createdUser.getUser_id()),
                "",
                List.of(new SimpleGrantedAuthority("USER"))
        ));
        return createdUser;
    }

    private void storeTokens(GoogleAuthorizationResponse tokenDetails, int userId) {
        GoogleResourceTokenEntity googleResourceTokenEntity = new GoogleResourceTokenEntity();
        googleResourceTokenEntity.setUserId(userId);
        googleResourceTokenEntity.setCurrentAccessToken(tokenDetails.getAccess_token());
        googleResourceTokenEntity.setExpirationTimeAccessToken(
                Instant.now().plusSeconds(tokenDetails.getExpires_in()));
        googleResourceTokenEntity.setRefreshToken(tokenDetails.getRefresh_token());
        googleResourceTokenEntity.setExpirationTimeRefreshToken(Instant.now().plus(14, ChronoUnit.DAYS));
        tokenRepository.save(googleResourceTokenEntity);
    }
}
