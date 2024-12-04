package cs4337.group_8.AuthenticationMicroservice.mappers;


import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleAuthorizationResponse;
import cs4337.group_8.AuthenticationMicroservice.entities.GoogleResourceTokenEntity;

public class TokenMapper {

    public static TokenMapper INSTANCE = new TokenMapper();

    public GoogleResourceTokenEntity toTokenEntity(GoogleAuthorizationResponse tokenResponse) {
        GoogleResourceTokenEntity googleResourceTokenEntity = new GoogleResourceTokenEntity();
        googleResourceTokenEntity.setRefreshToken(tokenResponse.getRefresh_token());
        return googleResourceTokenEntity;
    }
}
