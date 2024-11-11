package cs4337.group_8.AuthenticationMicroservice.repositories;

import cs4337.group_8.AuthenticationMicroservice.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthenticationRepository extends JpaRepository<UserEntity, Integer> {

}
