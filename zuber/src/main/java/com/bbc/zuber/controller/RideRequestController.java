package com.bbc.zuber.controller;

import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.command.CreateRideRequestCommand;
import com.bbc.zuber.model.riderequest.dto.RideRequestDto;
import com.bbc.zuber.model.riderequest.response.RideRequestResponse;
import com.bbc.zuber.service.RideRequestService;
import com.bbc.zuber.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/rideRequests")
@RequiredArgsConstructor
@Slf4j
public class RideRequestController {

    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<RideRequestDto> getRideRequest(@PathVariable Long id) {
        RideRequestDto dto = modelMapper.map(rideRequestService.getRideRequest(id), RideRequestDto.class);
        return new ResponseEntity<>(dto, OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<RideRequestResponse> save(@RequestBody @Valid CreateRideRequestCommand command, @PathVariable Long id) {
        RideRequest rideRequest = modelMapper.map(command, RideRequest.class);
        rideRequest.setUserUuid(userService.findById(id).getUuid());
        RideRequestResponse response = rideRequestService.createRideRequestResponse(rideRequest, id);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
