package com.bbc.zuber.service;

import com.bbc.zuber.model.rideinfo.RideInfo;
import com.bbc.zuber.repository.RideInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;

class RideInfoServiceTest {

    @Mock
    private RideInfoRepository rideInfoRepository;
    @InjectMocks
    private RideInfoService rideInfoService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldSaveRideInfoService() {
        //Given
        RideInfo rideInfo = RideInfo.builder()
                .id(1L)
                .rideAssignmentUuid(UUID.randomUUID())
                .userUuid(UUID.randomUUID())
                .driverUuid(UUID.randomUUID())
                .driverName("Test")
                .driverLocation("Test")
                .pickUpLocation("Test")
                .dropUpLocation("Test")
                .orderTime(LocalDateTime.of(2024,1,10,12,30))
                .estimatedArrivalTime(LocalDateTime.of(2024,1,10,12,40))
                .costOfRide(BigDecimal.valueOf(50))
                .timeToArrivalInMinutes("2")
                .rideLengthInKilometers("10.5")
                .build();

        when(rideInfoRepository.save(any(RideInfo.class)))
                .thenReturn(rideInfo);

        //When
        RideInfo result = rideInfoService.save(rideInfo);

        //Then
        verify(rideInfoRepository, times(1)).save(rideInfo);
        assertEquals(rideInfo, result);
    }
}