package com.bbc.zuber.mapping.riderequest;

import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.command.UpdateRideRequestCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;

import static com.bbc.zuber.model.riderequest.enums.RideRequestSize.SINGLE;
import static com.bbc.zuber.model.riderequest.enums.RideRequestType.STANDARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UpdateRideRequestCommandToRideRequestConverterTest {

    @Mock
    private MappingContext<UpdateRideRequestCommand, RideRequest> mappingContext;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldConvertUpdateRideRequestCommandToRideRequest() {
        //Given
        UpdateRideRequestCommand command = UpdateRideRequestCommand.builder()
                .pickUpLocation("Test")
                .dropOffLocation("Test")
                .type(STANDARD)
                .size(SINGLE)
                .date(LocalDate.of(2024, 2, 2))
                .build();

        UpdateRideRequestCommandToRideRequestConverter converter = new UpdateRideRequestCommandToRideRequestConverter();

        when(mappingContext.getSource())
                .thenReturn(command);

        //When
        RideRequest result = converter.convert(mappingContext);

        //Then
        verify(mappingContext, times(1)).getSource();

        assertEquals(command.getPickUpLocation(), result.getPickUpLocation());
        assertEquals(command.getDropOffLocation(), result.getDropOffLocation());
        assertEquals(command.getType(), result.getType());
        assertEquals(command.getSize(), result.getSize());
        assertEquals(command.getDate(), result.getDate());
    }
}