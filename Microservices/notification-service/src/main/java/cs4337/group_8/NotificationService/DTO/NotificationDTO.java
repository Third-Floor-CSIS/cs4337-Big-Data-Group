package cs4337.group_8.NotificationService.DTO;

import cs4337.group_8.NotificationService.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private String id;
    private String senderId;
    private String receiverId;
    private String message;
    private NotificationType type;
    private LocalDateTime timestamp;
    private boolean read;

}