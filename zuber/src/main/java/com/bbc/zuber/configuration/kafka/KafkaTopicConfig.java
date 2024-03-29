package com.bbc.zuber.configuration.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaTopicConfig {

    @Bean
    public NewTopic userRegistration() {
        return TopicBuilder.name("user-registration")
                .build();
    }

    @Bean
    public NewTopic userDelete() {
        return TopicBuilder.name("user-deleted")
                .build();
    }

    @Bean
    public NewTopic userEdit() {
        return TopicBuilder.name("user-edited")
                .build();
    }

    @Bean
    public NewTopic rideRequestRegistration() {
        return TopicBuilder.name("ride-request")
                .build();
    }

    @Bean
    public NewTopic rideCancel() {
        return TopicBuilder.name("ride-cancel")
                .build();
    }

    @Bean
    public NewTopic checkFundsAvailability() {
        return TopicBuilder.name("user-funds-availability")
                .build();
    }

    @Bean
    public NewTopic userMessage() {
        return TopicBuilder.name("user-message")
                .build();
    }

}
