package cs4337.group_8.NotificationService.controllers;

import cs4337.group_8.NotificationService.DTO.NotificationDTO;
import cs4337.group_8.NotificationService.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/unread/{receiverId}")
    public List<NotificationDTO> getUnreadNotifications(@PathVariable String receiverId) {
        return notificationService.getUnreadNotifications(receiverId);
    }

    @PostMapping("/read/{id}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable String id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok().build(); // returning as ResponseEntity incase we want status codes
    }
}
