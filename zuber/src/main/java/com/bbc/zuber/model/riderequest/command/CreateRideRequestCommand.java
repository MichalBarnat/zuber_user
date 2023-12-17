package com.bbc.zuber.model.riderequest.command;

import com.bbc.zuber.model.riderequest.enums.RideRequestSize;
import com.bbc.zuber.model.riderequest.enums.RideRequestType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateRideRequestCommand {

    @NotBlank(message = "PICK_UP_LOCATION_NOT_BLANK")
    private String pickUpLocation;
    @NotBlank(message = "DROP_OFF_LOCATION_NOT_BLANK")
    private String dropOffLocation;
    @NotNull(message = "TYPE_NOT_NULL")
    private RideRequestType type;
    @NotNull(message = "SIZE_NOT_NULL")
    private RideRequestSize size;
    @FutureOrPresent(message = "DATE_MUST_BE_PRESENT_OR_FUTURE")
    @NotNull(message = "DATE_NOT_NULL")
    private LocalDate date;
}
