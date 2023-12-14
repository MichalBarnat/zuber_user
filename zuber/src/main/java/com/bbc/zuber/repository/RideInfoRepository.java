package com.bbc.zuber.repository;

import com.bbc.zuber.model.rideinfo.RideInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideInfoRepository extends JpaRepository<RideInfo, Long> {
}
