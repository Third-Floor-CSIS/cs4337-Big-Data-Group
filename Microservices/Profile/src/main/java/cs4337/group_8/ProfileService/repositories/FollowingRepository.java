package cs4337.group_8.ProfileService.repositories;

import cs4337.group_8.ProfileService.entities.FollowingEntity;
import cs4337.group_8.ProfileService.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowingRepository extends JpaRepository<FollowingEntity, String> {
    @Query(value = "SELECT * FROM following f WHERE f.initiator_id = ?1 AND f.target_id = ?2", nativeQuery = true)
    Optional<FollowingEntity> findByInitiatorIdAndTargetId(String initiatorId, String targetId);

    @Query(value = "SELECT COUNT(*) FROM following f WHERE f.initiator_id = ?1 AND f.status = ?2", nativeQuery = true)
    Integer countFollowingEntitiesByInitiatorIdAndStatus(String initiatorId, Status status);

    @Query(value = "SELECT COUNT(*) FROM following f WHERE f.initiator_id = ?1 AND f.status = ?2", nativeQuery = true)
    Integer countFollowingEntitiesByTargetIdAndStatus(String targetId, Status status);
    
    Integer countAllByInitiatorIdEqualsAndStatusEquals(String initiatorId, Status status);
    Integer countAllByTargetIdEqualsAndStatusEquals(String targetId, Status status);
}
