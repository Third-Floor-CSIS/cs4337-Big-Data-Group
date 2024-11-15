package cs4337.group_8.NotificationService.repositories;

import cs4337.group_8.NotificationService.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {

    // Gets all users unread notifications that match the receiverId
    @Query("SELECT n FROM NotificationEntity n WHERE n.receiverId = :receiverId AND n.read = false")
    List<NotificationEntity> findUnreadNotificationsByReceiverId(@Param("receiverId") String receiverId);

    // Sets a notification to read
    @Modifying
    @Transactional
    @Query("UPDATE NotificationEntity n SET n.read = true WHERE n.id = :notificationId")
    void markNotificationAsRead(@Param("notificationId") String notificationId);

}
