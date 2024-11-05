package cs4337.group_8.AuthenticationMicroservice.services;

import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleAuthorizationResponse;
import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleUserDetails;
import cs4337.group_8.AuthenticationMicroservice.entities.GoogleResourceTokenEntity;
import cs4337.group_8.AuthenticationMicroservice.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.List;

public class SharedResources {
    public SharedResources() {
    }

    GoogleResourceTokenEntity getMockTokenEntity() {
        GoogleResourceTokenEntity mockGoogleResourceTokenEntity = new GoogleResourceTokenEntity();
        mockGoogleResourceTokenEntity.setUserId(1);
        mockGoogleResourceTokenEntity.setRefreshToken("mockRefreshToken");
        mockGoogleResourceTokenEntity.setCurrentAccessToken("mockAccessToken");
        return mockGoogleResourceTokenEntity;
    }

    GoogleAuthorizationResponse mockGoogleAuthorizationResponse() {
        GoogleAuthorizationResponse mockApiResposne = new GoogleAuthorizationResponse();
        mockApiResposne.setAccess_token("mockAccessToken");
        mockApiResposne.setRefresh_token("mockRefreshToken");
        mockApiResposne.setExpires_in(3600);
        return mockApiResposne;
    }

    GoogleUserDetails mockGoogleUserDetails() {
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

    UserEntity getMockUserEntity() {
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

    UserDetails getMockUserDetails() {
        return new org.springframework.security.core.userdetails.User(
                "mockUser",
                "",
                List.of()
        );
    }
}