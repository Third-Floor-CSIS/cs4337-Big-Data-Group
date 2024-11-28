package cs4337.group_8.ProfileService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProfileMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfileMicroserviceApplication.class, args);
	}

}
