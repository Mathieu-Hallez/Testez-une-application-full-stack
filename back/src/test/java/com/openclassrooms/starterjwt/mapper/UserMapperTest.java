package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .lastName("lastname")
                .firstName("firstname")
                .email("test@test.com")
                .password("123456")
                .build();
        userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("123456");
        userDto.setLastName("lastname");
        userDto.setFirstName("firstname");
    }

    @Test
    public void givenUser_whenCallToDto_thenReturnUserDto() {
        UserDto userDtoMapped = userMapper.toDto(user);

        assertEquals(user.getEmail(), userDtoMapped.getEmail());
    }

    @Test
    public void givenUserDto_whenCallToEntity_thenReturnUserEntity() {
        User userMapped = userMapper.toEntity(userDto);

        assertEquals(userMapped.getEmail(), userDto.getEmail());
    }
}
