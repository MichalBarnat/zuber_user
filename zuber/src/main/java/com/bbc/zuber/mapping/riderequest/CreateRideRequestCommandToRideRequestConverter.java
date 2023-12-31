package com.bbc.zuber.mapping.riderequest;

import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.command.CreateRideRequestCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateRideRequestCommandToRideRequestConverter implements Converter<CreateRideRequestCommand, RideRequest> {

    @Override
    public RideRequest convert(MappingContext<CreateRideRequestCommand, RideRequest> mappingContext) {
        CreateRideRequestCommand command = mappingContext.getSource();

        return RideRequest.builder()
                .uuid(UUID.randomUUID())
                .pickUpLocation(command.getPickUpLocation())
                .dropOffLocation(command.getDropOffLocation())
                .type(command.getType())
                .size(command.getSize())
                .date(command.getDate())
                .build();
    }
}
