package com.bbc.zuber.service;

import com.bbc.zuber.exception.RideRequestNotFoundException;
import com.bbc.zuber.exception.UserNotFoundException;
import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.riderequest.response.RideRequestResponse;
import com.bbc.zuber.model.user.User;
import com.bbc.zuber.repository.RideRequestRepository;
import com.bbc.zuber.repository.UserRepository;
import com.bbc.zuber.service.producer.RideRequestProducerService;
import lombok.RequiredArgsConstructor;
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
    private final RideRequestProducerService producerService;

    @Transactional(readOnly = true)
    public RideRequest getRideRequest(long id) {
        return rideRequestRepository.findById(id)
                .orElseThrow(() -> new RideRequestNotFoundException(id));
    }

    @Transactional
    public RideRequestResponse createRideRequest(RideRequest rideRequest, long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        UUID requestUuid = UUID.randomUUID();
        FundsAvailability fundsAvailability = FundsAvailability.builder()
                .uuid(requestUuid)
                .userUuid(user.getUuid())
                .pickUpLocation(rideRequest.getPickUpLocation())
                .dropOffLocation(rideRequest.getDropOffLocation())
                .fundsAvailable(false)
                .build();

        fundsAvailabilityService.save(fundsAvailability);

        producerService.sendUserFundsAvailability(fundsAvailability);

        if (fundsAvailabilityService.findByUuid(requestUuid).getFundsAvailable() == null) {
            return RideRequestResponse.builder()
                    .message("Timeout reached waiting for funds availability!")
                    .status(REQUEST_TIMEOUT)
                    .build();
        }

        if (!fundsAvailabilityService.findByUuid(requestUuid).getFundsAvailable()) {
            return RideRequestResponse.builder()
                    .message("User doesn't have enough funds for this ride!")
                    .status(FORBIDDEN)
                    .build();
        }

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);
        producerService.sendRideRequest(savedRideRequest);
        return RideRequestResponse.builder()
                .message("Ride request created successfully!")
                .status(CREATED)
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
