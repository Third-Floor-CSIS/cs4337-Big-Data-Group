package cs4337.group_8.NotificationService.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private String id;
    private String userId;
    private String message;
    private NotificationType type;
    private LocalDateTime timestamp;
    private boolean read;

    // Enum for notification types
    public enum NotificationType {
        INFO,
        WARNING,
        ERROR,
        SUCCESS
    }
}