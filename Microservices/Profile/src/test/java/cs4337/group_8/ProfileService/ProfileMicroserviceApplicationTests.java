package cs4337.group_8.ProfileService;

import cs4337.group_8.ProfileService.controllers.ControllerProfile;
import cs4337.group_8.ProfileService.services.ProfileService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ContextConfiguration(classes = {ControllerProfile.class, ProfileService.class})

class ProfileMicroserviceApplicationTests {
	@Mock
	private ProfileService profileService;

	@InjectMocks
	private ControllerProfile controllerProfile;

	private MockMvc mockMvc;


	@Test
	void contextLoads() {
		assertNotNull(profileService);
		assertNotNull(controllerProfile);
	}
	@Test
	void testControllerEndpoints() {
		assertNotNull(controllerProfile.test());
	}

	@Test
	void testServiceFunctionality() {
		assertNotNull(profileService);
	}


}
