package cs4337.group_8.AuthenticationMicroservice.mappers;


import cs4337.group_8.AuthenticationMicroservice.POJOs.GoogleAuthorizationResponse;
import cs4337.group_8.AuthenticationMicroservice.entities.RefreshTokenEntity;

public class TokenMapper {

    public static TokenMapper INSTANCE = new TokenMapper();

    public RefreshTokenEntity toTokenEntity(GoogleAuthorizationResponse tokenResponse) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(tokenResponse.getRefresh_token());
        return refreshTokenEntity;
    }


}
