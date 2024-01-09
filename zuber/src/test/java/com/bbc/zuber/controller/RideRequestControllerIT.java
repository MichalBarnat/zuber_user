//package com.bbc.zuber.controller;
//
//import com.bbc.zuber.DatabaseCleaner;
//import com.bbc.zuber.UserAppApplication;
//import com.bbc.zuber.model.riderequest.command.CreateRideRequestCommand;
//import com.bbc.zuber.model.riderequest.enums.RideRequestSize;
//import com.bbc.zuber.model.riderequest.enums.RideRequestType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import liquibase.exception.LiquibaseException;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//
//import static com.bbc.zuber.model.riderequest.enums.RideRequestSize.SINGLE;
//import static com.bbc.zuber.model.riderequest.enums.RideRequestType.STANDARD;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(classes = UserAppApplication.class)
//@AutoConfigureMockMvc
//@EmbeddedKafka(
//        partitions = 1,
//        brokerProperties = {
//                "listeners=PLAINTEXT://localhost:9092",
//                "port=9092"
//        },
//        controlledShutdown = true,
//        brokerPropertiesLocation = "classpath:embedded-kafka-broker-test.yml"
//)
//@DirtiesContext
//@ActiveProfiles("test")
//class RideRequestControllerIT {
//
//    private final MockMvc postman;
//    private final ObjectMapper objectMapper;
//    private final DatabaseCleaner databaseCleaner;
//
//    @Autowired
//    public RideRequestControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner) {
//        this.postman = postman;
//        this.objectMapper = objectMapper;
//        this.databaseCleaner = databaseCleaner;
//    }
//
//    @AfterEach
//    void tearDown() throws LiquibaseException {
//        databaseCleaner.cleanUp();
//    }
//
//    @Test
//    void shouldGetRideRequest() throws Exception {
//        //Given
//        //When
//        CreateRideRequestCommand command = CreateRideRequestCommand.builder()
//                .pickUpLocation("Test")
//                .dropOffLocation("Test")
//                .type(STANDARD)
//                .size(SINGLE)
//                .date(LocalDate.of(2024, 2, 2))
//                .build();
//
//        String json = objectMapper.writeValueAsString(command);
//
//        postman.perform(post("/api/rideRequests/1")
//                        .contentType(APPLICATION_JSON)
//                        .content(json))
//                .andDo(print())
//                .andExpect(status().isCreated());
//
//        //Then
//        postman.perform(get("/api/rideRequests/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.pickUpLocation").value("Test"))
//                .andExpect(jsonPath("$.dropOffLocation").value("Test"))
//                .andExpect(jsonPath("$.type").value("STANDARD"))
//                .andExpect(jsonPath("$.size").value("SINGLE"))
//                .andExpect(jsonPath("$.date").value("2024-02-02"));
//
//    }
//
//    @Test
//    void shouldSaveRideRequest() throws Exception {
//        //Given
//        CreateRideRequestCommand command = CreateRideRequestCommand.builder()
//                .pickUpLocation("Test")
//                .dropOffLocation("Test")
//                .type(STANDARD)
//                .size(SINGLE)
//                .date(LocalDate.of(2024, 2, 2))
//                .build();
//
//        String json = objectMapper.writeValueAsString(command);
//
//        //When
//        postman.perform(get("/api/rideRequests/1"))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Ride request with id: 1 not found!"))
//                .andExpect(jsonPath("$.uri").value("/api/rideRequests/1"))
//                .andExpect(jsonPath("$.method").value("GET"));
//
//        postman.perform(post("/api/rideRequests/1")
//                        .contentType(APPLICATION_JSON)
//                        .content(json))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.message").value("Ride request created successfully!"))
//                .andExpect(jsonPath("$.status").value("CREATED"))
//                .andExpect(jsonPath("$.rideRequestDto.id").value(1))
//                .andExpect(jsonPath("$.rideRequestDto.pickUpLocation").value(command.getPickUpLocation()))
//                .andExpect(jsonPath("$.rideRequestDto.dropOffLocation").value(command.getDropOffLocation()))
//                .andExpect(jsonPath("$.rideRequestDto.type").value(command.getType().toString()))
//                .andExpect(jsonPath("$.rideRequestDto.size").value(command.getSize().toString()))
//                .andExpect(jsonPath("$.rideRequestDto.date").value(command.getDate().toString()));
//
//        //Then
//        postman.perform(get("/api/rideRequests/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.pickUpLocation").value(command.getPickUpLocation()))
//                .andExpect(jsonPath("$.dropOffLocation").value(command.getDropOffLocation()))
//                .andExpect(jsonPath("$.type").value(command.getType().toString()))
//                .andExpect(jsonPath("$.size").value(command.getSize().toString()))
//                .andExpect(jsonPath("$.date").value(command.getDate().toString()));
//    }
//}