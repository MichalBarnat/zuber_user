package com.bbc.zuber.service;

import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.repository.FundsAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FundsAvailabilityService {
    private final FundsAvailabilityRepository fundsAvailabilityRepository;

    public FundsAvailability save(FundsAvailability fundsAvailability) {
        return fundsAvailabilityRepository.save(fundsAvailability);
    }

    public List<FundsAvailability> findAll() {
        return fundsAvailabilityRepository.findAll();
    }

    public FundsAvailability findById(Long id){
        return fundsAvailabilityRepository.findById(id).orElseThrow();
    }

    public FundsAvailability findByUuid(UUID uuid) {
        return findAll()
                .stream()
                .filter(request -> request.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow();
    }

    @Transactional
    public void setFundsAvailability(UUID uuid, boolean available) {
        FundsAvailability fundsAvailability = findByUuid(uuid);
        fundsAvailability.setFundsAvailable(available);
        save(fundsAvailability);
    }
}
