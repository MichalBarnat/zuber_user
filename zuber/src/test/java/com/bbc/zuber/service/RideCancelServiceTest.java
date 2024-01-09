package com.bbc.zuber.service;

import com.bbc.zuber.kafka.KafkaProducerService;
import com.bbc.zuber.model.ridecancel.RideCancel;
import com.bbc.zuber.repository.RideCancelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RideCancelServiceTest {

    @Mock
    private RideCancelRepository rideCancelRepository;
    @Mock
    private KafkaProducerService producerService;
    @InjectMocks
    private RideCancelService rideCancelService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldSaveRideCancel() {
        //Given
        RideCancel rideCancel = RideCancel.builder()
                .id(1L)
                .rideAssignmentId(UUID.randomUUID())
                .cancel(true)
                .build();

        when(rideCancelRepository.save(any(RideCancel.class)))
                .thenReturn(rideCancel);

        //When
        RideCancel result = rideCancelService.save(rideCancel);

        //Then
        verify(rideCancelRepository, times(1)).save(rideCancel);
        verify(producerService, times(1)).sendRideCancel(rideCancel);
        assertEquals(rideCancel, result);
    }
}