package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void testToDto() {
        User user = User.builder()
                .lastName("lastname")
                .firstName("firstname")
                .email("test@test.com")
                .password("123456")
                .build();

        UserDto userDto = userMapper.toDto(user);

        assertEquals(user.getEmail(), userDto.getEmail());
    }
}
