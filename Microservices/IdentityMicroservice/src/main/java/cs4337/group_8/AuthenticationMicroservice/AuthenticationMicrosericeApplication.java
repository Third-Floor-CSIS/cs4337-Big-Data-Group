package cs4337.group_8.AuthenticationMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//		(scanBasePackages = {"cs4337.group_8.AuthenticationMicroservice", "cs4337.group_8.AuthenticationMicroservice.services"})
@EnableFeignClients
public class AuthenticationMicrosericeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationMicrosericeApplication.class, args);
	}

}
