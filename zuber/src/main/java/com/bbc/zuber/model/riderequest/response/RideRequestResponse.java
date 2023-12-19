package com.bbc.zuber.model.riderequest.response;

import com.bbc.zuber.model.riderequest.dto.RideRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RideRequestResponse {
    private String message;
    private HttpStatus status;
    private RideRequestDto rideRequestDto;
}
