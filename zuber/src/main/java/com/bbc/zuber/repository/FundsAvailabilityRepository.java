package com.bbc.zuber.repository;

import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FundsAvailabilityRepository extends JpaRepository<FundsAvailability, Long> {

    Optional<FundsAvailability> findByUuid(UUID uuid);
}
