package com.bbc.zuber.repository;

import com.bbc.zuber.model.ridecancel.RideCancel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideCancelRepository extends JpaRepository<RideCancel, Long> {
}
