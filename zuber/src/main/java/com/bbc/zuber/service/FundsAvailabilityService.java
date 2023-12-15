package com.bbc.zuber.service;

import com.bbc.zuber.exception.FundsAvailabilityNotFound;
import com.bbc.zuber.exception.FundsAvailabilityUuidNotFound;
import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.repository.FundsAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FundsAvailabilityService {

    private final FundsAvailabilityRepository fundsAvailabilityRepository;

    @Transactional
    public FundsAvailability save(FundsAvailability fundsAvailability) {
        return fundsAvailabilityRepository.save(fundsAvailability);
    }

    @Transactional(readOnly = true)
    public Page<FundsAvailability> findAll(Pageable pageable) {
        return fundsAvailabilityRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public FundsAvailability findById(Long id) {
        return fundsAvailabilityRepository.findById(id)
                .orElseThrow(() -> new FundsAvailabilityNotFound(id));
    }

    @Transactional(readOnly = true)
    public FundsAvailability findByUuid(UUID uuid) {
        return fundsAvailabilityRepository.findByUuid(uuid)
                .orElseThrow(() -> new FundsAvailabilityUuidNotFound(uuid));
    }

    @Transactional
    public void setFundsAvailability(UUID uuid, boolean available) {
        FundsAvailability fundsAvailability = fundsAvailabilityRepository.findByUuid(uuid)
                .orElseThrow(() -> new FundsAvailabilityUuidNotFound(uuid));

        fundsAvailability.setFundsAvailable(available);
        fundsAvailabilityRepository.save(fundsAvailability);
    }
}
