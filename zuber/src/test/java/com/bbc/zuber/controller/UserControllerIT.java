package com.bbc.zuber.controller;

import com.bbc.zuber.DatabaseCleaner;
import com.bbc.zuber.UserAppApplication;
import com.bbc.zuber.exception.dto.ValidationErrorDto;
import com.bbc.zuber.model.user.command.CreateUserCommand;
import com.bbc.zuber.model.user.command.UpdateUserPartiallyCommand;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.bbc.zuber.model.user.enums.Sex.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UserAppApplication.class)
@AutoConfigureMockMvc
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        },
        controlledShutdown = true,
        brokerPropertiesLocation = "classpath:embedded-kafka-broker-test.yml"
)
@DirtiesContext
@ActiveProfiles("test")
class UserControllerIT {

    private final MockMvc postman;
    private final ObjectMapper objectMapper;
    private final DatabaseCleaner databaseCleaner;

    @Autowired
    public UserControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner) {
        this.postman = postman;
        this.objectMapper = objectMapper;
        this.databaseCleaner = databaseCleaner;
    }

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    void shouldGetUser() throws Exception {
        //Given
        //When
        //Then
        postman.perform(get("/api/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uuid").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.name").value("Jan"))
                .andExpect(jsonPath("$.sex").value("MALE"));
    }

    @Test
    void shouldFindAllUsers() throws Exception {
        //Given
        //When
        //Then
        postman.perform(get("/api/users")
                        .param("size", "3")
                        .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].uuid").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.content[0].name").value("Jan"))
                .andExpect(jsonPath("$.content[0].sex").value("MALE"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].uuid").value("123e4567-e89b-12d3-a456-426614174001"))
                .andExpect(jsonPath("$.content[1].name").value("Anna"))
                .andExpect(jsonPath("$.content[1].sex").value("FEMALE"))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content[2].uuid").value("123e4567-e89b-12d3-a456-426614174002"))
                .andExpect(jsonPath("$.content[2].name").value("Mateusz"))
                .andExpect(jsonPath("$.content[2].sex").value("MALE"));

    }

    @Test
    void shouldFindAllDeletedUsers() throws Exception {
        //Given
        postman.perform(get("/api/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uuid").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.name").value("Jan"))
                .andExpect(jsonPath("$.sex").value("MALE"));

        postman.perform(get("/api/users/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.uuid").value("123e4567-e89b-12d3-a456-426614174001"))
                .andExpect(jsonPath("$.name").value("Anna"))
                .andExpect(jsonPath("$.sex").value("FEMALE"));

        //When
        postman.perform(delete("/api/users/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        postman.perform(delete("/api/users/2"))
                .andDo(print())
                .andExpect(status().isNoContent());

        //Then
        postman.perform(get("/api/users/deleted")
                        .param("size", "3")
                        .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].uuid").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.content[0].name").value("Jan"))
                .andExpect(jsonPath("$.content[0].sex").value("MALE"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].uuid").value("123e4567-e89b-12d3-a456-426614174001"))
                .andExpect(jsonPath("$.content[1].name").value("Anna"))
                .andExpect(jsonPath("$.content[1].sex").value("FEMALE"));
    }

    @Test
    void shouldSaveUser() throws Exception {
        //Given
        CreateUserCommand command = CreateUserCommand.builder()
                .name("test")
                .surname("test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("test.t@example.com")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/12"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 12 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/12"))
                .andExpect(jsonPath("$.method").value("GET"));

        postman.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.name").value(command.getName()))
                .andExpect(jsonPath("$.sex").value(command.getSex().toString()));

        //Then
        postman.perform(get("/api/users/11"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.name").value(command.getName()))
                .andExpect(jsonPath("$.sex").value(command.getSex().toString()));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        //Given
        //When
        postman.perform(get("/api/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uuid").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.name").value("Jan"))
                .andExpect(jsonPath("$.sex").value("MALE"));

        postman.perform(delete("/api/users/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        //Then
        postman.perform(get("/api/users/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 1 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/1"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    void shouldEditUserPartially() throws Exception {
        //Given
        UpdateUserPartiallyCommand command = UpdateUserPartiallyCommand.builder()
                .name("test")
                .surname("test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("test.t@example.com")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.uuid").value("123e4567-e89b-12d3-a456-426614174001"))
                .andExpect(jsonPath("$.name").value("Anna"))
                .andExpect(jsonPath("$.sex").value("FEMALE"));

        postman.perform(patch("/api/users/2")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.uuid").value("123e4567-e89b-12d3-a456-426614174001"))
                .andExpect(jsonPath("$.name").value(command.getName()))
                .andExpect(jsonPath("$.sex").value(command.getSex().toString()));

        //Then
        postman.perform(get("/api/users/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.uuid").value("123e4567-e89b-12d3-a456-426614174001"))
                .andExpect(jsonPath("$.name").value(command.getName()))
                .andExpect(jsonPath("$.sex").value(command.getSex().toString()));
    }

    @Test
    void shouldNotGetUserWhenIdDoesNotExists() throws Exception {
        //Given
        //When
        //Then
        postman.perform(get("/api/users/20"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 20 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/20"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    void shouldNotDeleteUserWhenIdDoesNotExists() throws Exception {
        //Given
        //When
        postman.perform(get("/api/users/20"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 20 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/20"))
                .andExpect(jsonPath("$.method").value("GET"));

        //Then
        postman.perform(delete("/api/users/20"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 20 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/20"))
                .andExpect(jsonPath("$.method").value("DELETE"));
    }

    @Test
    void shouldNotSaveUserWhenNameIsBlank() throws Exception {
        //Given
        CreateUserCommand command = CreateUserCommand.builder()
                .name("")
                .surname("test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("test.t@example.com")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/12"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 12 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/12"))
                .andExpect(jsonPath("$.method").value("GET"));

        //Then
        String responseJson = postman.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.field == 'name' && @.code == 'NAME_NOT_BLANK')]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotSaveUserWhenSurnameIsBlank() throws Exception {
        //Given
        CreateUserCommand command = CreateUserCommand.builder()
                .name("test")
                .surname("")
                .dob(LocalDate.of(2000, 1, 1))
                .email("test.t@example.com")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/12"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 12 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/12"))
                .andExpect(jsonPath("$.method").value("GET"));

        //Then
        String responseJson = postman.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.field == 'surname' && @.code == 'SURNAME_NOT_BLANK')]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotSaveUserWhenDobIsFutureOrPresent() throws Exception {
        //Given
        CreateUserCommand command = CreateUserCommand.builder()
                .name("test")
                .surname("test")
                .dob(LocalDate.of(2030, 1, 1))
                .email("test.t@example.com")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/12"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 12 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/12"))
                .andExpect(jsonPath("$.method").value("GET"));

        //Then
        String responseJson = postman.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.field == 'dob' && @.code == 'DOB_CANNOT_BE_FUTURE_OR_PRESENT')]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotSaveUserWhenDobIsNull() throws Exception {
        //Given
        CreateUserCommand command = CreateUserCommand.builder()
                .name("test")
                .surname("test")
                .dob(null)
                .email("test.t@example.com")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/12"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 12 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/12"))
                .andExpect(jsonPath("$.method").value("GET"));

        //Then
        String responseJson = postman.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.field == 'dob' && @.code == 'DOB_NOT_NULL')]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotSaveUserWhenEmailExists() throws Exception {
        //Given
        CreateUserCommand command = CreateUserCommand.builder()
                .name("test")
                .surname("test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("jan.kowalski@example.com")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/12"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 12 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/12"))
                .andExpect(jsonPath("$.method").value("GET"));

        //Then
        String responseJson = postman.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.field == 'email' && @.code == 'GIVEN_EMAIL_EXISTS')]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotSaveUserWhenEmailFormatIsWrong() throws Exception {
        //Given
        CreateUserCommand command = CreateUserCommand.builder()
                .name("test")
                .surname("test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("test.t")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/12"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 12 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/12"))
                .andExpect(jsonPath("$.method").value("GET"));

        //Then
        String responseJson = postman.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.field == 'email' && @.code == 'INCORRECT_EMAIL_FORMAT')]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotSaveUserWhenEmailIsBlank() throws Exception {
        //Given
        CreateUserCommand command = CreateUserCommand.builder()
                .name("test")
                .surname("test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/12"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 12 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/12"))
                .andExpect(jsonPath("$.method").value("GET"));

        //Then
        String responseJson = postman.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.field == 'email' && @.code == 'EMAIL_NOT_BLANK')]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotSaveUserWhenSexIsNull() throws Exception {
        //Given
        CreateUserCommand command = CreateUserCommand.builder()
                .name("test")
                .surname("test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("test.t@example.com")
                .sex(null)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/12"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 12 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/12"))
                .andExpect(jsonPath("$.method").value("GET"));

        //Then
        String responseJson = postman.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.field == 'sex' && @.code == 'SEX_NOT_NULL')]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotUpdateUserPartiallyWhenIdDoesNotExists() throws Exception {
        //Given
        UpdateUserPartiallyCommand command = UpdateUserPartiallyCommand.builder()
                .name("test")
                .surname("test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("test.t@example.com")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/20"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 20 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/20"))
                .andExpect(jsonPath("$.method").value("GET"));

        //Then
        postman.perform(patch("/api/users/20")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 20 not found!"))
                .andExpect(jsonPath("$.uri").value("/api/users/20"))
                .andExpect(jsonPath("$.method").value("PATCH"));
    }

    @Test
    void shouldNotUpdateUserPartiallyWhenEmailExists() throws Exception {
        //Given
        UpdateUserPartiallyCommand command = UpdateUserPartiallyCommand.builder()
                .name("test")
                .surname("test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("jan.kowalski@example.com")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.uuid").value("123e4567-e89b-12d3-a456-426614174001"))
                .andExpect(jsonPath("$.name").value("Anna"))
                .andExpect(jsonPath("$.sex").value("FEMALE"));

        //Then
        String responseJson = postman.perform(patch("/api/users/2")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.field == 'email' && @.code == 'GIVEN_EMAIL_EXISTS')]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotUpdateUserPartiallyWhenEmailFormatIsWrong() throws Exception {
        //Given
        UpdateUserPartiallyCommand command = UpdateUserPartiallyCommand.builder()
                .name("test")
                .surname("test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("test.t")
                .sex(MALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        String json = objectMapper.writeValueAsString(command);

        //When
        postman.perform(get("/api/users/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.uuid").value("123e4567-e89b-12d3-a456-426614174001"))
                .andExpect(jsonPath("$.name").value("Anna"))
                .andExpect(jsonPath("$.sex").value("FEMALE"));

        //Then
        String responseJson = postman.perform(patch("/api/users/2")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.field == 'email' && @.code == 'INCORRECT_EMAIL_FORMAT')]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertEquals(1, errors.size());
    }
}