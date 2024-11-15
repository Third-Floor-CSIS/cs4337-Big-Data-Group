package cs4337.group_8.NotificationService.repositories;

import cs4337.group_8.NotificationService.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
}
