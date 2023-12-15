package com.bbc.zuber.service.producer;

import com.bbc.zuber.model.ridecancel.RideCancel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideCancelProducerService {

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
}
