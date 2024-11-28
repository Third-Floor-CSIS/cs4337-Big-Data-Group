package cs4337.group_8.ProfileService.repositories;

import cs4337.group_8.ProfileService.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// <Entity, ID> (ID is the type of the primary key of the entity)
public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {

    // Optional is a container object which may or may not contain a non-null value
    @Query(value = "SELECT e FROM ProfileEntity e WHERE e.userID = ?1")
    Optional<ProfileEntity> findByIdEquals(String userID);

    @Modifying
    @Query(
        """
        UPDATE ProfileEntity t
        SET t.fullName = :fullName,  t.bio = :bio,  t.profilePic = :profilePic
        WHERE t.userID = :userID
        """
    )
    void updateByUserId(
        @Param("userID")
        String userID,
        @Param("fullName")
        String fullName,
        @Param("bio")
        String bio,
        @Param("profilePic")
        String profilePic
    );

}
