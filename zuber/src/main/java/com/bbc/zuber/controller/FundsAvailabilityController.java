package com.bbc.zuber.controller;

import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.service.FundsAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fundsAvailability")
@RequiredArgsConstructor
public class FundsAvailabilityController {

    private final FundsAvailabilityService fundsAvailabilityService;

    @GetMapping("/{id}")
    public ResponseEntity<FundsAvailability> findById(@PathVariable Long id) {
        return ResponseEntity.ok(fundsAvailabilityService.findById(id));
    }

}
