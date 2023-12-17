package com.bbc.zuber.service;

import com.bbc.zuber.exception.RideRequestNotFoundException;
import com.bbc.zuber.exception.UserNotFoundException;
import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.dto.RideRequestDto;
import com.bbc.zuber.model.riderequest.response.RideRequestResponse;
import com.bbc.zuber.model.user.User;
import com.bbc.zuber.repository.RideRequestRepository;
import com.bbc.zuber.repository.UserRepository;
import com.bbc.zuber.service.producer.RideRequestProducerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;

@Service
@RequiredArgsConstructor
public class RideRequestService {

    private final RideRequestRepository rideRequestRepository;
    private final UserRepository userRepository;
    private final FundsAvailabilityService fundsAvailabilityService;
    private final RideRequestProducerService rideRequestProducerService;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public RideRequest getRideRequest(long id) {
        return rideRequestRepository.findById(id)
                .orElseThrow(() -> new RideRequestNotFoundException(id));
    }

    public RideRequestResponse createRideRequestResponse(RideRequest rideRequest, long userId) {
        RideRequestDto dto = modelMapper.map(rideRequest, RideRequestDto.class);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        UUID fundsRequestUuid = UUID.randomUUID();
        FundsAvailability fundsAvailability = FundsAvailability.builder()
                .uuid(fundsRequestUuid)
                .userUuid(user.getUuid())
                .pickUpLocation(rideRequest.getPickUpLocation())
                .dropOffLocation(rideRequest.getDropOffLocation())
                .build();

        fundsAvailabilityService.save(fundsAvailability);

        rideRequestProducerService.sendUserFundsAvailability(fundsAvailability);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (fundsAvailabilityService.findByUuid(fundsRequestUuid).getFundsAvailable() == null) {
            return RideRequestResponse.builder()
                    .message("Timeout reached waiting for funds availability or null.")
                    .status(REQUEST_TIMEOUT)
                    .rideRequestDto(dto)
                    .build();
        }

        if (!fundsAvailabilityService.findByUuid(fundsRequestUuid).getFundsAvailable()) {
            return RideRequestResponse.builder()
                    .message("User doesn't have enough funds for this ride!")
                    .status(FORBIDDEN)
                    .rideRequestDto(dto)
                    .build();
        }

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);
        rideRequestProducerService.sendRideRequest(savedRideRequest);
        dto = modelMapper.map(rideRequest, RideRequestDto.class);
        return RideRequestResponse.builder()
                .message("Ride request created successfully!")
                .status(CREATED)
                .rideRequestDto(dto)
                .build();
    }

    public void cancelRideRequest() {
        return;
        // Trzeba rozważyć 2 warianty.
        // Anulujemy przejazd bez opłaty jeśli nie złapał go jakiś kierowca i jest do nas w drodze
        // oraz taki gdzie nie płaci użytkownik nic, gdy żaden z kierowców go nie zaakceptował.
        // Tu jest ważna wymiana informacji na temat statusu przejazdu.
    }

    public RideRequest getRideRequestStatus() {
        return null;
        // Sprawdzamy czy przejazd złapał jakiś kierowca.
    }

    public RideRequest getRideRequestDetails() {
        return null;
        // Tu wyświetlimy wszystkie szczegóły.
    }
}
