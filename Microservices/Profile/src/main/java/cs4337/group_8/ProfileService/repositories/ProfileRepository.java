package cs4337.group_8.ProfileService.repositories;

import cs4337.group_8.ProfileService.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
// <Entity, ID> (ID is the type of the primary key of the entity)
public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {

    // Optional is a container object which may or may not contain a non-null value
    @Query(value = "SELECT e FROM ProfileEntity e WHERE e.user_id = ?1")
    Optional<ProfileEntity> findByIdEquals(String user_id);

    @Modifying
    @Query(
        """
        UPDATE ProfileEntity t 
        SET t.full_name = :full_name,  t.bio = :bio,  t.profile_pic = :profile_pic  
        WHERE t.user_id = :user_id
        """
    )
    int updateByUserId(
        @Param("user_id")
        int user_id,
        @Param("full_name")
        String full_name,
        @Param("bio")
        String bio,
        @Param("profile_pic")
        String profile_pic
    );

}
