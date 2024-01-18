package com.bbc.zuber.kafka;

import com.bbc.zuber.exception.KafkaMessageProcessingException;
import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.model.message.command.CreateMessageCommand;
import com.bbc.zuber.model.ridecancel.RideCancel;
import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendRideCancel(RideCancel rideCancelSaved) {
        try {
            String rideCancelJson = objectMapper.writeValueAsString(rideCancelSaved);
            kafkaTemplate.send("ride-cancel", rideCancelJson);
        } catch (JsonProcessingException e) {
            throw new KafkaMessageProcessingException("Problem with sending to topic ride-cancel.");
        }
    }

    public void sendUserFundsAvailability(FundsAvailability fundsAvailability) {
        try {
            String fundsAvailabilityJson = objectMapper.writeValueAsString(fundsAvailability);
            kafkaTemplate.send("user-funds-availability", fundsAvailabilityJson);
        } catch (JsonProcessingException e) {
            throw new KafkaMessageProcessingException("Problem with sending to topic user-funds-availability.");
        }
    }

    public void sendRideRequest(RideRequest savedRideRequest) {
        try {
            String rideRequestJson = objectMapper.writeValueAsString(savedRideRequest);
            kafkaTemplate.send("ride-request", rideRequestJson);
        } catch (JsonProcessingException e) {
            throw new KafkaMessageProcessingException("Problem with sending to topic ride-request.");
        }
    }

    public void sendSavedUser(User savedUser) {
        try {
            String userJson = objectMapper.writeValueAsString(savedUser);
            kafkaTemplate.send("user-registration", userJson);
        } catch (JsonProcessingException e) {
            throw new KafkaMessageProcessingException("Problem with sending to topic user-registration.");
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
            throw new KafkaMessageProcessingException("Problem with sending to topic user-edited.");
        }
    }

    public void sendMessage(CreateMessageCommand command) {
        try {
            String messageJson = objectMapper.writeValueAsString(command);
            kafkaTemplate.send("user-message", messageJson);
        } catch (JsonProcessingException e) {
            throw new KafkaMessageProcessingException("Problem with sending to topic user-message.");
        }
    }
}
