package com.bbc.zuber.mapping.user;

import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.spi.MappingContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.bbc.zuber.model.user.enums.Sex.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserToUserDtoConverterTest {

    @Mock
    private MappingContext<User, UserDto> mappingContext;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldConvertUserToUserDto() {
        //Given
        User user = User.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .name("Test")
                .surname("Test")
                .dob(LocalDate.of(2000, 1, 1))
                .sex(MALE)
                .email("test.t@example.com")
                .balance(BigDecimal.valueOf(500))
                .build();

        UserToUserDtoConverter converter = new UserToUserDtoConverter();

        when(mappingContext.getSource())
                .thenReturn(user);

        //When
        UserDto result = converter.convert(mappingContext);

        //Then
        verify(mappingContext, times(1)).getSource();

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getSex(), result.getSex());
    }
}