package com.bbc.zuber.controller;

import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.command.CreateRideRequestCommand;
import com.bbc.zuber.model.riderequest.dto.RideRequestDto;
import com.bbc.zuber.service.RideRequestService;
import com.bbc.zuber.service.UserService;
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
    private final UserService userService;
    private final KafkaTemplate<String, RideRequest> kafkaTemplate;

    @GetMapping("/{id}")
    public RideRequest getRideRequest(@PathVariable Long id) {
        return rideRequestService.getRideRequest(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<RideRequestDto> save(@RequestBody CreateRideRequestCommand command, @PathVariable Long id) {
        RideRequest rideRequestToSave = modelMapper.map(command, RideRequest.class);
        rideRequestToSave.setUserId(userService.getUser(id).getUuid());
        RideRequest savedRideRequest = rideRequestService.createRideRequest(rideRequestToSave);
        kafkaTemplate.send("ride-request", savedRideRequest);
        return ResponseEntity.ok(modelMapper.map(savedRideRequest, RideRequestDto.class));
    }

}
