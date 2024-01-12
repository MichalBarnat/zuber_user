package com.bbc.zuber.service;

import com.bbc.zuber.exception.InsufficientFundsException;
import com.bbc.zuber.exception.UserNotFoundException;
import com.bbc.zuber.exception.UserUuidNotFoundException;
import com.bbc.zuber.kafka.KafkaProducerService;
import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.command.CreateUserCommand;
import com.bbc.zuber.model.user.command.UpdateUserPartiallyCommand;
import com.bbc.zuber.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bbc.zuber.model.user.enums.Sex.FEMALE;
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

class UserServiceTest {

    private User user;
    @Mock
    private UserRepository userRepository;
    @Mock
    private KafkaProducerService producerService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        userService = new UserService(userRepository, producerService);

        String userUuid = "123e4567-e89b-12d3-a456-426614174000";
        user = new User(1L, UUID.fromString(userUuid), "Test", "Test",
                LocalDate.of(2000, 1, 1), MALE, "test.t@example.com",
                BigDecimal.valueOf(1000), false);
    }

    @Test
    void shouldFindByUserId() {
        //Given
        long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        //When
        User result = userService.findById(userId);

        //Then
        verify(userRepository, times(1)).findById(userId);
        assertEquals(user, result);
    }

    @Test
    void shouldFindAllUsers() {
        //Given
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        User user2 = new User();
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user);
        expectedUsers.add(user2);

        when(userRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(expectedUsers));

        //When
        Page<User> result = userService.findAll(pageable);

        //Then
        verify(userRepository, times(1)).findAll(pageable);
        assertEquals(expectedUsers.size(), result.getContent().size());
    }

    @Test
    void shouldFindAllDeletedUsers() {
        //Given
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        User user2 = new User();
        user2.setIsDeleted(true);
        user.setIsDeleted(true);

        List<User> deletedUsers = new ArrayList<>();
        deletedUsers.add(user);
        deletedUsers.add(user2);

        when(userRepository.findAllDeleted(pageable))
                .thenReturn(new PageImpl<>(deletedUsers));

        //When
        Page<User> result = userService.findAllDeleted(pageable);

        //Then
        verify(userRepository, times(1)).findAllDeleted(pageable);
        assertEquals(user, result.getContent().get(0));
        assertEquals(user2, result.getContent().get(1));
    }

    @Test
    void shouldFindByUserUuid() {
        //Given
        String userUuid = "123e4567-e89b-12d3-a456-426614174000";

        when(userRepository.findByUuid(UUID.fromString(userUuid)))
                .thenReturn(Optional.of(user));

        //When
        User result = userService.findByUuid(UUID.fromString(userUuid));

        //Then
        verify(userRepository, times(1)).findByUuid(UUID.fromString(userUuid));
        assertEquals(user, result);
    }

    @Test
    void shouldSaveUser() {
        //Given
        CreateUserCommand command = CreateUserCommand.builder()
                .name("Test")
                .surname("Test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("test.t@example.com")
                .sex(MALE)
                .balance(BigDecimal.valueOf(500))
                .build();

        User userToSave = modelMapper.map(command, User.class);

        when(modelMapper.map(eq(command), eq(User.class)))
                .thenReturn(userToSave);
        when(userRepository.save(any(User.class)))
                .thenReturn(userToSave);

        //When
        User result = userService.save(userToSave);

        //Then
        verify(userRepository, times(1)).save(userToSave);
        verify(producerService, times(1)).sendSavedUser(result);
        assertEquals(userToSave, result);
    }

    @Test
    void shouldDeleteByUserId() {
        //Given
        long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user))
                .thenThrow(new UserNotFoundException(userId));

        //When
        userService.deleteById(userId);

        //Then
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
        verify(producerService, times(1)).sendDeletedUser(user.getUuid());
        assertThrows(UserNotFoundException.class, () -> userService.deleteById(userId));
    }

    @Test
    void shouldEditUser() {
        //Given
        long userId = 1L;
        UpdateUserPartiallyCommand command = UpdateUserPartiallyCommand.builder()
                .name("Test")
                .surname("Test")
                .dob(LocalDate.of(2000, 2, 2))
                .email("Test.t@example.com")
                .sex(FEMALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        //When
        User result = userService.edit(userId, command);

        //Then
        verify(userRepository, times(1)).findById(userId);
        verify(producerService, times(1)).sendEditedUser(user);
        assertEquals(command.getName(), result.getName());
        assertEquals(command.getSurname(), result.getSurname());
        assertEquals(command.getDob(), result.getDob());
        assertEquals(command.getEmail(), result.getEmail());
        assertEquals(command.getSex(), result.getSex());
        assertEquals(command.getBalance(), result.getBalance());
    }

    @Test
    void shouldPayForRide() {
        //Given
        UUID userUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        BigDecimal initialBalance = BigDecimal.valueOf(1000);
        BigDecimal rideAmount = BigDecimal.valueOf(200);

        user.setBalance(initialBalance);

        when(userRepository.findByUuid(userUuid))
                .thenReturn(Optional.of(user));

        //When
        userService.payForRide(userUuid, rideAmount);

        //Then
        verify(userRepository, times(1)).findByUuid(userUuid);
        verify(userRepository, times(1)).save(user);
        assertEquals(new BigDecimal("800"), user.getBalance());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIdDoesNotExistsForFindByIdMethod() {
        //Given
        long userId = 99L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        //When
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findById(userId)
        );

        //Then
        verify(userRepository, times(1)).findById(userId);
        assertEquals("User with id: 99 not found!", exception.getMessage());
    }

    @Test
    void shouldThrowUserUuidNotFoundExceptionWhenUserUuidDoesNotExistsForFindByUuidMethod() {
        //Given
        String userUuid = "999e9999-e99b-99d9-a999-999999999999";

        when(userRepository.findByUuid(UUID.fromString(userUuid)))
                .thenReturn(Optional.empty());

        //When
        UserUuidNotFoundException exception = assertThrows(
                UserUuidNotFoundException.class,
                () -> userService.findByUuid(UUID.fromString(userUuid))
        );

        //Then
        verify(userRepository, times(1)).findByUuid(UUID.fromString(userUuid));
        assertEquals("User with uuid: 999e9999-e99b-99d9-a999-999999999999 not found!", exception.getMessage());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIdDoesNotExistsForDeleteMethod() {
        //Given
        long userId = 99L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty())
                .thenThrow(new UserNotFoundException(userId));

        //When
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteById(userId)
        );

        //Then
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
        assertEquals("User with id: 99 not found!", exception.getMessage());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIdDoesNotExistsForEditMethod() {
        //Given
        long userId = 99L;
        UpdateUserPartiallyCommand command = UpdateUserPartiallyCommand.builder()
                .name("Test")
                .surname("Test")
                .dob(LocalDate.of(2000, 2, 2))
                .email("Test.t@example.com")
                .sex(FEMALE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        //When
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.edit(userId, command)
        );

        //Then
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
        assertEquals("User with id: 99 not found!", exception.getMessage());
    }

    @Test
    void shouldThrowUserUuidNotFoundExceptionWhenUserUuidDoesNotExistsForPayForRideMethod() {
        UUID userUuid = UUID.fromString("999e9999-e99b-99d9-a999-999999999999");
        BigDecimal initialBalance = BigDecimal.valueOf(1000);
        BigDecimal rideAmount = BigDecimal.valueOf(200);

        user.setBalance(initialBalance);

        when(userRepository.findByUuid(userUuid))
                .thenReturn(Optional.empty());

        //When
        UserUuidNotFoundException exception = assertThrows(
                UserUuidNotFoundException.class,
                () -> userService.payForRide(userUuid, rideAmount)
        );

        //Then
        verify(userRepository, times(1)).findByUuid(userUuid);
        verify(userRepository, never()).save(any(User.class));
        assertEquals("User with uuid: 999e9999-e99b-99d9-a999-999999999999 not found!", exception.getMessage());
    }

    @Test
    void shouldThrowInsufficientFundsExceptionWhenCostOfRideIsHigherThanUserBalance() {
        //Given
        UUID userUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal rideAmount = BigDecimal.valueOf(200);

        user.setBalance(initialBalance);

        when(userRepository.findByUuid(userUuid))
                .thenReturn(Optional.of(user));

        //When
        InsufficientFundsException exception = assertThrows(
                InsufficientFundsException.class,
                () -> userService.payForRide(userUuid, rideAmount)
        );

        //Then
        verify(userRepository, times(1)).findByUuid(userUuid);
        verify(userRepository, never()).save(any(User.class));
        assertEquals("User with uuid: 123e4567-e89b-12d3-a456-426614174000 don't have enough money for that ride!",
                exception.getMessage());
    }
}