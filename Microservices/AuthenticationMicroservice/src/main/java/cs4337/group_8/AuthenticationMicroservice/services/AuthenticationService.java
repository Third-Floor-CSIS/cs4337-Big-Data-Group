package cs4337.group_8.AuthenticationMicroservice.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs4337.group_8.AuthenticationMicroservice.DTOs.UserDTO;
import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleAuthorizationResponse;
import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleUserDetails;
import cs4337.group_8.AuthenticationMicroservice.entities.RefreshTokenEntity;
import cs4337.group_8.AuthenticationMicroservice.entities.UserEntity;
import cs4337.group_8.AuthenticationMicroservice.exceptions.AuthenticationException;
import cs4337.group_8.AuthenticationMicroservice.exceptions.ValidateTokenException;
import cs4337.group_8.AuthenticationMicroservice.mappers.TokenMapper;
import cs4337.group_8.AuthenticationMicroservice.repositories.AuthenticationRepository;
import cs4337.group_8.AuthenticationMicroservice.repositories.TokenRepository;
import cs4337.group_8.AuthenticationMicroservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Date;

@Service
@Slf4j
public class AuthenticationService {
    private final String clientId;
    private final String clientSecret;
    private final AuthenticationRepository authenticationRepository;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private static final long DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
    private static final long SECOND_IN_MILLISECONDS = 1000;

    public AuthenticationService(@Value("${CLIENT_SECRET}") String clientSecret,
                                 @Value("${CLIENT_ID}") String clientId,
                                 AuthenticationRepository authenticationRepository, TokenRepository tokenRepository, UserRepository userRepository) {
        this.clientSecret = clientSecret;
        this.clientId = clientId;
        this.authenticationRepository = authenticationRepository;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<UserEntity> handleAuthentication(String grantCode) {
        GoogleAuthorizationResponse apiResponse = getOauthInformationFromGrantCode(grantCode);
        String accessToken = apiResponse.getAccess_token();
        String refreshToken = apiResponse.getRefresh_token();

        GoogleUserDetails userDetails = getGoogleProfileDetails(accessToken);
        UserEntity user = userRepository.findByEmail(userDetails.getEmail())
                .orElseGet(() -> createNewUserFromGoogleOauth(userDetails));

        storeRefreshToken(refreshToken, user.getUser_id());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        UserDTO userDto = new UserDTO();
        userDto.setUserId(user.getUser_id());
        userDto.setProfilePicture(user.getProfile_picture());
        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }

    public boolean doesEmailExist(String email) {
        return authenticationRepository.findByEmail(email).isPresent();
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

    private void storeRefreshToken(String refreshToken, Integer userId) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUserId(userId);
        refreshTokenEntity.setCurrentAccessToken(null);
        refreshTokenEntity.setExpirationTimeAccessToken(new Timestamp(System.currentTimeMillis() + (SECOND_IN_MILLISECONDS * 0)));
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setExpirationTimeRefreshToken(new Timestamp(System.currentTimeMillis() + (DAY_IN_MILLISECONDS * 14)));
        tokenRepository.save(refreshTokenEntity);
    }

    public String refreshToken(String accessToken, int userId) throws ValidateTokenException {
        boolean isTokenExpired = isTokenExpired(accessToken);
        if (isTokenExpired) {
            // Try and refresh the token
            String refreshToken = tokenRepository.getTokenEntityByUserId(userId)
                    .orElseThrow(() -> new ValidateTokenException("No refresh token found for user"))
                    .getRefreshToken();

            GoogleAuthorizationResponse updatedData = refreshAccessToken(refreshToken, userId);
            accessToken = updatedData.getAccess_token();
        }
        return accessToken;
    }

    private GoogleAuthorizationResponse refreshAccessToken(String refreshToken, int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // The content type will contain the form data in the URL

        MultiValueMap<String, String> params = getPayloadForGoogleRefresh(refreshToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> responseFromGoogle = requestRefreshToken(requestEntity);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseFromGoogle.getBody());
            GoogleAuthorizationResponse tokenDetails = objectMapper.treeToValue(jsonNode, GoogleAuthorizationResponse.class);

            RefreshTokenEntity refreshTokenEntity = TokenMapper.INSTANCE.toTokenEntity(tokenDetails);
            refreshTokenEntity.setUserId(userId);
            tokenRepository.save(refreshTokenEntity);

            return tokenDetails;
        } catch (JsonProcessingException e) {
            throw new AuthenticationException("Failed to refresh access token");
        }
    }

    private MultiValueMap<String, String> getPayloadForGoogleRefresh(String refreshToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", this.clientId);
        params.add("client_secret", this.clientSecret);
        params.add("refresh_token", refreshToken);
        params.add("grant_type", "refresh_token");
        return params;
    }

    private ResponseEntity<String> requestRefreshToken(HttpEntity<MultiValueMap<String, String>> requestPayload) {
        RestTemplate restTemplate = new RestTemplate();
        String googleRefreshTokenURL = "https://oauth2.googleapis.com/token";
        return restTemplate.postForEntity(googleRefreshTokenURL, requestPayload, String.class);
    }

    private boolean isTokenExpired(String accessToken) {
        try {
            Date expirationTime = JWT.decode(accessToken).getExpiresAt();
            return expirationTime.before(new Date());
        } catch (JWTDecodeException e) {
            log.error("Invalid Access token provided: " + e.getMessage());
            return true;
        }
    }

    private GoogleUserDetails getGoogleProfileDetails(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            GoogleUserDetails authenticatedUser = objectMapper.treeToValue(jsonNode, GoogleUserDetails.class);
            return authenticatedUser;
        } catch (JsonProcessingException e) {
            throw new AuthenticationException("Failed to parse provided user details");
        }
    }

    private GoogleAuthorizationResponse getOauthInformationFromGrantCode(String grantCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        setInformationParameters(grantCode, params);


        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            String url = "https://oauth2.googleapis.com/token";
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            GoogleAuthorizationResponse userTokenDetails = objectMapper.treeToValue(jsonNode, GoogleAuthorizationResponse.class);
            return userTokenDetails;
        } catch (JsonProcessingException e) {
            throw new AuthenticationException("Failed to get access token");
        } catch (HttpClientErrorException e) {
            throw new AuthenticationException("Invalid grant code");
        }

    }

    private void setInformationParameters(String code, MultiValueMap<String, String> params) {
        // User authorization code
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8082/grantcode");
        params.add("client_id", this.clientId);
        params.add("client_secret", this.clientSecret);

        // Information we retrieve
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
        params.add("scope", "openid");
        params.add("grant_type", "authorization_code");
    }


}
