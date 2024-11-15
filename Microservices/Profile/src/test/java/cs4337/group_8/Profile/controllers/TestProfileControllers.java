package cs4337.group_8.Profile.controllers;

import cs4337.group_8.ProfileService.DTO.ProfileDTO;
import cs4337.group_8.ProfileService.exceptions.SampleCustomException;
import cs4337.group_8.ProfileService.services.ProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ProfileController;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
public class TestProfileControllers {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    // First test the '/profile/test' endpoint
    @Test
    public void testEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));

    }

    // Test the '/profile/new' success endpoint
    @Test
    public void testNewUserEndpointSuccess() throws Exception {

        // test user id
        String user_id = "1234";

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUser_id(user_id);

        // Assume user id doesnt exist
        Mockito.doNothing().when(profileService).getUserExistanceById(user_id);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user_id\":\"" + user_id + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(user_id));
    }

    // Test the '/profile/new' fail endpoint
    @Test
    public void testNewUserEndpointFail() throws Exception {

        // test user id
        String user_id = "1234";

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUser_id(user_id);

        // throw exception
        Mockito.doThrow(new SampleCustomException("User already exists"))
                        .when(profileService).getUserExistanceById(user_id);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user_id\":\"" + user_id + "\"}"))
                .andExpect(status().isConflict());
    }

}
