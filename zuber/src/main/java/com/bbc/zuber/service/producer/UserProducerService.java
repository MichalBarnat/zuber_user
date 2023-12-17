package com.bbc.zuber.service.producer;

import com.bbc.zuber.model.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendSavedUser(User savedUser) {
        try {
            String userJson = objectMapper.writeValueAsString(savedUser);
            kafkaTemplate.send("user-registration", userJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDeletedUser(UUID uuid) {
        kafkaTemplate.send("user-deleted", uuid);
    }

    public void sendEditedUser(User editedUser) {
        try {
            String editedUserJson = objectMapper.writeValueAsString(editedUser);
            kafkaTemplate.send("user-edited", editedUserJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
