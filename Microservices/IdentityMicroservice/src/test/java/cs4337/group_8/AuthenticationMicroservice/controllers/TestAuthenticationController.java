package cs4337.group_8.AuthenticationMicroservice.controllers;

import cs4337.group_8.AuthenticationMicroservice.DTOs.UserDTO;
import cs4337.group_8.AuthenticationMicroservice.exceptions.AuthenticationException;
import cs4337.group_8.AuthenticationMicroservice.exceptions.RefreshTokenExpiredException;
import cs4337.group_8.AuthenticationMicroservice.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@ContextConfiguration(classes = {TestAuthenticationController.SecurityTestConfig.class})
public class TestAuthenticationController {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    // Disable security config for testing
    @TestConfiguration
    @EnableWebSecurity
    static class SecurityTestConfig {
        @Bean
        @Primary
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
                    request -> request.requestMatchers("*").permitAll()
            );
            return http.build();
        }
    }

    @Test
    public void grantCode_shouldReturnUserDTO_whenAuthenticationIsSuccessful() throws Exception {
        // Incoming properties
        String code = "some code";
        String scope = "some scope";
        String authUser = "some user";
        String prompt = "some prompt";

        UserDTO expectedUser = new UserDTO();
        expectedUser.setUserId(1);
        expectedUser.setProfilePicture("some link to a pfp");

        when(authenticationService.handleAuthentication(code))
                .thenReturn(expectedUser);

        mockMvc.perform(get("/grantcode")
                        .param("code", code)
                        .param("scope", scope)
                        .param("authuser", authUser)
                        .param("prompt", prompt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(expectedUser.getUserId()))
                .andExpect(jsonPath("$.profilePicture").value(expectedUser.getProfilePicture()));
    }

    @Test
    public void grantCode_shouldReturnBadRequest_whenAuthenticationFails() throws Exception {
        // Incoming properties
        String code = "invalid code";
        String scope = "some scope";
        String authUser = "some user";
        String prompt = "some prompt";

        String errorMsg = "Authentication failed";
        when(authenticationService.handleAuthentication(code))
                .thenThrow(new AuthenticationException(errorMsg));

        mockMvc.perform(get("/grantcode")
                        .param("code", code)
                        .param("scope", scope)
                        .param("authuser", authUser)
                        .param("prompt", prompt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(errorMsg));
    }

    @Test
    public void refreshToken_shouldReturnOk_whenTokenIsRefreshed() throws Exception {
        // Init variables
        String requestHeaderToken = "Bearer someToken";
        String trimmedToken = requestHeaderToken.substring(7);
        String refreshedToken = "refreshedToken";

        when(authenticationService.refreshAccessToken(trimmedToken))
                .thenReturn(refreshedToken);

        mockMvc.perform(post("/refresh-token")
                        .header("Authorization", requestHeaderToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Token refreshed"))
                .andExpect(header().string("Authorization", "Bearer " + refreshedToken));

    }

    @Test
    public void refreshToken_shouldReturn403Forbidden_whenRefreshTokenIsExpired() throws Exception {
        // Init variables
        String requestHeaderToken = "Bearer someToken";
        String trimmedToken = requestHeaderToken.substring(7);

        when(authenticationService.refreshAccessToken(trimmedToken))
                .thenThrow(new RefreshTokenExpiredException("Refresh token expired"));

        mockMvc.perform(post("/refresh-token").header("Authorization", requestHeaderToken))
                .andExpect(status().isForbidden())
                .andExpect(header().string("Location", "/login"))
                .andExpect(content().string("Refresh token expired"));
    }


}
