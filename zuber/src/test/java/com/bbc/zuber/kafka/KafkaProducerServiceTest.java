package com.bbc.zuber.kafka;

import com.bbc.zuber.exception.KafkaMessageProcessingException;
import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.model.ridecancel.RideCancel;
import com.bbc.zuber.model.riderequest.RideRequest;
import com.bbc.zuber.model.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.bbc.zuber.model.riderequest.enums.RideRequestSize.SINGLE;
import static com.bbc.zuber.model.riderequest.enums.RideRequestType.STANDARD;
import static com.bbc.zuber.model.user.enums.Sex.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class KafkaProducerServiceTest {

    private User user;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks
    private KafkaProducerService producerService;

    @BeforeEach
    void setUp() {
        openMocks(this);

        String userUuid = "123e4567-e89b-12d3-a456-426614174000";
        user = new User(1L, UUID.fromString(userUuid), "Test", "Test",
                LocalDate.of(2000, 1, 1), MALE, "test.t@example.com", BigDecimal.valueOf(1000));
    }

    @Test
    void shouldSendRideCancelToKafka() throws JsonProcessingException {
        //Given
        RideCancel rideCancel = RideCancel.builder()
                .id(1L)
                .rideAssignmentUuid(UUID.randomUUID())
                .cancel(true)
                .build();

        String expectedJson = "{" +
                "\"id\":" + rideCancel.getId() + "," +
                "\"rideAssignmentId\":\"" + rideCancel.getRideAssignmentUuid() + "\"," +
                "\"cancel\":" + rideCancel.getCancel() + "\"" +
                "}";

        when(objectMapper.writeValueAsString(rideCancel))
                .thenReturn(expectedJson);

        //When
        producerService.sendRideCancel(rideCancel);

        //Then
        verify(objectMapper, times(1)).writeValueAsString(rideCancel);
        verify(kafkaTemplate, times(1)).send(eq("ride-cancel"), eq(expectedJson));
    }

    @Test
    void shouldSendUserFundsAvailabilityToKafka() throws JsonProcessingException {
        //Given
        FundsAvailability fundsAvailability = FundsAvailability.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .userUuid(user.getUuid())
                .pickUpLocation("Test")
                .dropOffLocation("Test")
                .fundsAvailable(true)
                .build();

        String expectedJson = "{" +
                "\"id\":" + fundsAvailability.getId() + "," +
                "\"uuid\":\"" + fundsAvailability.getUuid() + "\"," +
                "\"userUuid\":\"" + fundsAvailability.getUserUuid() + "\"," +
                "\"pickUpLocation\":\"" + fundsAvailability.getPickUpLocation() + "\"," +
                "\"dropOffLocation\":\"" + fundsAvailability.getDropOffLocation() + "\"," +
                "\"fundsAvailable\":" + fundsAvailability.getFundsAvailable() + "\"" +
                "}";

        when(objectMapper.writeValueAsString(fundsAvailability))
                .thenReturn(expectedJson);

        //When
        producerService.sendUserFundsAvailability(fundsAvailability);

        //Then
        verify(objectMapper, times(1)).writeValueAsString(fundsAvailability);
        verify(kafkaTemplate, times(1)).send(eq("user-funds-availability"), eq(expectedJson));
    }

    @Test
    void shouldSendRideRequestToKafka() throws JsonProcessingException {
        //Given
        RideRequest rideRequest = RideRequest.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .userUuid(user.getUuid())
                .pickUpLocation("Test")
                .dropOffLocation("Test")
                .type(STANDARD)
                .size(SINGLE)
                .date(LocalDate.of(2000, 1, 1))
                .build();

        String expectedJson = "{" +
                "\"id\":" + user.getId() + "," +
                "\"uuid\":\"" + rideRequest.getUuid() + "\"," +
                "\"userUuid\":\"" + rideRequest.getUserUuid() + "\"," +
                "\"pickUpLocation\":\"" + rideRequest.getPickUpLocation() + "\"," +
                "\"dropOffLocation\":\"" + rideRequest.getDropOffLocation() + "\"," +
                "\"type\":\"" + rideRequest.getType() + "\"," +
                "\"size\":\"" + rideRequest.getSize() + "\"," +
                "\"date\":\"" + rideRequest.getDate() + "\"" +
                "}";

        when(objectMapper.writeValueAsString(rideRequest))
                .thenReturn(expectedJson);

        //When
        producerService.sendRideRequest(rideRequest);

        //Then
        verify(objectMapper, times(1)).writeValueAsString(rideRequest);
        verify(kafkaTemplate, times(1)).send(eq("ride-request"), eq(expectedJson));
    }

    @Test
    void shouldSendSavedUserToKafka() throws JsonProcessingException {
        //Given
        String expectedJson = "{" +
                "\"id\":" + user.getId() + "," +
                "\"uuid\":\"" + user.getUuid() + "\"," +
                "\"name\":\"" + user.getName() + "\"," +
                "\"surname\":\"" + user.getSurname() + "\"," +
                "\"dob\":\"" + user.getDob() + "\"," +
                "\"sex\":\"" + user.getSex() + "\"," +
                "\"email\":\"" + user.getEmail() + "\"," +
                "\"balance\":" + user.getBalance() + "\"," +
                "}";

        when(objectMapper.writeValueAsString(user))
                .thenReturn(expectedJson);

        //When
        producerService.sendSavedUser(user);

        //Then
        verify(objectMapper, times(1)).writeValueAsString(user);
        verify(kafkaTemplate, times(1)).send(eq("user-registration"), eq(expectedJson));
    }

    @Test
    void shouldSendDeletedUserToKafka() {
        //Given
        UUID userUuid = UUID.randomUUID();

        //When
        producerService.sendDeletedUser(userUuid);

        //Then
        verify(kafkaTemplate, times(1)).send(eq("user-deleted"), eq(userUuid));
    }

    @Test
    void shouldSendEditedUserToKafka() throws JsonProcessingException {
        //Given
        String expectedJson = "{" +
                "\"id\":" + user.getId() + "," +
                "\"uuid\":\"" + user.getUuid() + "\"," +
                "\"name\":\"" + user.getName() + "\"," +
                "\"surname\":\"" + user.getSurname() + "\"," +
                "\"dob\":\"" + user.getDob() + "\"," +
                "\"sex\":\"" + user.getSex() + "\"," +
                "\"email\":\"" + user.getEmail() + "\"," +
                "\"balance\":" + user.getBalance() + "\"," +
                "}";

        when(objectMapper.writeValueAsString(user))
                .thenReturn(expectedJson);

        //When
        producerService.sendEditedUser(user);

        //Then
        verify(objectMapper, times(1)).writeValueAsString(user);
        verify(kafkaTemplate, times(1)).send(eq("user-edited"), eq(expectedJson));
    }

    @Test
    void shouldHandleJsonProcessingExceptionForSendRideCancelMethod() throws JsonProcessingException {
        //Given
        RideCancel rideCancel = RideCancel.builder()
                .id(1L)
                .rideAssignmentUuid(UUID.randomUUID())
                .cancel(true)
                .build();

        when(objectMapper.writeValueAsString(rideCancel))
                .thenThrow(JsonProcessingException.class);

        //When
        KafkaMessageProcessingException exception = assertThrows(
                KafkaMessageProcessingException.class,
                () -> producerService.sendRideCancel(rideCancel)
        );

        //Then
        verify(objectMapper, times(1)).writeValueAsString(rideCancel);
        verify(kafkaTemplate, never()).send(any(), any());
        assertEquals("Problem with sending to topic ride-cancel", exception.getMessage());
    }

    @Test
    void shouldHandleJsonProcessingExceptionForSendUserFundsAvailabilityMethod() throws JsonProcessingException {
        //Given
        FundsAvailability fundsAvailability = FundsAvailability.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .userUuid(user.getUuid())
                .pickUpLocation("Test")
                .dropOffLocation("Test")
                .fundsAvailable(true)
                .build();

        when(objectMapper.writeValueAsString(fundsAvailability))
                .thenThrow(JsonProcessingException.class);

        //When
        KafkaMessageProcessingException exception = assertThrows(
                KafkaMessageProcessingException.class,
                () -> producerService.sendUserFundsAvailability(fundsAvailability)
        );

        //Then
        verify(objectMapper, times(1)).writeValueAsString(fundsAvailability);
        verify(kafkaTemplate, never()).send(any(), any());
        assertEquals("Problem with sending to topic user-funds-availability", exception.getMessage());
    }

    @Test
    void shouldHandleJsonProcessingExceptionForSendRideRequestMethod() throws JsonProcessingException {
        //Given
        RideRequest rideRequest = RideRequest.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .userUuid(user.getUuid())
                .pickUpLocation("Test")
                .dropOffLocation("Test")
                .type(STANDARD)
                .size(SINGLE)
                .date(LocalDate.of(2000, 1, 1))
                .build();

        when(objectMapper.writeValueAsString(rideRequest))
                .thenThrow(JsonProcessingException.class);

        //When
        KafkaMessageProcessingException exception = assertThrows(
                KafkaMessageProcessingException.class,
                () -> producerService.sendRideRequest(rideRequest)
        );

        //Then
        verify(objectMapper, times(1)).writeValueAsString(rideRequest);
        verify(kafkaTemplate, never()).send(any(), any());
        assertEquals("Problem with sending to topic ride-request", exception.getMessage());
    }

    @Test
    void shouldHandleJsonProcessingExceptionForSendSavedUserMethod() throws JsonProcessingException {
        //Given
        when(objectMapper.writeValueAsString(user))
                .thenThrow(JsonProcessingException.class);

        //When
        KafkaMessageProcessingException exception = assertThrows(
                KafkaMessageProcessingException.class,
                () -> producerService.sendSavedUser(user)
        );

        //Then
        verify(objectMapper, times(1)).writeValueAsString(user);
        verify(kafkaTemplate, never()).send(any(), any());
        assertEquals("Problem with sending to topic user-registration", exception.getMessage());
    }

    @Test
    void shouldHandleJsonProcessingExceptionForEditedUserMethod() throws JsonProcessingException {
        //Given
        when(objectMapper.writeValueAsString(user))
                .thenThrow(JsonProcessingException.class);

        //When
        KafkaMessageProcessingException exception = assertThrows(
                KafkaMessageProcessingException.class,
                () -> producerService.sendEditedUser(user)
        );

        //Then
        verify(objectMapper, times(1)).writeValueAsString(user);
        verify(kafkaTemplate, never()).send(any(), any());
        assertEquals("Problem with sending to topic user-edited", exception.getMessage());
    }
}