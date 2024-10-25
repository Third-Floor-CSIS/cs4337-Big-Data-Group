package cs4337.group_8.AuthenticationMicroservice.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private int userId;
    private String profilePicture;
}
