package com.bbc.zuber.kafka;

import com.bbc.zuber.model.rideinfo.RideInfo;
import com.bbc.zuber.service.RideInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {
    private final RideInfoService rideInfoService;

    @KafkaListener(topics = "ride-info")
    void rideInfoListener(RideInfo rideInfo) {
        RideInfo savedRideInfo = rideInfoService.save(rideInfo);
        System.out.println("Your info about ride: ");
        System.out.println(savedRideInfo);
        System.out.println("Have a good trip!");
    }

















}
