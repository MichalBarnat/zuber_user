package com.bbc.zuber.controller;

import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.command.CreateRideRequestCommand;
import com.bbc.zuber.model.riderequest.dto.RideRequestDto;
import com.bbc.zuber.service.FundsAvailabilityService;
import com.bbc.zuber.service.RideRequestService;
import com.bbc.zuber.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/rideRequests")
@RequiredArgsConstructor
@Slf4j
public class RideRequestController {

    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final FundsAvailabilityService fundsAvailabilityService;
    private final Logger logger = LoggerFactory.getLogger(RideRequestController.class);

    @GetMapping("/{id}")
    public ResponseEntity<RideRequest> getRideRequest(@PathVariable Long id) {
        return ResponseEntity.ok(rideRequestService.getRideRequest(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> save(@RequestBody CreateRideRequestCommand command, @PathVariable Long id) throws JsonProcessingException {
        RideRequest rideRequestToSave = modelMapper.map(command, RideRequest.class);
        rideRequestToSave.setUserUuid(userService.findById(id).getUuid());

        UUID requestUuid = UUID.randomUUID();
        FundsAvailability fundsAvailability = FundsAvailability.builder()
                .uuid(requestUuid)
                .userUuid(userService.findById(id).getUuid())
                .pickUpLocation(command.getPickUpLocation())
                .dropOffLocation(command.getDropOffLocation())
                .build();

        fundsAvailabilityService.save(fundsAvailability);

        String fundsAvailabilityJson = objectMapper.writeValueAsString(fundsAvailability);
        kafkaTemplate.send("user-funds-availability", fundsAvailabilityJson);


        final long timeout = 10000;
        long startTime = System.currentTimeMillis();
        while (fundsAvailability.getFundsAvailable() == null && System.currentTimeMillis() - startTime < timeout) {
            try {
                Thread.sleep(500);
                fundsAvailability = fundsAvailabilityService.findByUuid(requestUuid);
                logger.info("THIS UUID IS: {}",requestUuid);
                logger.info("STATUS: {}",fundsAvailabilityService.findByUuid(requestUuid).getFundsAvailable());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ResponseEntity<>("Interrupted while waiting for funds availability", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (fundsAvailability.getFundsAvailable() == null) {
            return new ResponseEntity<>("Timeout reached waiting for funds availability STATUS: ", HttpStatus.REQUEST_TIMEOUT);
        }

        if (!fundsAvailability.getFundsAvailable()) {
            return new ResponseEntity<>("User doesn't have enough funds for this ride!", HttpStatus.FORBIDDEN);
        }

        RideRequest savedRideRequest = rideRequestService.createRideRequest(rideRequestToSave);
        String rideRequestJson = objectMapper.writeValueAsString(savedRideRequest);
        kafkaTemplate.send("ride-request", rideRequestJson);

        return ResponseEntity.ok(modelMapper.map(savedRideRequest, RideRequestDto.class));
    }


//    @PostMapping("/{id}")
//    public ResponseEntity<?> save(@RequestBody CreateRideRequestCommand command, @PathVariable Long id) throws JsonProcessingException {
//        RideRequest rideRequestToSave = modelMapper.map(command, RideRequest.class);
//        rideRequestToSave.setUserUuid(userService.findById(id).getUuid());
//
//        FundsAvailability fundsAvailability = FundsAvailability.builder()
//                .uuid(UUID.randomUUID())
//                .userUuid(userService.findById(id).getUuid())
//                .pickUpLocation(command.getPickUpLocation())
//                .dropOffLocation(command.getDropOffLocation())
//                .build();
//
//        fundsAvailabilityService.save(fundsAvailability);
//
//        String fundsAvailabilityJson = objectMapper.writeValueAsString(fundsAvailability);
//        kafkaTemplate.send("user-funds-availability", fundsAvailabilityJson);
//
//        CompletableFuture.runAsync(() -> {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//
//
//        });
//
//        if(fundsAvailability.getFundsAvailable() == null) {
//            return new ResponseEntity<>("NULL !", FORBIDDEN);
//        }
//
//        if(fundsAvailability.getFundsAvailable() == false) {
//            return new ResponseEntity<>("User don't have enough funds for this ride!", FORBIDDEN);
//        }
//
//        RideRequest savedRideRequest = rideRequestService.createRideRequest(rideRequestToSave);
//        String rideRequestJson = objectMapper.writeValueAsString(savedRideRequest);
//        kafkaTemplate.send("ride-request", rideRequestJson);
//        return ResponseEntity.ok(modelMapper.map(savedRideRequest, RideRequestDto.class));
//    }

}
