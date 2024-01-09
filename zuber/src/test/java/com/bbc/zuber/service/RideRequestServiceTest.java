package com.bbc.zuber.service;

import com.bbc.zuber.exception.RideRequestNotFoundException;
import com.bbc.zuber.kafka.KafkaProducerService;
import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.dto.RideRequestDto;
import com.bbc.zuber.model.riderequest.response.RideRequestResponse;
import com.bbc.zuber.model.user.User;
import com.bbc.zuber.repository.RideRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.bbc.zuber.model.riderequest.enums.RideRequestSize.SINGLE;
import static com.bbc.zuber.model.riderequest.enums.RideRequestType.STANDARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.CREATED;


class RideRequestServiceTest {

    private RideRequest rideRequest;
    @Mock
    private RideRequestRepository rideRequestRepository;
    @Mock
    private KafkaProducerService producerService;
    @Mock
    private UserService userService;
    @Mock
    private FundsAvailabilityService fundsAvailabilityService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private RideRequestService rideRequestService;

    @BeforeEach
    void setUp() {
        openMocks(this);

        rideRequest = new RideRequest(1L, UUID.randomUUID(), UUID.randomUUID(),
                "Test", "Test", STANDARD, SINGLE, LocalDate.of(2024, 1, 10));
    }

    @Test
    void shouldGetRideRequest() {
        //Given
        long rideRequestId = 1L;

        when(rideRequestRepository.findById(rideRequestId))
                .thenReturn(Optional.of(rideRequest));

        //When
        RideRequest result = rideRequestService.getRideRequest(rideRequestId);

        //Then
        verify(rideRequestRepository, times(1)).findById(rideRequestId);
        assertEquals(rideRequest, result);
    }

    @Test
    void shouldSaveRideRequest() {
        //Given
        when(rideRequestRepository.save(any(RideRequest.class)))
                .thenReturn(rideRequest);

        //When
        RideRequest result = rideRequestService.save(rideRequest);

        //Then
        verify(rideRequestRepository, times(1)).save(rideRequest);
        assertEquals(rideRequest, result);
    }

//    @Test
//    void createRideRequestResponse() {
//        // Given
//        long userId = 1L;
//        UUID fundsAvailabilityUuid = UUID.randomUUID();
//
//        User user = new User();
//        user.setId(userId);
//        user.setUuid(UUID.randomUUID());
//        rideRequest.setUserUuid(user.getUuid());
//
//        FundsAvailability fundsAvailability = mock(FundsAvailability.class);
//
//        RideRequestDto dto = modelMapper.map(rideRequest, RideRequestDto.class);
//
//        when(modelMapper.map(eq(rideRequest), eq(RideRequestDto.class)))
//                .thenReturn(dto);
//        when(userService.findById(userId))
//                .thenReturn(user);
//        when(fundsAvailabilityService.save(any(FundsAvailability.class)))
//                .thenReturn(fundsAvailability);
//        when(fundsAvailabilityService.findByUuid(any(UUID.class)))
//                .thenReturn(fundsAvailability);
//        doReturn(true).when(fundsAvailability)
//                .getFundsAvailable();
//
//        // When
//        RideRequestResponse result = rideRequestService.createRideRequestResponse(rideRequest, userId);
//
//        // Then
//        verify(modelMapper, times(3)).map(eq(rideRequest), eq(RideRequestDto.class));
//        verify(userService, times(1)).findById(userId);
//        verify(fundsAvailabilityService, times(1)).save(any(FundsAvailability.class));
//        verify(fundsAvailabilityService, times(5)).findByUuid(any(UUID.class));
//        verify(rideRequestRepository, times(1)).save(rideRequest);
//        verify(producerService, times(1)).sendUserFundsAvailability(any(FundsAvailability.class));
//        verify(producerService, times(1)).sendRideRequest(any(RideRequest.class));
//
//        assertEquals("Ride request created successfully!", result.getMessage());
//        assertEquals(CREATED, result.getStatus());
//        assertEquals(dto, result.getRideRequestDto());
//    }


//    @Test
//    void shouldCancelRideRequest() {
//    }


    @Test
    void shouldThrowRideRequestNotFoundExceptionForGetRideRequestMethod() {
        //Given
        long rideRequestId = 1L;

        when(rideRequestRepository.findById(rideRequestId))
                .thenReturn(Optional.empty());

        //When
        RideRequestNotFoundException exception = assertThrows(
                RideRequestNotFoundException.class,
                () -> rideRequestService.getRideRequest(rideRequestId)
        );

        //Then
        verify(rideRequestRepository, times(1)).findById(rideRequestId);
        assertEquals("Ride request with id: 1 not found!", exception.getMessage());
    }
}