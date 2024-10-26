package cs4337.group_8.AuthenticationMicroservice.POJOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@ToString
public class GoogleUserDetails {
    String id;
    String name;
    String given_name;
    String family_name;
    String email;
    boolean verified_email;
    String picture;
}
