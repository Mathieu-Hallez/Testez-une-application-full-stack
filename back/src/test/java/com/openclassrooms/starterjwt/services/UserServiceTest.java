package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test for User Service.")
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("1234")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
    }

    @Test
    public void givenUserId_whenFindUserByID_thenReturnNullWhenNotFind() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(userService.findById(1L)).isNull();
    }

    @Test
    public void givenUserId_whenFindUserByID_thenReturnTheJavaUserObject() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        assertThat(userService.findById(1L).getFirstName()).isEqualTo("firstname");
    }
}
