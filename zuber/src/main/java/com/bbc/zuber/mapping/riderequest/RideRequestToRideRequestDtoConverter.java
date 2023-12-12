package com.bbc.zuber.mapping.riderequest;

import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.dto.RideRequestDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RideRequestToRideRequestDtoConverter implements Converter<RideRequest, RideRequestDto> {

    @Override
    public RideRequestDto convert(MappingContext<RideRequest, RideRequestDto> mappingContext) {
        RideRequest rideRequest = mappingContext.getSource();

        return RideRequestDto.builder()
                .id(rideRequest.getId())
                .uuid(UUID.randomUUID())
                .userId(rideRequest.getUserUuid())
                .pickUpLocation(rideRequest.getPickUpLocation())
                .dropOffLocation(rideRequest.getDropOffLocation())
                .type(rideRequest.getType())
                .size(rideRequest.getSize())
                .date(rideRequest.getDate())
                .build();
    }
}
