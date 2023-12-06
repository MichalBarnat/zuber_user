package com.bbc.zuber.kafka;

import com.bbc.zuber.model.rideinfo.RideInfo;
import com.bbc.zuber.service.RideInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {
    private final RideInfoService rideInfoService;
    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(KafkaListeners.class);

    @KafkaListener(topics = "ride-info")
    void rideInfoListener(String rideInfoString) throws JsonProcessingException {
        RideInfo savedRideInfo = objectMapper.readValue(rideInfoString, RideInfo.class);
        rideInfoService.save(savedRideInfo);
        logger.info("Successfully rideInfo");
        System.out.println("Your info about ride: ");
        System.out.println(savedRideInfo);
        System.out.println("Have a good trip!");
    }

















}
