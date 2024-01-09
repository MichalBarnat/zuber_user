package com.bbc.zuber.kafka;

import com.bbc.zuber.model.rideinfo.RideInfo;
import com.bbc.zuber.model.user.User;
import com.bbc.zuber.service.FundsAvailabilityService;
import com.bbc.zuber.service.RideInfoService;
import com.bbc.zuber.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class KafkaListenersTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RideInfoService rideInfoService;

    @Mock
    private FundsAvailabilityService fundsAvailabilityService;

    @Mock
    private UserService userService;

    @InjectMocks
    private KafkaListeners kafkaListeners;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldConsumeRideInfoListenerFromKafka() throws JsonProcessingException {
        //Given
        RideInfo rideInfo = RideInfo.builder()
                .rideAssignmentUuid(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                .userUuid(UUID.fromString("550e8401-e29b-41d4-a716-446655440000"))
                .driverUuid(UUID.fromString("550e8402-e29b-41d4-a716-446655440000"))
                .driverName("Test")
                .driverLocation("Test")
                .pickUpLocation("Test")
                .dropUpLocation("Test")
                .orderTime(LocalDateTime.parse("2024-01-02T10:15:30"))
                .estimatedArrivalTime(LocalDateTime.parse("2024-01-02T11:30:45"))
                .costOfRide(new BigDecimal("10.50"))
                .timeToArrivalInMinutes("15")
                .rideLengthInKilometers("5.7")
                .build();

        String rideInfoString = objectMapper.writeValueAsString(rideInfo);

        when(objectMapper.readValue(rideInfoString, RideInfo.class))
                .thenReturn(rideInfo);

        //When
        kafkaListeners.rideInfoListener(rideInfoString);

        //Then
        verify(objectMapper, times(1)).readValue(rideInfoString, RideInfo.class);
        verify(rideInfoService, times(1)).save(any(RideInfo.class));
        ArgumentCaptor<RideInfo> rideInfoCaptor = ArgumentCaptor.forClass(RideInfo.class);
        verify(rideInfoService).save(rideInfoCaptor.capture());

        RideInfo capturedRideInfo = rideInfoCaptor.getValue();
        assertEquals(rideInfo.getRideAssignmentUuid(), capturedRideInfo.getRideAssignmentUuid());
        assertEquals(rideInfo.getUserUuid(), capturedRideInfo.getUserUuid());
        assertEquals(rideInfo.getDriverUuid(), capturedRideInfo.getDriverUuid());
        assertEquals(rideInfo.getDriverName(), capturedRideInfo.getDriverName());
        assertEquals(rideInfo.getDriverLocation(), capturedRideInfo.getDriverLocation());
        assertEquals(rideInfo.getPickUpLocation(), capturedRideInfo.getPickUpLocation());
        assertEquals(rideInfo.getDropUpLocation(), capturedRideInfo.getDropUpLocation());
        assertEquals(rideInfo.getOrderTime(), capturedRideInfo.getOrderTime());
        assertEquals(rideInfo.getEstimatedArrivalTime(), capturedRideInfo.getEstimatedArrivalTime());
        assertEquals(rideInfo.getCostOfRide(), capturedRideInfo.getCostOfRide());
        assertEquals(rideInfo.getTimeToArrivalInMinutes(), capturedRideInfo.getTimeToArrivalInMinutes());
        assertEquals(rideInfo.getRideAssignmentUuid(), capturedRideInfo.getRideAssignmentUuid());
    }

    @Test
    void shouldConsumeFundsAvailabilityResponseListenerFromKafka() throws JsonProcessingException {
        // Given
        String responseJson = "{" +
                "\"uuid\":\"" + UUID.randomUUID() + "\"," +
                "\"userUuid\":\"" + UUID.randomUUID() + "\"," +
                "\"cost\":10.50" +
                "}";

        JsonNode jsonNode = mock(JsonNode.class);
        UUID uuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();
        User user = User.builder()
                .uuid(userUuid)
                .balance(new BigDecimal("15.00"))
                .build();

        when(objectMapper.readTree(responseJson))
                .thenReturn(jsonNode);
        when(jsonNode.get("uuid"))
                .thenReturn(mock(JsonNode.class));
        when(jsonNode.get("uuid").asText())
                .thenReturn(uuid.toString());
        when(jsonNode.get("userUuid"))
                .thenReturn(mock(JsonNode.class));
        when(jsonNode.get("userUuid").asText())
                .thenReturn(userUuid.toString());
        when(jsonNode.get("cost"))
                .thenReturn(mock(JsonNode.class));
        when(jsonNode.get("cost").asDouble())
                .thenReturn(10.50);

        when(userService.findByUuid(userUuid))
                .thenReturn(user);

        // When
        kafkaListeners.fundsAvailabilityResponseListener(responseJson);

        // Then
        verify(objectMapper, times(1)).readTree(responseJson);
        verify(userService, times(1)).findByUuid(userUuid);
        verify(fundsAvailabilityService).setFundsAvailability(uuid, true);
    }
}