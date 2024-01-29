package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test for UserDetailServiceImpl.")
public class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

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
    public void givenUserName_whenLoadUserByUsername_thenReturnUsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());


        assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(() -> userDetailsServiceImpl.loadUserByUsername("test@gmail.com"));
    }

    @Test
    public void givenUserName_whenLoadUserByUsername_thenReturnJavaUserDetailsImplObject() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));

        final UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername("test@test.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getLastName()).isEqualTo(user.getLastName());
    }
}
