package cs4337.group_8.AuthenticationMicroservice.repositories;

import cs4337.group_8.AuthenticationMicroservice.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {
    Optional<RefreshTokenEntity> getTokenEntityByUserId(int userId);
}
