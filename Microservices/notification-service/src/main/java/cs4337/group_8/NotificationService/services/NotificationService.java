package cs4337.group_8.NotificationService.services;

import cs4337.group_8.NotificationService.DTO.NotificationDTO;
import cs4337.group_8.NotificationService.mappers.NotificationMapper;
import cs4337.group_8.NotificationService.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationMapper notificationMapper;


    public List<NotificationDTO> getUnreadNotifications(String receiverId) {
    }
}