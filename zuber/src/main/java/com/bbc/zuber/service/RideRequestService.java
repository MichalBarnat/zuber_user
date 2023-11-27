package com.bbc.zuber.service;

import com.bbc.zuber.model.rideRequest.RideRequest;
import com.bbc.zuber.repository.RideRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestService {
    private final RideRequestRepository rideRequestRepository;


    public RideRequest createRideRequest(RideRequest rideRequest) {
        return rideRequestRepository.save(rideRequest);
        // Zapisujemy w repozytorium.
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
