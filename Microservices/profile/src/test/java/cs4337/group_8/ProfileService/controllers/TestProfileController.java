package cs4337.group_8.ProfileService.controllers;

import cs4337.group_8.ProfileService.DTO.ProfileDTO;
import cs4337.group_8.ProfileService.entities.ProfileEntity;
import cs4337.group_8.ProfileService.exceptions.SampleCustomException;
import cs4337.group_8.ProfileService.mappers.ProfileMapper;
import cs4337.group_8.ProfileService.services.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(ControllerProfile.class)
@ContextConfiguration(classes = {TestProfileController.SecurityTestConfig.class})
public class TestProfileController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @TestConfiguration
    @EnableWebSecurity
    static class SecurityTestConfig {
        @Bean
        @Primary
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(request -> request.anyRequest().permitAll());
            return http.build();
        }
    }

    @BeforeEach
    void setupMocks() {
        // Configure default behavior for service mock
    }

    @Test
    public void testEndpoint_shouldReturnHelloWorld() throws Exception {
        mockMvc.perform(get("/profile/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }
    @Test
    public void getProfile_shouldReturnProfile_whenUserExists() throws Exception {
        ProfileEntity mockEntity = new ProfileEntity();
        mockEntity.setUser_id("123");
        mockEntity.setFull_name("John Doe");
        mockEntity.setBio("Hello, world");
        mockEntity.setProfile_pic("https://example.com/pic.jpg");
        mockEntity.setCount_follower(10);
        mockEntity.setCount_following(5);

        when(profileService.getUserExistanceById(anyString())).thenReturn(mockEntity);
        when(ProfileMapper.INSTANCE.toDto(mockEntity)).thenReturn(new ProfileDTO());

        mockMvc.perform(get("/profile/existing")
                        .content("123")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    public void addNewUser_shouldReturnOk_whenProfileIsValid() throws Exception {
        mockMvc.perform(post("/profile/new")
                        .content("""
                                {
                                    "user_id": "123",
                                    "full_name": "John Doe",
                                    "bio": "Hello, I am John",
                                    "profile_pic": "https://example.com/pic.jpg",
                                    "count_follower": 0,
                                    "count_following": 0
                                }
                                """)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
    @Test
    public void getProfile_shouldReturnConflict_whenUserDoesNotExist() throws Exception {
        when(profileService.getUserExistanceById(Mockito.anyString()))
                .thenThrow(new SampleCustomException("User not found"));

        mockMvc.perform(get("/profile/existing")
                        .content("123")
                        .contentType("application/json"))
                .andExpect(status().isConflict());
    }

    @Test
    public void addNewUser_shouldReturnConflict_whenUserAlreadyExists() throws Exception {
        when(profileService.getUserExistanceById(anyString()))
                .thenThrow(new SampleCustomException("User already exists"));

        mockMvc.perform(post("/profile/new")
                        .content("""
                                {
                                    "user_id": "123",
                                    "full_name": "John Doe",
                                    "bio": "Hello, I am John",
                                    "profile_pic": "https://example.com/pic.jpg",
                                    "count_follower": 0,
                                    "count_following": 0
                                }
                                """)
                        .contentType("application/json"))
                .andExpect(status().isConflict());
    }

    @Test
    public void setProfile_shouldReturnConflict_whenUpdateFails() throws Exception {
        Mockito.doThrow(new SampleCustomException("Update failed"))
                .when(profileService).updateByUserId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        mockMvc.perform(post("/profile/existing")
                        .content("""
                                {
                                    "user_id": "123",
                                    "full_name": "John Doe",
                                    "bio": "Hello, I am John",
                                    "profile_pic": "https://example.com/pic.jpg",
                                    "count_follower": 0,
                                    "count_following": 0
                                }
                                """)
                        .contentType("application/json"))
                .andExpect(status().isConflict());
}
}