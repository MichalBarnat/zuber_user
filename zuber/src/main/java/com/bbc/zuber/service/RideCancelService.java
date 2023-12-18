package com.bbc.zuber.service;

import com.bbc.zuber.kafka.KafkaProducerService;
import com.bbc.zuber.model.ridecancel.RideCancel;
import com.bbc.zuber.repository.RideCancelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RideCancelService {

    private final RideCancelRepository rideCancelRepository;
    private final KafkaProducerService producerService;

    @Transactional
    public RideCancel save(RideCancel rideCancel) {
        RideCancel rideCancelSaved = rideCancelRepository.save(rideCancel);
        producerService.sendRideCancel(rideCancelSaved);
        return rideCancelSaved;
    }
}