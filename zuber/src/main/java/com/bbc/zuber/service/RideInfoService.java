package com.bbc.zuber.service;

import com.bbc.zuber.model.rideinfo.RideInfo;
import com.bbc.zuber.repository.RideInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RideInfoService {

    private final RideInfoRepository rideInfoRepository;

    @Transactional
    public RideInfo save(RideInfo rideInfo) {
        return rideInfoRepository.save(rideInfo);
    }
}
