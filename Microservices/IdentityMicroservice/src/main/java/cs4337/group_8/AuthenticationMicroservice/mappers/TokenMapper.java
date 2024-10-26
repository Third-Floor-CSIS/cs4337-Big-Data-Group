package cs4337.group_8.AuthenticationMicroservice.mappers;


import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleAuthorizationResponse;
import cs4337.group_8.AuthenticationMicroservice.entities.TokenEntity;

public class TokenMapper {

    public static TokenMapper INSTANCE = new TokenMapper();

    public TokenEntity toTokenEntity(GoogleAuthorizationResponse tokenResponse) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setRefreshToken(tokenResponse.getRefresh_token());
        return tokenEntity;
    }
}
