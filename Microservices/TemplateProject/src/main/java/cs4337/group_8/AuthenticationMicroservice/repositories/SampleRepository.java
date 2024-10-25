package cs4337.group_8.AuthenticationMicroservice.repositories;

import cs4337.group_8.AuthenticationMicroservice.entities.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SampleRepository extends JpaRepository<SampleEntity, Integer> { // <Entity, ID> (ID is the type of the primary key of the entity)

    Optional<SampleEntity> findByIdEquals(Integer id); // Optional is a container object which may or may not contain a non-null value
    Optional<List<SampleEntity>> findAllByDateOfBirthBefore(Date date);
    Optional<List<SampleEntity>> findAllByVerifiedTrue();

    // You can also use the @Query annotation to write custom queries
    @Query(value = "SELECT e FROM SampleEntity e WHERE e.firstname = ?1 AND e.verified = ?2")
    Optional<List<SampleEntity>> findAllByFirstnameAndVerified(String firstname, boolean verified); // Where ?1 is the first parameter of the method
}
