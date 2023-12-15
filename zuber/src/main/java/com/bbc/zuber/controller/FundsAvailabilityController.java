package com.bbc.zuber.controller;

import com.bbc.zuber.model.fundsavailability.dto.FundsAvailabilityDto;
import com.bbc.zuber.service.FundsAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/fundsAvailability")
@RequiredArgsConstructor
public class FundsAvailabilityController {

    private final FundsAvailabilityService fundsAvailabilityService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<FundsAvailabilityDto> findById(@PathVariable Long id) {
        FundsAvailabilityDto dto = modelMapper.map(fundsAvailabilityService.findById(id), FundsAvailabilityDto.class);
        return new ResponseEntity<>(dto, OK);
    }

    @GetMapping("/by-uuid/{uuid}")
    public ResponseEntity<FundsAvailabilityDto> findByUuid(@PathVariable UUID uuid) {
        FundsAvailabilityDto dto = modelMapper.map(fundsAvailabilityService.findByUuid(uuid), FundsAvailabilityDto.class);
        return new ResponseEntity<>(dto, OK);
    }

    @GetMapping
    public ResponseEntity<Page<FundsAvailabilityDto>> findAll(@PageableDefault Pageable pageable) {
        Page<FundsAvailabilityDto> dtos = fundsAvailabilityService.findAll(pageable)
                .map(fundsAvailability -> modelMapper.map(fundsAvailability, FundsAvailabilityDto.class));
        return new ResponseEntity<>(dtos, OK);
    }
}
