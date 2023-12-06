package com.bbc.zuber.service;

import com.bbc.zuber.model.ridecancel.RideCancel;
import com.bbc.zuber.repository.RideCancelRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideCancelService {
    private final RideCancelRepository rideCancelRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RideCancel save(RideCancel rideCancel) throws JsonProcessingException {
        RideCancel rideCancelsaved = rideCancelRepository.save(rideCancel);
        String rideCancelJson = objectMapper.writeValueAsString(rideCancelsaved);
        kafkaTemplate.send("ride-cancel", rideCancelJson);
        return rideCancelsaved;
    }


}