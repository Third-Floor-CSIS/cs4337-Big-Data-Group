package cs4337.group_8.ProfileService.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic profileUpdatesTopic() {
        return TopicBuilder.name("profile-updates").partitions(10).build();
    }
}