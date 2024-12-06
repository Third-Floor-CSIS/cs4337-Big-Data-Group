package cs4337.group_8.NotificationService.controllers;

import cs4337.group_8.NotificationService.services.KafkaConsumer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaConsumerController {

    private final KafkaConsumer kafkaConsumer;

    public KafkaConsumerController(KafkaConsumer kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    @GetMapping("/analytics")
    public String getAnalytics() {
        return "Messages processed: " + kafkaConsumer.getMessagesProcessedCount();
    }
}
