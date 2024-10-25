package cs4337.group_8.AuthenticationMicroservice.POJOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleAuthorizationResponse {
    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String scope;
    private String id_token;
    private String refresh_token;
}
