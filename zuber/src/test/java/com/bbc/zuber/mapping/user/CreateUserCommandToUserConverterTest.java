package com.bbc.zuber.mapping.user;

import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.command.CreateUserCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.spi.MappingContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.bbc.zuber.model.user.enums.Sex.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class   CreateUserCommandToUserConverterTest {

    @Mock
    private MappingContext<CreateUserCommand, User> mappingContext;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldConvertCreateUserCommandToUser() {
        //Given
        CreateUserCommand command = CreateUserCommand.builder()
                .name("Test")
                .surname("Test")
                .dob(LocalDate.of(2000, 1, 1))
                .email("test.t@example.com")
                .sex(MALE)
                .balance(BigDecimal.valueOf(500))
                .build();

        CreateUserCommandToUserConverter converter = new CreateUserCommandToUserConverter();

        when(mappingContext.getSource())
                .thenReturn(command);

        //When
        User result = converter.convert(mappingContext);

        //Then
        verify(mappingContext, times(1)).getSource();

        assertEquals(command.getName(), result.getName());
        assertEquals(command.getSurname(), result.getSurname());
        assertEquals(command.getDob(), result.getDob());
        assertEquals(command.getEmail(), result.getEmail());
        assertEquals(command.getSex(), result.getSex());
        assertEquals(command.getBalance(), result.getBalance());
    }
}