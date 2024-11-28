package cs4337.group_8.NotificationService.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    private int messagesProcessedCount = 0;

    @KafkaListener(topics = "profile-updates", groupId = "notification-group")
    public void consumeProfileUpdates(String message) {
        System.out.println("Notification Received (Profile Update): " + message);
        LOGGER.info("Consumed profile message: {}", message);
        messagesProcessedCount++;
    }

    @KafkaListener(topics = "post-updates", groupId = "notification-group")
    public void consumePostUpdates(String message) {
        System.out.println("Notification Received (Post Update): " + message);
        LOGGER.info("Consumed post message: {}", message);
        messagesProcessedCount++;
    }

    public int getMessagesProcessedCount() {
        return messagesProcessedCount;
    }
}





