package com.bbc.zuber.service;

import com.bbc.zuber.model.ridecancel.RideCancel;
import com.bbc.zuber.repository.RideCancelRepository;
import com.bbc.zuber.service.producer.RideCancelProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RideCancelService {

    private final RideCancelRepository rideCancelRepository;
    private final RideCancelProducerService producerService;

    @Transactional
    public RideCancel save(RideCancel rideCancel) {
        RideCancel rideCancelSaved = rideCancelRepository.save(rideCancel);
        producerService.sendRideCancel(rideCancelSaved);
        return rideCancelSaved;
    }
}