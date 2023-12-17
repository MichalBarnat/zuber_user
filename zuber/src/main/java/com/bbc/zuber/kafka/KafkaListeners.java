package com.bbc.zuber.kafka;

import com.bbc.zuber.model.rideinfo.RideInfo;
import com.bbc.zuber.model.user.User;
import com.bbc.zuber.service.FundsAvailabilityService;
import com.bbc.zuber.service.RideInfoService;
import com.bbc.zuber.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final RideInfoService rideInfoService;
    private final ObjectMapper objectMapper;
    private final FundsAvailabilityService fundsAvailabilityService;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(KafkaListeners.class);

    @KafkaListener(topics = "ride-info")
    void rideInfoListener(String rideInfoString) throws JsonProcessingException {
        RideInfo savedRideInfo = objectMapper.readValue(rideInfoString, RideInfo.class);
        rideInfoService.save(savedRideInfo);
        logger.info("Successfully rideInfo");
        logger.info("Your info about ride: ");
        logger.info("{}", savedRideInfo);
        logger.info("Have a good trip!");
    }

    @KafkaListener(topics = "funds-availability-response")
    void fundsAvailabilityResponseListener(String responseJson) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(responseJson);
        UUID uuid = UUID.fromString(jsonNode.get("uuid").asText());
        UUID userUuid = UUID.fromString(jsonNode.get("userUuid").asText());
        User user = userService.findByUuid(userUuid);
        BigDecimal cost = BigDecimal.valueOf(jsonNode.get("cost").asDouble());

        boolean canAfford = user.getBalance().compareTo(cost) > 0;

        fundsAvailabilityService.setFundsAvailability(uuid, canAfford);

        logger.info("FUND AVAILABILITY ID: {}", uuid);
        logger.info("COST OF THAT RIDE WILL BE : {}", cost);
    }
}