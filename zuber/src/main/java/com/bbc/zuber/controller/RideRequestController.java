package com.bbc.zuber.controller;

import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.command.CreateRideRequestCommand;
import com.bbc.zuber.model.riderequest.dto.RideRequestDto;
import com.bbc.zuber.service.RideRequestService;
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

@RestController
@RequestMapping("/api/rideRequests")
@RequiredArgsConstructor
@Slf4j
public class RideRequestController {

    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<RideRequestDto> getRideRequest(@PathVariable Long id) {
        RideRequestDto dto = modelMapper.map(rideRequestService.getRideRequest(id), RideRequestDto.class);
        return new ResponseEntity<>(dto, OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> save(@RequestBody @Valid CreateRideRequestCommand command, @PathVariable Long id) throws JsonProcessingException {
        RideRequest rideRequestToSave = modelMapper.map(command, RideRequest.class);
        rideRequestToSave.setUserUuid(userService.findById(id).getUuid());

        UUID requestUuid = UUID.randomUUID();
        FundsAvailability fundsAvailability = FundsAvailability.builder()
                .uuid(requestUuid)
                .userUuid(userService.findById(id).getUuid())
                .pickUpLocation(command.getPickUpLocation())
                .dropOffLocation(command.getDropOffLocation())
                .fundsAvailable(false)
                .build();

        fundsAvailabilityService.save(fundsAvailability);

        String fundsAvailabilityJson = objectMapper.writeValueAsString(fundsAvailability);
        kafkaTemplate.send("user-funds-availability", fundsAvailabilityJson);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (fundsAvailabilityService.findByUuid(requestUuid).getFundsAvailable() == null) {
            return new ResponseEntity<>("Timeout reached waiting for funds availability STATUS: ", HttpStatus.REQUEST_TIMEOUT);
        }

        if (!fundsAvailabilityService.findByUuid(requestUuid).getFundsAvailable()) {
            return new ResponseEntity<>("User doesn't have enough funds for this ride!", HttpStatus.FORBIDDEN);
        }

    
        RideRequest savedRideRequest = rideRequestService.createRideRequest(rideRequestToSave);
        String rideRequestJson = objectMapper.writeValueAsString(savedRideRequest);
        kafkaTemplate.send("ride-request", rideRequestJson);

        return ResponseEntity.ok(modelMapper.map(savedRideRequest, RideRequestDto.class));
    }
}
