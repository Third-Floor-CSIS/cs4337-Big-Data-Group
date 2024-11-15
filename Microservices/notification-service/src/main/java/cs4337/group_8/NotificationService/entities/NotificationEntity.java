package cs4337.group_8.NotificationService.entities;

import cs4337.group_8.NotificationService.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@ToString(callSuper = true, includeFieldNames = true)
@Table(name = "notification")
public class NotificationEntity {

    @Id
    @Column
    private String id; //primary key

    @Column
    private String senderId;

    @Column
    private String receiverId;

    @Column
    private String message;

    @Column
    private NotificationType type;

    @Column
    private LocalDateTime timestamp;

    @Column
    private boolean read;

}
