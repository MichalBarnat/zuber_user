package com.bbc.zuber.kafka;

import com.bbc.zuber.model.fundsavailability.FundsAvailability;
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
            throw new RuntimeException(e);
        }
    }

    public void sendUserFundsAvailability(FundsAvailability fundsAvailability) {
        try {
            String fundsAvailabilityJson = objectMapper.writeValueAsString(fundsAvailability);
            kafkaTemplate.send("user-funds-availability", fundsAvailabilityJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRideRequest(RideRequest savedRideRequest) {
        try {
            String rideRequestJson = objectMapper.writeValueAsString(savedRideRequest);
            kafkaTemplate.send("ride-request", rideRequestJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

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
