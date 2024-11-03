package cs4337.group_8.TemplateProject.repositories;

import cs4337.group_8.TemplateProject.entities.ProfileEntity;
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
    Optional<ProfileEntity> findByIdEquals(String id);
//    Optional<List<ProfileEntity>> findAllByDateOfBirthBefore(Date date);
//    Optional<List<ProfileEntity>> findAllByVerifiedTrue();

    // You can also use the @Query annotation to write custom queries
    //@Query(value = "SELECT e FROM ProfileEntity e WHERE e.firstname = ?1 AND e.verified = ?2")
    // Where ?1 is the first parameter of the method
    //Optional<List<ProfileEntity>> findAllByFirstnameAndVerified(String firstname, boolean verified);
}
