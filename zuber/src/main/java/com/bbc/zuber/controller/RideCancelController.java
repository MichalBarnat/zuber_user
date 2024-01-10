package com.bbc.zuber.controller;

import com.bbc.zuber.model.response.UserResponse;
import com.bbc.zuber.model.ridecancel.RideCancel;
import com.bbc.zuber.service.RideCancelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rideCancel")
@RequiredArgsConstructor
public class RideCancelController {

    private final RideCancelService service;

    @PostMapping
    public ResponseEntity<UserResponse> save(@RequestBody RideCancel rideCancel) {
        service.save(rideCancel);
        return ResponseEntity.ok(UserResponse.builder()
                .message(String.format("Trying to cancel ride assignment with uuid: %s", rideCancel.getRideAssignmentUuid()))
                .build());
    }

}
