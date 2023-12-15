package com.bbc.zuber.model.riderequest.command;

import com.bbc.zuber.model.riderequest.enums.Size;
import com.bbc.zuber.model.riderequest.enums.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

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
    private Type type;
    @NotNull(message = "SIZE_NOT_NULL")
    private Size size;
    @Future(message = "DATE_CANNOT_BE_PAST_OR_PRESENT")
    @NotNull(message = "DATE_NOT_NULL")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = STRING)
    private LocalDateTime date;
}
