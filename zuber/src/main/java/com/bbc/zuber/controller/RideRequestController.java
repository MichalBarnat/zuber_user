package com.bbc.zuber.controller;

import com.bbc.zuber.model.rideRequest.RideRequest;
import com.bbc.zuber.model.rideRequest.command.CreateRideRequestCommand;
import com.bbc.zuber.model.rideRequest.dto.RideRequestDto;
import com.bbc.zuber.service.RideRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/rideRequests")
@RequiredArgsConstructor
@Slf4j
public class RideRequestController {

    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, RideRequest> kafkaTemplate;

    @GetMapping("/{id}")
    public RideRequest getRideRequest(@PathVariable Long id) {
        return rideRequestService.getRideRequest(id);
    }

    @PostMapping
    public ResponseEntity<RideRequestDto> save(@RequestBody CreateRideRequestCommand command) {
        RideRequest rideRequestToSave = modelMapper.map(command, RideRequest.class);
        RideRequest savedRideRequest = rideRequestService.createRideRequest(rideRequestToSave);
        kafkaTemplate.send("ride_request-registration", savedRideRequest);
        return ResponseEntity.ok(modelMapper.map(savedRideRequest, RideRequestDto.class));
    }

}
