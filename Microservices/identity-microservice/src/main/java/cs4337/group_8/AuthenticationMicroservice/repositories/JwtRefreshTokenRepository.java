package cs4337.group_8.AuthenticationMicroservice.repositories;

import cs4337.group_8.AuthenticationMicroservice.entities.JwtRefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshTokenEntity, Long> {
    Optional<JwtRefreshTokenEntity> findByUserId(int userId);
}
