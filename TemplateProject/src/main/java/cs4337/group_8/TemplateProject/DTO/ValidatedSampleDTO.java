package cs4337.group_8.TemplateProject.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Date;

@Data
public class ValidatedSampleDTO {
    private Integer id;

    // Pretend this is a DTO for an incoming registration request
    @Size(min = 2, message = "Firstname too short")
    @NotNull
    private String firstname;

    @Size(min = 2, message = "Surname too short") // Message is what the error message will be if the validation fails
    @NotNull
    private String surname;

    @Email(message = "Email must be valid")
    @NotNull(message = "Email not provided")
    private String email;

    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W)(?!.* ).{8,}",
            message = "Password does not meet the requirements") // You can even provide regexes
    @NotNull
    private String password;

    @Past(message = "Date Of Birth is in the future") // And time comparison
    @NotNull(message = "Date of Birth not provided")
    private Date dateOfBirth;
}
