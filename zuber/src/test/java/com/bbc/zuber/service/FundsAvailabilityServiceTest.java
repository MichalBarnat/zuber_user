package com.bbc.zuber.service;

import com.bbc.zuber.exception.FundsAvailabilityNotFoundException;
import com.bbc.zuber.exception.FundsAvailabilityUuidNotFoundException;
import com.bbc.zuber.model.fundsavailability.FundsAvailability;
import com.bbc.zuber.repository.FundsAvailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class FundsAvailabilityServiceTest {

    private FundsAvailability fundsAvailability;
    @Mock
    private FundsAvailabilityRepository fundsAvailabilityRepository;
    @InjectMocks
    private FundsAvailabilityService fundsAvailabilityService;

    @BeforeEach
    void setUp() {
        openMocks(this);

        fundsAvailability = new FundsAvailability(1L, UUID.randomUUID(), UUID.randomUUID(),
                "Test", "Test", false);
    }

    @Test
    void shouldSaveFundsAvailability() {
        //Given

        when(fundsAvailabilityRepository.save(any(FundsAvailability.class)))
                .thenReturn(fundsAvailability);

        //When
        FundsAvailability result = fundsAvailabilityService.save(fundsAvailability);

        //Then
        verify(fundsAvailabilityRepository, times(1)).save(fundsAvailability);
        assertEquals(fundsAvailability, result);
    }

    @Test
    void shouldFindAllFundsAvailability() {
        //Given
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        FundsAvailability fundsAvailability2 = new FundsAvailability();

        List<FundsAvailability> expectedFundsAvailability = new ArrayList<>();
        expectedFundsAvailability.add(fundsAvailability);
        expectedFundsAvailability.add(fundsAvailability2);

        when(fundsAvailabilityRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(expectedFundsAvailability));

        //When
        Page<FundsAvailability> result = fundsAvailabilityService.findAll(pageable);

        //Then
        verify(fundsAvailabilityRepository, times(1)).findAll(pageable);
        assertEquals(expectedFundsAvailability.size(), result.getContent().size());
    }

    @Test
    void shouldFindByFundsAvailabilityId() {
        //Given
        long fundsAvailabilityId = 1L;

        when(fundsAvailabilityRepository.findById(fundsAvailabilityId))
                .thenReturn(Optional.of(fundsAvailability));

        //When
        FundsAvailability result = fundsAvailabilityService.findById(fundsAvailabilityId);

        //Then
        verify(fundsAvailabilityRepository, times(1)).findById(fundsAvailabilityId);
        assertEquals(fundsAvailability, result);
    }

    @Test
    void shouldFindByFundsAvailabilityUuid() {
        //Given
        UUID fundsAvailabilityUuid = UUID.randomUUID();
        fundsAvailability.setUuid(fundsAvailabilityUuid);

        when(fundsAvailabilityRepository.findByUuid(fundsAvailabilityUuid))
                .thenReturn(Optional.of(fundsAvailability));

        //When
        FundsAvailability result = fundsAvailabilityService.findByUuid(fundsAvailabilityUuid);

        //Then
        verify(fundsAvailabilityRepository, times(1)).findByUuid(fundsAvailabilityUuid);
        assertEquals(fundsAvailability, result);
    }

    @Test
    void setFundsAvailability() {
        //Given
        UUID fundsAvailabilityUuid = UUID.randomUUID();
        boolean available = true;
        fundsAvailability.setUuid(fundsAvailabilityUuid);

        when(fundsAvailabilityRepository.findByUuid(fundsAvailabilityUuid))
                .thenReturn(Optional.of(fundsAvailability));
        when(fundsAvailabilityRepository.save(any(FundsAvailability.class)))
                .thenReturn(fundsAvailability);

        //When
        fundsAvailabilityService.setFundsAvailability(fundsAvailabilityUuid, available);

        //Then
        verify(fundsAvailabilityRepository, times(1)).findByUuid(fundsAvailabilityUuid);
        verify(fundsAvailabilityRepository, times(1)).save(fundsAvailability);
        assertEquals(available, fundsAvailability.getFundsAvailable());
    }

    @Test
    void shouldThrowFundsAvailabilityNotFoundExceptionForFindByIdMethod() {
        //Given
        long fundsAvailabilityId = 1L;

        when(fundsAvailabilityRepository.findById(fundsAvailabilityId))
                .thenReturn(Optional.empty());

        //When
        FundsAvailabilityNotFoundException exception = assertThrows(
                FundsAvailabilityNotFoundException.class,
                () -> fundsAvailabilityService.findById(fundsAvailabilityId)
        );

        //Then
        verify(fundsAvailabilityRepository, times(1)).findById(fundsAvailabilityId);
        assertEquals("Funds availability with id: 1 not found!", exception.getMessage());
    }

    @Test
    void shouldThrowFundsAvailabilityUuidNotFoundExceptionForFindByUuidMethod() {
        //Given
        UUID fundsAvailabilityUuid = UUID.randomUUID();
        fundsAvailability.setUuid(fundsAvailabilityUuid);

        when(fundsAvailabilityRepository.findByUuid(fundsAvailabilityUuid))
                .thenReturn(Optional.empty());

        //When
        FundsAvailabilityUuidNotFoundException exception = assertThrows(
                FundsAvailabilityUuidNotFoundException.class,
                () -> fundsAvailabilityService.findByUuid(fundsAvailabilityUuid)
        );

        //Then
        verify(fundsAvailabilityRepository, times(1)).findByUuid(fundsAvailabilityUuid);
        assertEquals("Funds availability with uuid: " + fundsAvailabilityUuid + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowFundsAvailabilityUuidNotFoundExceptionForSetFundsAvailabilityMethod() {
        //Given
        UUID fundsAvailabilityUuid = UUID.randomUUID();
        boolean available = true;
        fundsAvailability.setUuid(fundsAvailabilityUuid);

        when(fundsAvailabilityRepository.findByUuid(fundsAvailabilityUuid))
                .thenReturn(Optional.empty());
        when(fundsAvailabilityRepository.save(any(FundsAvailability.class)))
                .thenReturn(fundsAvailability);

        //When
        FundsAvailabilityUuidNotFoundException exception = assertThrows(
                FundsAvailabilityUuidNotFoundException.class,
                () -> fundsAvailabilityService.setFundsAvailability(fundsAvailabilityUuid, available)
        );

        //Then
        verify(fundsAvailabilityRepository, times(1)).findByUuid(fundsAvailabilityUuid);
        verify(fundsAvailabilityRepository, never()).save(any(FundsAvailability.class));
        assertEquals("Funds availability with uuid: " + fundsAvailabilityUuid + " not found!", exception.getMessage());
    }
}