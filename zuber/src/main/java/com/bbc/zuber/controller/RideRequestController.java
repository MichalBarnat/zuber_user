package com.bbc.zuber.controller;

import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.command.CreateRideRequestCommand;
import com.bbc.zuber.model.riderequest.dto.RideRequestDto;
import com.bbc.zuber.service.RideRequestService;
import com.bbc.zuber.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}")
    public RideRequest getRideRequest(@PathVariable Long id) {
        return rideRequestService.getRideRequest(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<RideRequestDto> save(@RequestBody @Valid CreateRideRequestCommand command, @PathVariable Long id) throws JsonProcessingException {
        RideRequest rideRequestToSave = modelMapper.map(command, RideRequest.class);
        rideRequestToSave.setUserId(userService.getUser(id).getUuid());

        RideRequest savedRideRequest = rideRequestService.createRideRequest(rideRequestToSave);
        String rideRequestJson = objectMapper.writeValueAsString(savedRideRequest);
        kafkaTemplate.send("ride-request", rideRequestJson);
        return ResponseEntity.ok(modelMapper.map(savedRideRequest, RideRequestDto.class));
    }

}
