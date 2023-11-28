package com.bbc.zuber.mappings.rideRequest;

import com.bbc.zuber.model.rideRequest.RideRequest;
import com.bbc.zuber.model.rideRequest.command.UpdateRideRequestCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;


@Service
public class UpdateRideRequestCommandToRideRequestConverter implements Converter<UpdateRideRequestCommand, RideRequest> {

    @Override
    public RideRequest convert(MappingContext<UpdateRideRequestCommand, RideRequest> mappingContext) {
        UpdateRideRequestCommand command = mappingContext.getSource();

        return RideRequest.builder()
                .pickUpLocation(command.getPickUpLocation())
                .dropOffLocation(command.getDropOffLocation())
                .type(command.getType())
                .size(command.getSize())
                .date(command.getDate())
                .build();
    }
}
