package com.bbc.zuber.mapping.riderequest;

import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.dto.RideRequestDto;
import com.bbc.zuber.model.riderequest.enums.RideRequestSize;
import com.bbc.zuber.model.riderequest.enums.RideRequestType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;
import java.util.UUID;

import static com.bbc.zuber.model.riderequest.enums.RideRequestSize.SINGLE;
import static com.bbc.zuber.model.riderequest.enums.RideRequestType.STANDARD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RideRequestToRideRequestDtoConverterTest {

    @Mock
    private MappingContext<RideRequest, RideRequestDto> mappingContext;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldConvertRideRequestToRideRequestDto() {
        //Given
        RideRequest rideRequest = RideRequest.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .userUuid(UUID.randomUUID())
                .pickUpLocation("Test")
                .dropOffLocation("Test")
                .type(STANDARD)
                .size(SINGLE)
                .date(LocalDate.of(2024,2,2))
                .build();

        RideRequestToRideRequestDtoConverter converter = new RideRequestToRideRequestDtoConverter();

        when(mappingContext.getSource())
                .thenReturn(rideRequest);

        //When
        RideRequestDto result = converter.convert(mappingContext);

        //Then
        verify(mappingContext, times(1)).getSource();

        assertEquals(rideRequest.getId(), result.getId());
        assertEquals(rideRequest.getUserUuid(), result.getUserUuid());
        assertEquals(rideRequest.getPickUpLocation(), result.getPickUpLocation());
        assertEquals(rideRequest.getDropOffLocation(), result.getDropOffLocation());
        assertEquals(rideRequest.getType(), result.getType());
        assertEquals(rideRequest.getSize(), result.getSize());
        assertEquals(rideRequest.getDate(), result.getDate());
    }
}