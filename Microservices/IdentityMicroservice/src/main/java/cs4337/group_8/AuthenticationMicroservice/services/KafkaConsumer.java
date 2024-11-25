package cs4337.group_8.AuthenticationMicroservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    private int messagesProcessedCount = 0;

    @KafkaListener(topics = "Authentication-updates", groupId = "Authentication-group")
    public void consume(String message) {
        LOGGER.info("Consumed message: {}", message);
        messagesProcessedCount++;
    }

    public int getMessagesProcessedCount() {
        return messagesProcessedCount;
    }
}

