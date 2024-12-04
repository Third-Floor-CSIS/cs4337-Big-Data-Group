package cs4337.group_8.AuthenticationMicroservice.repositories;

import cs4337.group_8.AuthenticationMicroservice.entities.GoogleResourceTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<GoogleResourceTokenEntity, Integer> {

    Optional<GoogleResourceTokenEntity> getTokenEntityByCurrentAccessToken(String currentAccessToken);

    Optional<GoogleResourceTokenEntity> getTokenEntityByUserId(int userId);

    @Modifying
    @Query("UPDATE GoogleResourceTokenEntity t " +
            "SET t.currentAccessToken = :currentAccessToken, " +
            "t.expirationTimeAccessToken = :expirationTimeForAccessToken " +
            "WHERE t.userId = :userId")
    void updateRefreshTokenByUserId(@Param("userId") int userId,
                                    @Param("currentAccessToken") String currentAccessToken,
                                    @Param("expirationTimeForAccessToken") Instant expirationTimeForAccessToken);
}
