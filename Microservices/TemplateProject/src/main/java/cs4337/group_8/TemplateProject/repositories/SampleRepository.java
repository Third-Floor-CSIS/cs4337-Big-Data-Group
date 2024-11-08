package cs4337.group_8.TemplateProject.repositories;

import cs4337.group_8.TemplateProject.entities.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
// <Entity, ID> (ID is the type of the primary key of the entity)
public interface SampleRepository extends JpaRepository<SampleEntity, Integer> {
    // Optional is a container object which may or may not contain a non-null value
    Optional<SampleEntity> findByIdEquals(Integer id);
    Optional<List<SampleEntity>> findAllByDateOfBirthBefore(Date date);
    Optional<List<SampleEntity>> findAllByVerifiedTrue();

    // You can also use the @Query annotation to write custom queries
    // Where ?1 is the first parameter of the method and ?2 is the second
    @Query(value = "SELECT e FROM SampleEntity e WHERE e.firstname = ?1 AND e.verified = ?2")
    Optional<List<SampleEntity>> findAllByFirstnameAndVerified(String firstname, boolean verified);
}
