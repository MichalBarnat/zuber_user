package com.bbc.zuber.service.producer;

import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.model.riderequest.RideRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestProducerService {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

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
}

//todo przeniesc do klasy KafkaProducerService i tam obsłuzyc wszystkie wysyłania na osobne tematy